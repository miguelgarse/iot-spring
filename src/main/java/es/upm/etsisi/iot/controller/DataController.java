package es.upm.etsisi.iot.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import es.upm.etsisi.iot.dto.ApiUserDto;
import es.upm.etsisi.iot.dto.ApiSensorValueDto;
import es.upm.etsisi.iot.dto.SensorDto;
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
		
		return new ResponseEntity<>(apiUserDto, HttpStatus.FOUND);
	}
	
	@GetMapping("/{username}/{project}")
	public ApiUserDto findByUsernameAndProject(@PathVariable String username, @PathVariable String project, @RequestParam("token") String tokenApi) {
		if(!checkToken(tokenApi))
			return null;
		
		
		
		return null;
	}

}
