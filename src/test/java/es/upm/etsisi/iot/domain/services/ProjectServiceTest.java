package es.upm.etsisi.iot.domain.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import es.upm.etsisi.iot.TestConfig;
import es.upm.etsisi.iot.api.dtos.ProjectDto;
import es.upm.etsisi.iot.data.model.ProjectEntity;

@TestConfig
class ProjectServiceTest {

	@Autowired
	private ProjectService projectService;
	
	@Test
	void testCreateProject() throws Exception{
		ProjectEntity project = ProjectEntity.builder()
				.title("Project Title")
				.description("Project Description")
				.keywords(new String[] {"Keyword"})
				.location("Location")
				.dashboardIot("Dashboard")
				.collaborationPlatorm("Collaboration Platform")
				.components(new String[] {})
				.sensors(new ArrayList())
				.createdUser(null)
				.lastModifieduser(null)
				.dateCreated(new Date())
				.dateLastModified(new Date())
				.build();
		
		ProjectDto projectDto = this.projectService.createProject(project.toProjectDto());
		assertNotNull(projectDto);
		assertNotNull(projectDto.getId());
	}

}
