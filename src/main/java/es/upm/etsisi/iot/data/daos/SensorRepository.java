package es.upm.etsisi.iot.data.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.etsisi.iot.data.model.SensorEntity;


@Repository
public interface SensorRepository extends JpaRepository<SensorEntity, Long> {

	List<SensorEntity> findBySensorType(Long id);

	SensorEntity findByName(String a);
	
	List<SensorEntity> findByProjectId(Long projectId);
	
}
