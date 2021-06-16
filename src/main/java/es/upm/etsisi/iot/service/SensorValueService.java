package es.upm.etsisi.iot.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.etsisi.iot.dto.SensorDto;
import es.upm.etsisi.iot.dto.SensorValueDto;
import es.upm.etsisi.iot.modelo.ProjectEntity;
import es.upm.etsisi.iot.modelo.SensorEntity;
import es.upm.etsisi.iot.modelo.SensorValueEntity;
import es.upm.etsisi.iot.modelo.dao.ProjectRepository;
import es.upm.etsisi.iot.modelo.dao.SensorValueRepository;

@Service
@Transactional
public class SensorValueService {
	
	private SensorValueRepository sensorValueRepository;
	private ProjectRepository projectRepository;
	
	@Autowired
	public SensorValueService(SensorValueRepository sensorValueRepository, ProjectRepository projectRepository) {
		this.sensorValueRepository = sensorValueRepository;
		this.projectRepository = projectRepository;
	}

	public List<SensorValueDto> findAllSensorValueBySensorId(Long sensorId) {
		return this.sensorValueRepository.findBySensorId(sensorId).stream()
				.map(SensorValueEntity::toSensorValueDto)
				.collect(Collectors.toList());
	}
	
	public List<SensorDto> findAllSensorValueByProjectId(Long projectId) {
		List<SensorEntity> sensors = new ArrayList<>();
		Optional<ProjectEntity> project = this.projectRepository.findById(projectId);
		
		if(project.isPresent()) {
			project.get().getSensors().stream().forEach(sensor -> {
				sensor.getSensorValues();
				sensors.add(sensor);
			});
		}
		
		return sensors.stream().map(SensorEntity::toSensorDto).collect(Collectors.toList());
	}
	

}
