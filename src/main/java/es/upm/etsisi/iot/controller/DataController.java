package es.upm.etsisi.iot.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.upm.etsisi.iot.dto.ApiProjectDto;
import es.upm.etsisi.iot.dto.ApiSensorDto;
import es.upm.etsisi.iot.dto.ApiSensorValueDto;
import es.upm.etsisi.iot.dto.ApiUserDto;
import es.upm.etsisi.iot.security.dto.UserDto;
import es.upm.etsisi.iot.security.entity.User;
import es.upm.etsisi.iot.security.service.UserService;
import es.upm.etsisi.iot.service.ProjectService;
import es.upm.etsisi.iot.service.SensorService;
import es.upm.etsisi.iot.service.SensorValueService;

@RestController
@RequestMapping("/data")
@CrossOrigin(value = "*")
public class DataController {
	
	private UserService userService;
	private ProjectService projectService;
	private SensorService sensorService;
	private SensorValueService sensorValueService;
	
	@Autowired
	public DataController(UserService userService, ProjectService projectService, SensorService sensorService, SensorValueService sensorValueService) {
		this.userService = userService;
		this.projectService = projectService;
		this.sensorService = sensorService;
		this.sensorValueService = sensorValueService;
	}
	
	private boolean checkToken(String tokenApi) {
		boolean tokenExists = Boolean.FALSE;
		Optional<User> optUser = this.userService.findByTokenApi(tokenApi);

		if (optUser.isPresent()) 
			tokenExists = Boolean.TRUE;
		
		return tokenExists;
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<ApiUserDto> findByUsername(@PathVariable String username, @RequestParam("token") String tokenApi) {
		if(!checkToken(tokenApi))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
		ApiUserDto apiUserDto = getApiData(username);
		
		return new ResponseEntity<>(apiUserDto, HttpStatus.FOUND);
	}

	@GetMapping("/{username}/{projectId}")
	public ResponseEntity<ApiUserDto> findByUsernameAndProject(@PathVariable String username, @PathVariable Long projectId, @RequestParam("token") String tokenApi) {
		if(!checkToken(tokenApi))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
		ApiUserDto apiUserDto = getApiData(username);
		
		apiUserDto.setProjects(
				apiUserDto.getProjects()
				.stream()
				.filter(prj -> prj.getId().equals(projectId))
				.collect(Collectors.toList())
		);
		
		return new ResponseEntity<>(apiUserDto, HttpStatus.FOUND);
	}
	
	@GetMapping("/{username}/{projectId}/{sensor}")
	public ResponseEntity<ApiUserDto> findByUsernameAndProjectAndSensor(@PathVariable String username, @PathVariable Long projectId, @PathVariable String sensor, @RequestParam("token") String tokenApi) {
		if(!checkToken(tokenApi))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		
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
		
		return new ResponseEntity<>(apiUserDto, HttpStatus.FOUND);
	}
	
	@GetMapping("/{username}/{projectId}/{sensor}/{timestampIni}/{timestampEnd}")
	public ResponseEntity<ApiUserDto> findByUsernameAndProjectAndSensorAndTimestampIniTimestampEnd(
			@PathVariable String username, @PathVariable Long projectId, @PathVariable String sensor,
			@PathVariable String timestampIni, @PathVariable String timestampEnd,
			@RequestParam("token") String tokenApi) {
		if (!checkToken(tokenApi))
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

		ApiUserDto apiUserDto = null;
		
		try {
			final Date dateIni = new SimpleDateFormat("yyyyMMddhhmm").parse(timestampIni); 	// yyyyMMddhhmm
			final Date dateEnd = new SimpleDateFormat("yyyyMMddhhmm").parse(timestampEnd); 
			
			if(dateIni.after(dateEnd) || !dateIni.before(dateEnd)) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(apiUserDto, HttpStatus.FOUND);
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
