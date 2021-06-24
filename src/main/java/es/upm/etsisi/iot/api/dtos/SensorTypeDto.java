package es.upm.etsisi.iot.api.dtos;

import java.util.Date;

import es.upm.etsisi.iot.data.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SensorTypeDto {

	private Long id;
	private String code;
	private SensorCategoryDto category;
	private String description;
	private String manufacturer;
	private String url;
	private UserEntity createdUser;
	private UserEntity lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;
	
}
