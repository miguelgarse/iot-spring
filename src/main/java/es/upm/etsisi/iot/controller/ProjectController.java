package es.upm.etsisi.iot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.upm.etsisi.iot.dto.ProjectDto;
import es.upm.etsisi.iot.service.ProjectService;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/api/project")
public class ProjectController {

	private ProjectService projectService;
	
	@Autowired
	public ProjectController(ProjectService projectService) {
		 this.projectService = projectService;
	}
	
	@PostMapping
	public ProjectDto createProject(@RequestPart("project") ProjectDto project) throws Exception {
		return projectService.createProject(project);
	}
	
	
	@PutMapping
	public ProjectDto updateProject(@RequestPart("file") MultipartFile file, @RequestPart ProjectDto project) throws Exception {
		return projectService.updateProject(project, file);
	}

	@GetMapping
	public ResponseEntity<List<ProjectDto>> findAll() {
		return	ResponseEntity.ok(projectService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProjectDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok(projectService.findById(id));
	}

	@GetMapping("/byCurrentUser")
	public ResponseEntity<List<ProjectDto>> findAllByCurrentUser(Authentication authentication) {
		String username = authentication.getName();
		return ResponseEntity.ok(projectService.findAllByCurrentUser(username));
	}

}
