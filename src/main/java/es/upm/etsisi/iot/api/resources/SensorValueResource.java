package es.upm.etsisi.iot.api.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.upm.etsisi.iot.api.dtos.SensorDto;
import es.upm.etsisi.iot.api.dtos.SensorValueDto;
import es.upm.etsisi.iot.domain.services.SensorValueService;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/api/sensorValue")
public class SensorValueResource {
	
	private SensorValueService sensorValueService;
	
	@Autowired
	public SensorValueResource(SensorValueService sensorValueService) {
		this.sensorValueService = sensorValueService;
	}
	
	@GetMapping(value = "/{sensorId}")
	public List<SensorValueDto> findAllSensorValueBySensorId(@PathVariable Long sensorId) {
		return sensorValueService.findAllSensorValueBySensorId(sensorId);
	}
	
	@GetMapping(value = "/findAllByProjectId/{projectId}")
	public List<SensorDto> findAllSensorValueByProjectId(@PathVariable Long projectId) {
		return sensorValueService.findAllSensorValueByProjectId(projectId);
	}

}
