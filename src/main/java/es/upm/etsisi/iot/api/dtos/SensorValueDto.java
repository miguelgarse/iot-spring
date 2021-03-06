package es.upm.etsisi.iot.api.dtos;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SensorValueDto {

	private Long id;
	private SensorDto sensor;
	private BigDecimal value;
	private Date timestamp;
	@JsonIgnoreProperties(value={ "createdUser"})
	private UserDto createdUser;
	@JsonIgnoreProperties(value={ "createdUser"})
	private UserDto lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;

}
