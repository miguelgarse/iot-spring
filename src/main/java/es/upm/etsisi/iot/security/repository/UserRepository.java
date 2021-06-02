package es.upm.etsisi.iot.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import es.upm.etsisi.iot.security.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findByUsernameAndIsActiveTrue(String username);

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);
	
	Optional<User> findByTokenApiAndIsActiveTrue(String tokenApi);
	
}
