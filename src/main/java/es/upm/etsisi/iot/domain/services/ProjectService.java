package es.upm.etsisi.iot.domain.services;

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

import javax.persistence.EntityNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import es.upm.etsisi.iot.api.dtos.ProjectDto;
import es.upm.etsisi.iot.api.dtos.SensorDto;
import es.upm.etsisi.iot.api.dtos.SensorValueDto;
import es.upm.etsisi.iot.data.daos.ProjectRepository;
import es.upm.etsisi.iot.data.daos.SensorTypeRepository;
import es.upm.etsisi.iot.data.daos.UserRepository;
import es.upm.etsisi.iot.data.model.ProjectEntity;
import es.upm.etsisi.iot.data.model.UserEntity;
import es.upm.etsisi.iot.utils.Utilities;

@Service
@Transactional
public class ProjectService {

	@Autowired
	private Utilities utilities;
	
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

	public List<SensorDto> processCsvData(ProjectDto project, MultipartFile file) throws Exception {
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
			Optional<UserEntity> optionalUser = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername());
			int numValues = csvLines.size();
			int numSensors = csvLines.get(0).split(";").length;
			String[][] csvMatrix = new String [numValues][numSensors];
			
			for(int i = 0; i < numValues; i++) {
				csvMatrix[i] = csvLines.get(i).split(";");
			}
			
			for(int col = 1; col < numSensors; col++) {
				String sensorName = csvMatrix[0][col];
				
				SensorDto sensorDto = project.getSensors().stream()
						.filter(x -> x.getName().equals(sensorName))
						.findAny()
		                .orElse(new SensorDto());
				
				sensorDto.setName(sensorName);
				sensorDto.setDateLastModified(sysDate);
				sensorDto.setLastModifieduser(optionalUser.get().toUserDto());
				
				List<SensorValueDto> sensorValueDtos = new ArrayList<>();
				for (int row = 1; row < numValues; row++) {
					String value = csvMatrix[row][col];
					String timestamp = csvMatrix[row][0];
					
					SensorValueDto sensorValue = new SensorValueDto();
					sensorValue.setValue(new BigDecimal(value));  
					sensorValue.setTimestamp(new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(timestamp.trim()));
					sensorValue.setDateCreated(sysDate);
					sensorValue.setDateLastModified(sysDate);
					sensorValue.setCreatedUser(optionalUser.get().toUserDto());
					sensorValue.setLastModifieduser(optionalUser.get().toUserDto());
					sensorValueDtos.add(sensorValue);
				}
				
				sensorDto.setSensorValues(sensorValueDtos);
				sensors.add(sensorDto);
			}
		} else {
			throw new Exception("El fichero CSV no contiene valores");
		}
		
		return sensors;
	}
	
	public ProjectDto createProject(ProjectDto project) throws Exception {
		Date currentDate = new Date();
		
		Optional<UserEntity> optionalUser = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername());
		
		if(optionalUser.isPresent()) {
			project.setCreatedUser(optionalUser.get().toUserDto());
			project.setLastModifieduser(optionalUser.get().toUserDto());
			
			if(project.getSensors() != null && !project.getSensors().isEmpty()) {
				project.getSensors().stream().forEach(x -> {
					x.setCreatedUser(optionalUser.get().toUserDto());
					x.setLastModifieduser(optionalUser.get().toUserDto());
					x.setDateCreated(currentDate);
					x.setDateLastModified(currentDate);
				});
			}
		}
		
		project.setDateLastModified(currentDate);
		project.setDateCreated(currentDate);
		
		ProjectEntity projectEntity = modelMapper.map(project, ProjectEntity.class);
		
		if(project.getSensors() != null && !project.getSensors().isEmpty()) {
			projectEntity.getSensors().stream().forEach(x -> {
				x.setSensorType(this.sensorTypeRepository.findById(x.getSensorType().getId()).get());
				x.setProject(projectEntity);
			});
		}
		
		projectEntity.setIsActive(Boolean.TRUE);
		
		return projectRepository.save(projectEntity).toProjectDto();
	}

	public ProjectDto updateProject(ProjectDto project, MultipartFile file) throws Exception {
		Date currentDate = new Date();
		
		if(file != null) {
			List<SensorDto> sensors = processCsvData(project, file);
			project.setSensors(sensors);
		}
		
		Optional<UserEntity> optionalUser = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername());
		
		if(optionalUser.isPresent()) {
			project.setLastModifieduser(optionalUser.get().toUserDto());
			
			project.getSensors().stream().forEach(sensor -> {
				if(sensor.getCreatedUser() == null) {
					sensor.setCreatedUser(optionalUser.get().toUserDto());
					sensor.setDateCreated(currentDate);
				}
				
				sensor.setLastModifieduser(optionalUser.get().toUserDto());
				sensor.setDateLastModified(currentDate);
			});
		}
		
		project.setDateLastModified(currentDate);
		
		ProjectEntity projectEntity = modelMapper.map(project, ProjectEntity.class);
		
		projectEntity.getSensors().stream().forEach(sensor -> {
			sensor.setSensorType(this.sensorTypeRepository.findById(sensor.getSensorType().getId()).get());
			sensor.setProject(projectEntity);
			if(sensor.getSensorValues() != null && !sensor.getSensorValues().isEmpty()) {
				sensor.getSensorValues().stream().forEach(sernsorValue -> {
					sernsorValue.setSensor(sensor);
				});
			}
		});
		
		projectEntity.setIsActive(Boolean.TRUE);
		
		return projectRepository.save(projectEntity).toProjectDto();
	}

	public List<ProjectDto> searchProject(ProjectDto projectDto) {
		return this.projectRepository.findByTitleLike(projectDto.getTitle())
				.stream()
				.map(ProjectEntity::toProjectDto)
				.collect(Collectors.toList());
	}

	public ProjectDto findById(Long id) {
		Optional<ProjectEntity> project = projectRepository.findById(id);
		
		ProjectEntity projectEntity = null;
		if(project.isPresent()) {
			projectEntity = project.get();
		} else {
			projectEntity = new ProjectEntity();
		}
		
		return projectEntity.toProjectDto();
	}
	
	public List<ProjectDto> findAll() {
		List<ProjectEntity> projects = projectRepository.findByIsActiveTrueOrderByDateLastModifiedDesc();
		
		return projects.stream()
				.map(ProjectEntity::toProjectDto)
				.collect(Collectors.toList());
	}

	public List<ProjectDto> findAllByCreatedUser(String username) {
		List<ProjectDto> projectDtoList = new ArrayList<>();
		
		Optional<UserEntity> optionalUser = this.userRepository.findByUsernameAndIsActiveTrue(username);
		
		if(optionalUser.isPresent()) {
			List<ProjectEntity> projects = projectRepository.findByCreatedUserAndIsActiveTrueOrderByDateLastModifiedDesc(optionalUser.get());
			
			projectDtoList = projects.stream()
					.map(ProjectEntity::toProjectDto)
					.collect(Collectors.toList());
		}
		
		return projectDtoList;
	}
	
	public void deleteById(Long projectId) {
		Optional<ProjectEntity> projectOptional = projectRepository.findById(projectId);
		
		if(projectOptional.isPresent()) {
			ProjectEntity projectEntity = projectOptional.get();
			projectEntity.setIsActive(Boolean.FALSE);
			projectEntity.setDateLastModified(new Date());
			UserEntity currentUser = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername()).get();
			projectEntity.setLastModifieduser(currentUser);
			
			this.projectRepository.save(projectEntity);
		} else {
			throw new EntityNotFoundException("El Proyecto solicitado no se encuentra almacenado");
		}
	}
	
}
