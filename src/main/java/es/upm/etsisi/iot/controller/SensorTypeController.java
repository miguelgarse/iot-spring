package es.upm.etsisi.iot.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping(value = "/{sensorTypeId}")
	public SensorTypeDto searchSensorType(@PathVariable Long sensorTypeId) {
		return sensorTypeService.findById(sensorTypeId);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "/{sensorTypeId}")
	public void deleteSensorTypeById(@PathVariable Long sensorTypeId) {
		sensorTypeService.deleteSensorTypeById(sensorTypeId);
	}
}
