package es.upm.etsisi.iot.api.resources;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.upm.etsisi.iot.api.dtos.SensorDto;
import es.upm.etsisi.iot.domain.services.SensorService;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/api/sensor")
public class SensorResource {

	private SensorService sensorService;
	
	@Autowired
	public SensorResource(SensorService sensorService) {
		this.sensorService = sensorService;
	}

	@PostMapping()
	public SensorDto createSensor(@NotNull @NotEmpty @RequestBody SensorDto sensor) {
		return this.sensorService.createSensor(sensor);
	}

	@PutMapping(value = "/{sensorId}")
	public SensorDto updateSensor(@NotNull @NotEmpty @RequestBody SensorDto sensor, String sensorId) {
		return this.sensorService.updateSensor(sensor, sensorId);
	}

	@DeleteMapping(value = "/{sensorId}")
	public void deleteSensor(@PathVariable String sensorId) {
		this.sensorService.deleteSensor(sensorId);
	}

	@GetMapping()
	public List<SensorDto> findAllSensors() {
		return this.sensorService.findAll();
	}

	@GetMapping(value = "/{sensorId}")
	public SensorDto findSensorBySensorId(@PathVariable String sensorId) {
		return this.sensorService.findBySensorId(sensorId);
	}

	@GetMapping(value = "/{projectId}")
	public SensorDto findSensorByProjectId(@PathVariable String projectId) {
		return this.sensorService.findBySensorId(projectId);
	}
	
}
