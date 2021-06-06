package es.upm.etsisi.iot.dto;

import java.util.Date;
import java.util.List;

import es.upm.etsisi.iot.security.dto.UserDto;
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
	private UserDto createdUser;
	private UserDto lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;

}
