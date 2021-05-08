package es.upm.etsisi.iot.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsisi.iot.security.entity.Role;
import es.upm.etsisi.iot.security.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Integer> {
	Optional<Role> findByRoleName(RoleName roleNombre);
}
