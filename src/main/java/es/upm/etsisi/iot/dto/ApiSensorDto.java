package es.upm.etsisi.iot.dto;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiSensorDto {

	private String name;
	private Long sensorType;
	private List<ApiSensorValueDto> sensorValues;
	private Date dateCreated;
	private Date dateLastModified;
	
}
