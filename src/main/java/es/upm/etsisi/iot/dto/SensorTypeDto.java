package es.upm.etsisi.iot.dto;

import java.util.Date;

import es.upm.etsisi.iot.security.entity.User;
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
	private String type;
	private String description;
	private String manufacturer;
	private String url;
	private User createdUser;
	private User lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;
	
}
