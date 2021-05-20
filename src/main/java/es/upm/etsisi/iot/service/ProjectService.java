package es.upm.etsisi.iot.service;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import es.upm.etsisi.iot.dto.ProjectDto;
import es.upm.etsisi.iot.dto.SensorDto;
import es.upm.etsisi.iot.dto.SensorValueDto;
import es.upm.etsisi.iot.modelo.ProjectEntity;
import es.upm.etsisi.iot.modelo.dao.ProjectRepository;
import es.upm.etsisi.iot.modelo.dao.SensorTypeRepository;
import es.upm.etsisi.iot.security.entity.User;
import es.upm.etsisi.iot.security.repository.UserRepository;
import es.upm.etsisi.iot.utils.Utilities;

@Service
public class ProjectService {

	private ProjectRepository projectRepository;
	private UserRepository userRepository;
	private SensorTypeRepository sensorTypeRepository;

	private ModelMapper modelMapper;
	
	@Autowired
	public ProjectService(ProjectRepository projectRepository, UserRepository userRepository, SensorTypeRepository sensorTypeRepository, ModelMapper modelMapper) {
		this.projectRepository = projectRepository;
		this.userRepository = userRepository; 
		this.sensorTypeRepository = sensorTypeRepository;
		this.modelMapper = modelMapper;
	}

	private List<SensorDto> processCsvData(ProjectDto project, MultipartFile file) throws Exception {
		List<SensorDto> sensors = new ArrayList<>();
		
		InputStream is = null;
		BufferedReader bfReader = null;
		List<String> csvLines = new ArrayList<>();
		try {
			is = new ByteArrayInputStream(file.getBytes());
			bfReader = new BufferedReader(new InputStreamReader(is));
			String temp = null;
			while ((temp = bfReader.readLine()) != null) {
				csvLines.add(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception ex) {
				throw ex;
			}
		}
		
		if(csvLines.size() > 1) {
			Date sysDate = new Date();
			Optional<User> optionalUser = this.userRepository.findByUsername(Utilities.getCurrentUser().getUsername());
			int numValues = csvLines.size();
			int numSensors = csvLines.get(0).split(";").length;
			String[][] csvMatrix = new String [numValues][numSensors];
			
			for(int col = 1; col < numSensors; col++) {
				String sensorName = csvMatrix[0][col];
				SensorDto sensorDto = new SensorDto();
				sensorDto.setName(sensorName);
				sensorDto.setDateCreated(sysDate);
				sensorDto.setDateLastModified(sysDate);
				sensorDto.setCreatedUser(optionalUser.get().toUserDto());
				sensorDto.setLastModifieduser(optionalUser.get().toUserDto());
				
				for (int row = 1; row < numValues; row++) {
					String value = csvMatrix[row][col];
					String timestamp = csvMatrix[row][0];
					
					SensorValueDto sensorValue = new SensorValueDto();
					sensorValue.setValue(new BigDecimal(value));  
					sensorValue.setTimestamp(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(timestamp));
					sensorValue.setDateCreated(sysDate);
					sensorValue.setDateLastModified(sysDate);
					sensorValue.setCreatedUser(optionalUser.get().toUserDto());
					sensorValue.setLastModifieduser(optionalUser.get().toUserDto());
				}
			}
		} else {
			throw new Exception("El fichero CSV no contiene valores");
		}
		
		
		return sensors;
	}
	
	public ProjectDto newProject(ProjectDto project, MultipartFile file) throws Exception {
		Date currentDate = new Date();
		
		List<SensorDto> sensors = processCsvData(project, file);
		
		Optional<User> optionalUser = this.userRepository.findByUsername(Utilities.getCurrentUser().getUsername());
		
		if(optionalUser.isPresent()) {
			project.setCreatedUser(optionalUser.get().toUserDto());
			project.setLastModifieduser(optionalUser.get().toUserDto());
			
			project.getSensors().stream().forEach(x -> {
				x.setCreatedUser(optionalUser.get().toUserDto());
				x.setLastModifieduser(optionalUser.get().toUserDto());
				x.setDateCreated(currentDate);
				x.setDateLastModified(currentDate);
			});
		}
		
		project.setDateLastModified(currentDate);
		project.setDateCreated(currentDate);
		
		ProjectEntity projectEntity = modelMapper.map(project, ProjectEntity.class);
		
		projectEntity.getSensors().stream().forEach(x -> {
			x.setSensorType(this.sensorTypeRepository.findById(x.getSensorType().getId()).get());
			x.setProject(projectEntity);
		});
		
		//ProjectEntity projectEntity = new ProjectEntity(project);
		return projectRepository.save(projectEntity).toProjectDto();
	}

	public ProjectDto updateProject(ProjectDto projectDto) {
		return null;
	}

	public ProjectDto deleteProject(String projectId) {
		return null;
	}

	public ProjectDto searchProject(String projectName) {
		return null;
	}

	public ProjectDto findById(Long id) {
		Optional<ProjectEntity> project = projectRepository.findById(id);
		
		modelMapper.map(project.get(), ProjectDto.class);
		
		return modelMapper.map(project.get(), ProjectDto.class);
	}
	
	public List<ProjectDto> findAll() {
		List<ProjectEntity> projects = projectRepository.findAll();
		
		return projects.stream()
				.map(ProjectEntity::toProjectDto)
				.collect(Collectors.toList());
	}

	public List<ProjectDto> findAllByCurrentUser(String username) {
		List<ProjectDto> projectDtoList = new ArrayList<>();
		
		Optional<User> optionalUser = this.userRepository.findByUsername(username);
		
		if(optionalUser.isPresent()) {
			List<ProjectEntity> projects = projectRepository.findByCreatedUser(optionalUser.get());
			
			projectDtoList = projects.stream()
					.map(ProjectEntity::toProjectDto)
					.collect(Collectors.toList());
		}
		
		return projectDtoList;
	}
	
}
