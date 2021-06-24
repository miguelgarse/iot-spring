package es.upm.etsisi.iot.data.daos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.etsisi.iot.data.model.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	Optional<UserEntity> findByUsernameAndIsActiveTrue(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
	
	Optional<UserEntity> findByTokenApiAndIsActiveTrue(String tokenApi);
	
}
