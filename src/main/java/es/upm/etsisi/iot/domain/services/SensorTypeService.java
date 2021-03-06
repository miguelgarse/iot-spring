package es.upm.etsisi.iot.domain.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.etsisi.iot.api.dtos.SensorCategoryDto;
import es.upm.etsisi.iot.api.dtos.SensorTypeDto;
import es.upm.etsisi.iot.data.daos.SensorCategoryRepository;
import es.upm.etsisi.iot.data.daos.SensorTypeRepository;
import es.upm.etsisi.iot.data.daos.UserRepository;
import es.upm.etsisi.iot.data.model.SensorCategoryEntity;
import es.upm.etsisi.iot.data.model.SensorTypeEntity;
import es.upm.etsisi.iot.data.model.UserEntity;
import es.upm.etsisi.iot.utils.Utilities;

@Service
@Transactional
public class SensorTypeService {

	@Autowired
	private Utilities utilities;
	
	private SensorTypeRepository sensorTypeRepository;
	private SensorCategoryRepository sensorCategoryRepository;
	private UserRepository userRepository;
	
	@Autowired
	public SensorTypeService(SensorTypeRepository sensorTypeRepository, UserRepository userRepository, SensorCategoryRepository sensorCategoryRepository) {
		this.sensorTypeRepository = sensorTypeRepository;
		this.sensorCategoryRepository = sensorCategoryRepository;
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
			UserEntity currentUser = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername()).get();
			sensorTypeEntity.setLastModifieduser(currentUser);
			
			this.sensorTypeRepository.save(sensorTypeEntity);
		} else {
			throw new EntityNotFoundException("El tipo de sensor solicitado no se encuentra almacenado");
		}
	}
	
	public List<SensorCategoryDto> findAllSensorCategories() {
		return sensorCategoryRepository.findByIsActiveTrueOrderByCategoryAsc()
				.stream()
				.map(SensorCategoryEntity::toSensorCategoryDto)
				.collect(Collectors.toList());
	}
	
	public SensorTypeDto createSensorType(SensorTypeDto sensorType) {
		SensorTypeEntity sensorTypeEntity = new SensorTypeEntity();
		BeanUtils.copyProperties(sensorType, sensorTypeEntity);
		
		sensorTypeEntity.setDateCreated(new Date());
		sensorTypeEntity.setDateLastModified(new Date());
		
		Optional<UserEntity> userOpt = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername());
		if(userOpt.isPresent()) {
			sensorTypeEntity.setLastModifieduser(userOpt.get());
			sensorTypeEntity.setCreatedUser(userOpt.get());
		}
		
		Optional<SensorCategoryEntity> sensorCategoryOpt = this.sensorCategoryRepository.findById(sensorType.getCategory().getId()); 
		if(sensorCategoryOpt.isPresent()) {
			sensorTypeEntity.setCategory(sensorCategoryOpt.get());
		}
		
		sensorTypeEntity.setIsActive(Boolean.TRUE);
		
		return sensorTypeRepository.save(sensorTypeEntity).toSensorTypeDto();
	}
	
	public SensorTypeDto updateSensorType(SensorTypeDto sensorType) {
		SensorTypeEntity sensorTypeEntity = new SensorTypeEntity();
				
		Optional<SensorTypeEntity> sensorTypeOpt = sensorTypeRepository.findById(sensorType.getId());
		
		if(sensorTypeOpt.isPresent()) {
			sensorTypeEntity = sensorTypeOpt.get();
			BeanUtils.copyProperties(sensorType, sensorTypeEntity);
			
			sensorTypeEntity.setDateLastModified(new Date());
			
			Optional<UserEntity> userOpt = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername()); 
			if(userOpt.isPresent()) {
				sensorTypeEntity.setLastModifieduser(userOpt.get());
			}
			
			Optional<SensorCategoryEntity> sensorCategoryOpt = this.sensorCategoryRepository.findById(sensorType.getCategory().getId()); 
			if(sensorCategoryOpt.isPresent()) {
				sensorTypeEntity.setCategory(sensorCategoryOpt.get());
			}
			
			this.sensorTypeRepository.save(sensorTypeEntity);
		} else {
			throw new EntityNotFoundException("El tipo de sensor solicitado no se encuentra almacenado");
		}
		
		return sensorTypeEntity.toSensorTypeDto();
	}

}
