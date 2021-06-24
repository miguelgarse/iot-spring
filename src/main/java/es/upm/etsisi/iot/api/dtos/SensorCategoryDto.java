package es.upm.etsisi.iot.api.dtos;

import java.util.Date;

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
