package es.upm.etsisi.iot.domain.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.etsisi.iot.api.dtos.SensorDto;
import es.upm.etsisi.iot.api.dtos.SensorValueDto;
import es.upm.etsisi.iot.data.daos.ProjectRepository;
import es.upm.etsisi.iot.data.daos.SensorValueRepository;
import es.upm.etsisi.iot.data.model.ProjectEntity;
import es.upm.etsisi.iot.data.model.SensorEntity;
import es.upm.etsisi.iot.data.model.SensorValueEntity;

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
