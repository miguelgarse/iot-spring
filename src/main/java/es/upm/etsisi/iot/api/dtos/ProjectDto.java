package es.upm.etsisi.iot.api.dtos;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProjectDto {

	private Long id;
	private String title;
	private String description;
	private String[] keywords;
	private String location;
	private List<SensorDto> sensors;
	private String dashboardIot;
	private String collaborationPlatorm;
	private String[] components;
	@JsonIgnoreProperties(value={ "createdUser"})
	private UserDto createdUser;
	@JsonIgnoreProperties(value={ "createdUser", "profileImage"})
	private UserDto lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;

}
