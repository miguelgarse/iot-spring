package es.upm.etsisi.iot.modelo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.etsisi.iot.modelo.SensorTypeEntity;

@Repository
public interface SensorTypeRepository extends JpaRepository<SensorTypeEntity, Long> {

	List<SensorTypeEntity> findByIsActiveTrueOrderByDateLastModifiedDesc();
	
}
