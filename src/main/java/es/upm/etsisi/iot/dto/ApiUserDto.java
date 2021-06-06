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
public class ApiUserDto {

	private String username;
	private List<ApiProjectDto> projects;
	
}
