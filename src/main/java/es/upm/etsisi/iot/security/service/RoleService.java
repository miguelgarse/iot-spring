package es.upm.etsisi.iot.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.etsisi.iot.security.entity.Role;
import es.upm.etsisi.iot.security.enums.RoleName;
import es.upm.etsisi.iot.security.repository.RoleRepository;

@Service
@Transactional
public class RoleService {

	@Autowired
	RoleRepository roleRepository;

	public Optional<Role> getByRoleName(RoleName roleNombre) {
		return roleRepository.findByRoleName(roleNombre);
	}
}
