package es.upm.etsisi.iot.dto;

import java.util.Date;

import es.upm.etsisi.iot.security.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SensorCategoryDto {

	private Long id;
	private String category;
	private String description;
	private UserDto createdUser;
	private UserDto lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;

}
