package es.upm.etsisi.iot.domain.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.etsisi.iot.api.dtos.SensorDto;
import es.upm.etsisi.iot.data.daos.SensorRepository;
import es.upm.etsisi.iot.data.daos.SensorTypeRepository;
import es.upm.etsisi.iot.data.model.SensorEntity;
import es.upm.etsisi.iot.data.model.SensorTypeEntity;

@Service
@Transactional
public class SensorService {
	
	private SensorRepository sensorRepository;
	
	private SensorTypeRepository sensorTypeRepository;
	
	@Autowired
	public SensorService(SensorRepository sensorRepository, SensorTypeRepository sensorTypeRepository) {
		this.sensorRepository = sensorRepository;
		this.sensorTypeRepository = sensorTypeRepository;
	}

	public SensorDto createSensor(SensorDto sensorDto) {
		Optional<SensorTypeEntity> sensorTypeEntity = sensorTypeRepository.findById(sensorDto.getSensorType().getId());
		SensorEntity sensor = new SensorEntity();
		sensor.setName(sensorDto.getName());
		sensor.setSensorType(sensorTypeEntity.get());
		return sensorRepository.save(sensor).toSensorDto();
	}

	public List<SensorDto> findAll() {
		return sensorRepository.findAll()
				.stream()
				.map(SensorEntity::toSensorDto)
				.collect(Collectors.toList());
	}

	public SensorDto updateSensor(SensorDto sensor, String identificador) {
		SensorEntity sensorToUpdate = sensorRepository.findByName(identificador);

		if (!sensorToUpdate.getName().equals(sensor.getName())) {
			sensorToUpdate.setName(sensor.getName());
		}
		if (!sensorToUpdate.getSensorType().getId().equals(sensor.getSensorType().getId())) {
			sensorToUpdate.setSensorType(sensorTypeRepository.findById(sensor.getSensorType().getId()).get());
		}
		
		return sensorRepository.save(sensorToUpdate).toSensorDto();
	}

	public SensorDto findBySensorId(String sensorId) {
		Optional<SensorEntity> sensor = sensorRepository.findById(Long.parseLong(sensorId));
		
		return sensor.isPresent() ? sensor.get().toSensorDto() : null;
	}

	public void deleteSensor(String sensorId) {
		SensorEntity sensorFind = sensorRepository.findByName(sensorId);
		sensorRepository.deleteById(sensorFind.getId());
	}
	
	public List<SensorDto> findAllSensorByProjectId(Long projectId) {
		return sensorRepository.findByProjectId(projectId)
				.stream()
				.map(SensorEntity::toSensorDto)
				.collect(Collectors.toList());
	}
}
