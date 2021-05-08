package es.upm.etsisi.iot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.upm.etsisi.iot.dto.SensorDto;
import es.upm.etsisi.iot.service.SensorValueService;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/api/sensorValue")
public class SensorValueController {
	
	private SensorValueService sensorValueService;
	
	@Autowired
	public SensorValueController(SensorValueService sensorValueService) {
		this.sensorValueService = sensorValueService;
	}
	
	@GetMapping(value = "/{sensorId}")
	public List<SensorDto> findAllSensorValueBySensorId(@PathVariable String sensorId) {
		return sensorValueService.findAllSensorValueBySensorId(sensorId);
	}

}
