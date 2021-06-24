package es.upm.etsisi.iot.domain.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.etsisi.iot.data.daos.RoleRepository;
import es.upm.etsisi.iot.data.model.RoleEntity;
import es.upm.etsisi.iot.utils.RoleName;

@Service
@Transactional
public class RoleService {

	@Autowired
	RoleRepository roleRepository;

	public Optional<RoleEntity> getByRoleName(RoleName roleNombre) {
		return roleRepository.findByRoleName(roleNombre);
	}
}
