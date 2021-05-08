package es.upm.etsisi.iot.modelo.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.etsisi.iot.modelo.SensorTypeEntity;


@Repository
public interface SensorTypeRepository extends JpaRepository<SensorTypeEntity, Long> {

	SensorTypeEntity findByName(String name);
	
}
