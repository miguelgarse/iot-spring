package es.upm.etsisi.iot.data.daos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.etsisi.iot.data.model.ProjectEntity;
import es.upm.etsisi.iot.data.model.UserEntity;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

	List<ProjectEntity> findByCreatedUserAndIsActiveTrueOrderByDateLastModifiedDesc(UserEntity createdUser);
	
	List<ProjectEntity> findByTitleLike(String title);
	
	List<ProjectEntity> findByIsActiveTrueOrderByDateLastModifiedDesc();
	
}
