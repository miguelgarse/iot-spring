package es.upm.etsisi.iot.modelo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.etsisi.iot.modelo.ProjectEntity;
import es.upm.etsisi.iot.security.entity.User;

@Repository
public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

	List<ProjectEntity> findByCreatedUser(User createdUser);
	
	List<ProjectEntity> findByTitleLike(String title);
	
}
