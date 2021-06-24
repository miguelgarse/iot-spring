package es.upm.etsisi.iot.data.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.etsisi.iot.data.model.SensorCategoryEntity;

@Repository
public interface SensorCategoryRepository extends JpaRepository<SensorCategoryEntity, Long> {

	List<SensorCategoryEntity> findByIsActiveTrueOrderByCategoryAsc();
	
}
