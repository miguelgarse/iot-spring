package es.upm.etsisi.iot.modelo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.etsisi.iot.modelo.SensorValueEntity;

@Repository
public interface SensorValueRepository extends JpaRepository<SensorValueEntity, Long> {

	List<SensorValueEntity> findBySensorId(Long sensorId);
}
