package es.upm.etsisi.iot.controller;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.upm.etsisi.iot.dto.SensorTypeDto;
import es.upm.etsisi.iot.service.SensorTypeService;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/api/sensorType")
public class SensorTypeController {
	
	private SensorTypeService sensorTypeService;

	@Autowired
	public SensorTypeController(SensorTypeService sensorTypeService) {
		this.sensorTypeService = sensorTypeService;
	}

	@GetMapping
	public List<SensorTypeDto> findAllSensorTypes() {
		return sensorTypeService.findAll();
	}

	@GetMapping(value = "/{sensorTypeId}")
	public SensorTypeDto searchSensorType(
			@NotNull @NotEmpty @PathVariable String sensorTypeId) {
		return sensorTypeService.findById(sensorTypeId);
	}
}
