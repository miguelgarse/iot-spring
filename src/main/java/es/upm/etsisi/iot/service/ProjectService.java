package es.upm.etsisi.iot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.etsisi.iot.dto.ProjectDto;
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

	public ProjectDto newProject(ProjectDto project) {
		Date currentDate = new Date();
		
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
