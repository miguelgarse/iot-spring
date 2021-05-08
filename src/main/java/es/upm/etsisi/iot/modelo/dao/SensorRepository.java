package es.upm.etsisi.iot.modelo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.etsisi.iot.modelo.SensorEntity;


@Repository
public interface SensorRepository extends JpaRepository<SensorEntity, Long> {

	List<SensorEntity> findBySensorType(Long id);

	SensorEntity findByName(String a);
	
}
