package es.upm.etsisi.iot.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiSensorValueDto {

	private BigDecimal value;
	private Date timestamp;
	private Date dateCreated;
	
}
