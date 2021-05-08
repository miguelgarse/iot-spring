package es.upm.etsisi.iot.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.etsisi.iot.dto.ProjectDto;
import es.upm.etsisi.iot.modelo.ProjectEntity;
import es.upm.etsisi.iot.modelo.dao.ProjectRepository;
import es.upm.etsisi.iot.security.entity.User;
import es.upm.etsisi.iot.security.repository.UserRepository;
import es.upm.etsisi.iot.utils.Utilities;

@Service
public class ProjectService {

	private ProjectRepository projectRepository;
	private UserRepository userRepository;

	@Autowired
	public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
		this.projectRepository = projectRepository;
		this.userRepository = userRepository; 
	}

	public ProjectDto newProject(ProjectDto project) {
		
		Optional<User> optionalUser = this.userRepository.findByUsername(Utilities.getCurrentUser().getUsername());
		
		if(optionalUser.isPresent()) {
			project.setCreatedUser(optionalUser.get());
			project.setLastModifieduser(optionalUser.get());
		}
		
		project.setDateLastModified(new Date());
		project.setDateCreated(new Date());
		
		ProjectEntity projectEntity = new ProjectEntity(project);
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
		
		return project.map(ProjectEntity::toProjectDto)
				.orElse(new ProjectDto());
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
