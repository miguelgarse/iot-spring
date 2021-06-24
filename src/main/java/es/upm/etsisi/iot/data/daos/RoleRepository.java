package es.upm.etsisi.iot.data.daos;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import es.upm.etsisi.iot.data.model.RoleEntity;
import es.upm.etsisi.iot.utils.RoleName;

public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
	Optional<RoleEntity> findByRoleName(RoleName roleNombre);
}
