package es.upm.etsisi.iot.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public ProjectDto newProject(@RequestBody ProjectDto project) {
		return projectService.newProject(project);
	}
	
	
	@PutMapping
	public ProjectDto updateProject(@NotNull @NotEmpty @RequestBody ProjectDto project) {
		return projectService.updateProject(project);
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
	
	@PostMapping(value = "/add-data")
	public ResponseEntity addData(@RequestBody MultipartFile file) {

		InputStream is = null;
		BufferedReader bfReader = null;
		try {
			is = new ByteArrayInputStream(file.getBytes());
			bfReader = new BufferedReader(new InputStreamReader(is));
			String temp = null;
			while ((temp = bfReader.readLine()) != null) {
				System.out.println(temp);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception ex) {

			}
		}
		
		return ResponseEntity.ok(null);
	}
}
