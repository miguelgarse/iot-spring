package es.upm.etsisi.iot.api.resources;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.upm.etsisi.iot.api.dtos.ApiProjectDto;
import es.upm.etsisi.iot.api.dtos.ApiSensorDto;
import es.upm.etsisi.iot.api.dtos.ApiSensorValueDto;
import es.upm.etsisi.iot.api.dtos.ApiUserDto;
import es.upm.etsisi.iot.api.dtos.UserDto;
import es.upm.etsisi.iot.data.model.UserEntity;
import es.upm.etsisi.iot.domain.exceptions.BadRequestException;
import es.upm.etsisi.iot.domain.services.ProjectService;
import es.upm.etsisi.iot.domain.services.SensorService;
import es.upm.etsisi.iot.domain.services.SensorValueService;
import es.upm.etsisi.iot.domain.services.UserService;

@RestController
@RequestMapping("/data")
@CrossOrigin(value = "*")
public class DataResource {
	
	private UserService userService;
	private ProjectService projectService;
	private SensorService sensorService;
	private SensorValueService sensorValueService;
	
	private static final String NOT_VALID_TOKEN_MSG = "Token is not valid";
	
	@Autowired
	public DataResource(UserService userService, ProjectService projectService, SensorService sensorService, SensorValueService sensorValueService) {
		this.userService = userService;
		this.projectService = projectService;
		this.sensorService = sensorService;
		this.sensorValueService = sensorValueService;
	}
	
	private boolean checkToken(String tokenApi) {
		boolean tokenExists = Boolean.FALSE;
		Optional<UserEntity> optUser = this.userService.findByTokenApi(tokenApi);

		if (optUser.isPresent()) 
			tokenExists = Boolean.TRUE;
		
		return tokenExists;
	}
	
	@GetMapping("/{username}")
	public ApiUserDto findByUsername(@PathVariable String username, @RequestParam("token") String tokenApi) {
		if(!checkToken(tokenApi))
			throw new AccessDeniedException(NOT_VALID_TOKEN_MSG);
		
		return getApiData(username);
	}

	@GetMapping("/{username}/{projectId}")
	public ApiUserDto findByUsernameAndProject(@PathVariable String username, @PathVariable Long projectId, @RequestParam("token") String tokenApi) {
		if(!checkToken(tokenApi))
			throw new AccessDeniedException(NOT_VALID_TOKEN_MSG);
		
		ApiUserDto apiUserDto = getApiData(username);
		
		apiUserDto.setProjects(
				apiUserDto.getProjects()
				.stream()
				.filter(prj -> prj.getId().equals(projectId))
				.collect(Collectors.toList())
		);
		
		return apiUserDto;
	}
	
	@GetMapping("/{username}/{projectId}/{sensor}")
	public ApiUserDto findByUsernameAndProjectAndSensor(@PathVariable String username, @PathVariable Long projectId, @PathVariable String sensor, @RequestParam("token") String tokenApi) {
		if(!checkToken(tokenApi))
			throw new AccessDeniedException(NOT_VALID_TOKEN_MSG);
		
		ApiUserDto apiUserDto = getApiData(username);
		
		apiUserDto.setProjects(
				apiUserDto.getProjects()
				.stream()
				.filter(prj -> prj.getId().equals(projectId))
				.collect(Collectors.toList())
		);
		
		apiUserDto.getProjects()
		.stream()
		.forEach(prj -> {
			prj.setSensors(
				prj.getSensors()
				.stream()
				.filter(sens -> sens.getName().equals(sensor))
				.collect(Collectors.toList())
			);
		});
		
		return apiUserDto;
	}
	
	@GetMapping("/{username}/{projectId}/{sensor}/{timestampIni}/{timestampEnd}")
	public ApiUserDto findByUsernameAndProjectAndSensorAndTimestampIniTimestampEnd(
			@PathVariable String username, @PathVariable Long projectId, @PathVariable String sensor,
			@PathVariable String timestampIni, @PathVariable String timestampEnd,
			@RequestParam("token") String tokenApi) {
		if (!checkToken(tokenApi))
			throw new AccessDeniedException(NOT_VALID_TOKEN_MSG);

		ApiUserDto apiUserDto = null;
		
		try {
			final Date dateIni = new SimpleDateFormat("yyyyMMddhhmm").parse(timestampIni); 	// yyyyMMddhhmm
			final Date dateEnd = new SimpleDateFormat("yyyyMMddhhmm").parse(timestampEnd); 
			
			if(dateIni.after(dateEnd) || !dateIni.before(dateEnd)) {
				throw new BadRequestException("Request params are wrong!");
			}
			
			apiUserDto = getApiData(username);
	
			// Filtramos por titulo de proyecto
			apiUserDto.setProjects(apiUserDto.getProjects().stream()
					.filter(prj -> prj.getId().equals(projectId))
					.collect(Collectors.toList())
			);
			
			// Filtramos por nombre de sensor
			apiUserDto.getProjects()
			.stream()
			.forEach(prj -> {
				prj.setSensors(
					prj.getSensors()
					.stream()
					.filter(sens -> sens.getName().equals(sensor))
					.collect(Collectors.toList())
				);
			});
			
			// Filtramos por timestamp
			apiUserDto.getProjects().stream().forEach(prj -> {
				prj.getSensors().stream()
						.forEach(sens -> sens.setSensorValues(sens.getSensorValues().stream().filter(
								sensVal -> sensVal.getTimestamp().after(dateIni) && sensVal.getTimestamp().before(dateEnd))
								.collect(Collectors.toList())));
			});
			
		} catch (Exception e) {
			throw new BadRequestException("Request params are wrong!");
		}
		return apiUserDto;
	}
	
	private ApiUserDto getApiData(String username) {
		UserDto userDto = userService.findByUsername(username).get().toUserDto();
		List<ApiProjectDto> apiProjects = new ArrayList<>();
		this.projectService.findAllByCreatedUser(userDto.getUsername()).stream().forEach(projectDto -> {
			ApiProjectDto apiProjectDto = new ApiProjectDto();
			BeanUtils.copyProperties(projectDto, apiProjectDto);
			
			List<ApiSensorDto> apiSensorDtos = new ArrayList<>();
			this.sensorService.findAllSensorByProjectId(projectDto.getId()).stream().forEach(sensorDto -> {
				ApiSensorDto apiSensorDto = new ApiSensorDto();
				BeanUtils.copyProperties(sensorDto, apiSensorDto);
				apiSensorDtos.add(apiSensorDto);
				
				List<ApiSensorValueDto> apiSensorValueDtos = new ArrayList<>();
				this.sensorValueService.findAllSensorValueBySensorId(sensorDto.getId()).stream().forEach(sensorValueDto -> {
					ApiSensorValueDto apiSensorValueDto = new ApiSensorValueDto();
					BeanUtils.copyProperties(sensorValueDto, apiSensorValueDto);
					apiSensorValueDtos.add(apiSensorValueDto);
				});
				
				apiSensorDto.setSensorValues(apiSensorValueDtos);
			});
			
			apiProjectDto.setSensors(apiSensorDtos);
			
			apiProjects.add(apiProjectDto);
		});
		
		ApiUserDto apiUserDto = new ApiUserDto();
		BeanUtils.copyProperties(userDto, apiUserDto);
		
		apiUserDto.setProjects(apiProjects);
		return apiUserDto;
	}

}
