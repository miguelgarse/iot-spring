package es.upm.etsisi.iot.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.upm.etsisi.iot.security.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SensorDto {

	private Long id;
	private String name;
	private SensorTypeDto sensorType;
	private List<SensorValueDto> sensorValues;
	@JsonIgnoreProperties(value={ "createdUser"})
	private UserDto createdUser;
	@JsonIgnoreProperties(value={ "createdUser"})
	private UserDto lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;

}
