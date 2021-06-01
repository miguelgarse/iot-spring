package es.upm.etsisi.iot.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.etsisi.iot.dto.SensorTypeDto;
import es.upm.etsisi.iot.modelo.SensorTypeEntity;
import es.upm.etsisi.iot.modelo.dao.SensorTypeRepository;
import es.upm.etsisi.iot.security.entity.User;
import es.upm.etsisi.iot.security.repository.UserRepository;
import es.upm.etsisi.iot.utils.Utilities;

@Service
public class SensorTypeService {

	@Autowired
	private Utilities utilities;
	
	private SensorTypeRepository sensorTypeRepository;
	private UserRepository userRepository;
	
	@Autowired
	public SensorTypeService(SensorTypeRepository sensorTypeRepository, UserRepository userRepository) {
		this.sensorTypeRepository = sensorTypeRepository;
		this.userRepository = userRepository;
	}

	public List<SensorTypeDto> findAll() {
		return sensorTypeRepository.findByIsActiveTrueOrderByDateLastModifiedDesc()
				.stream()
				.map(SensorTypeEntity::toSensorTypeDto)
				.collect(Collectors.toList());
	}

	public SensorTypeDto findById(Long sensorTypeId) {
		Optional<SensorTypeEntity> tipoSensor = sensorTypeRepository.findById(sensorTypeId);
		
		return tipoSensor.isPresent() ? tipoSensor.get().toSensorTypeDto() : null;
	}
	
	public void deleteSensorTypeById(Long sensorTypeId) {
		Optional<SensorTypeEntity> sensorType = sensorTypeRepository.findById(sensorTypeId);
		
		if(sensorType.isPresent()) {
			SensorTypeEntity sensorTypeEntity = sensorType.get();
			sensorTypeEntity.setIsActive(Boolean.FALSE);
			sensorTypeEntity.setDateLastModified(new Date());
			User currentUser = this.userRepository.findByUsername(utilities.getCurrentUser().getUsername()).get();
			sensorTypeEntity.setLastModifieduser(currentUser);
			
			this.sensorTypeRepository.save(sensorTypeEntity);
		} else {
			throw new EntityNotFoundException("El tipo de sensor solicitado no se encuentra almacenado");
		}
	}

}
