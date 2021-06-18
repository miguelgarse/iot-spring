package es.upm.etsisi.iot.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiProjectDto {

	private Long id;
	private String title;
	private String description;
	private String[] keywords;
	private String location;
	private List<ApiSensorDto> sensors;
	private String dashboardIot;
	private String collaborationPlatorm;
	private String[] components;
	
}
