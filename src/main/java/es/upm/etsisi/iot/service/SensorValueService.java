package es.upm.etsisi.iot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.etsisi.iot.dto.SensorDto;
import es.upm.etsisi.iot.modelo.ProjectEntity;
import es.upm.etsisi.iot.modelo.SensorEntity;
import es.upm.etsisi.iot.modelo.dao.ProjectRepository;
import es.upm.etsisi.iot.modelo.dao.SensorRepository;
import es.upm.etsisi.iot.modelo.dao.SensorValueRepository;

@Service
public class SensorValueService {
	
	private SensorRepository sensorRepository;
	private SensorValueRepository sensorValueRepository;
	private ProjectRepository projectRepository;
	
	@Autowired
	public SensorValueService(SensorRepository sensorRepository, SensorValueRepository sensorValueRepository, ProjectRepository projectRepository) {
		this.sensorRepository = sensorRepository;
		this.sensorValueRepository = sensorValueRepository;
		this.projectRepository = projectRepository;
	}

	public List<SensorDto> findAllSensorValueBySensorId(Long sensorId) {
		// Empty service
		return null;
	}
	
	public List<SensorDto> findAllSensorValueByProjectId(Long projectId) {
		List<SensorEntity> sensors = new ArrayList<>();
		Optional<ProjectEntity> project = this.projectRepository.findById(projectId);
		
		if(project.isPresent()) {
			project.get().getSensors().stream().forEach(sensor -> {
				sensor.getSensorValues();
			});
		}
		
		return sensors.stream().map(SensorEntity::toSensorDto).collect(Collectors.toList());
	}
	

}
