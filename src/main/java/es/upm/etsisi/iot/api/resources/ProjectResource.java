package es.upm.etsisi.iot.api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import es.upm.etsisi.iot.api.dtos.ProjectDto;
import es.upm.etsisi.iot.domain.services.ProjectService;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/api/project")
public class ProjectResource {

	private ProjectService projectService;
	
	@Autowired
	public ProjectResource(ProjectService projectService) {
		 this.projectService = projectService;
	}
	
	@PostMapping
	public ProjectDto createProject(@RequestPart(name = "file", required = false) MultipartFile file, @RequestPart("project") ProjectDto project) throws Exception {
		return projectService.createProject(project);
	}
	
	@PutMapping
	public ProjectDto updateProject(@RequestPart(name = "file", required = false) MultipartFile file, @RequestPart ProjectDto project) throws Exception {
		return projectService.updateProject(project, file);
	}

	@GetMapping
	public List<ProjectDto> findAll() {
		return	projectService.findAll();
	}
	
	@GetMapping("/{id}")
	public ProjectDto findById(@PathVariable Long id) {
		return projectService.findById(id);
	}

	@GetMapping("/byCurrentUser")
	public List<ProjectDto> findAllByCurrentUser(Authentication authentication) {
		String username = authentication.getName();
		return projectService.findAllByCreatedUser(username);
	}
	
	@PostMapping("/search")
	public List<ProjectDto> searchProject(@RequestBody ProjectDto projectSearch) {
		return projectService.searchProject(projectSearch);
	}

	@DeleteMapping("/{projectId}")
	public void deleteProject(@PathVariable Long projectId) {
		projectService.deleteById(projectId);
	}
}
