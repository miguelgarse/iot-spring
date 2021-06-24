package es.upm.etsisi.iot.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.upm.etsisi.iot.api.dtos.MainUserDto;
import es.upm.etsisi.iot.data.model.UserEntity;

/**
 * Esta clase la vamos a tener siempre que trabajemos con JWT
 * 
 * @author Miguel
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userService.findByUsername(username).get();
		return MainUserDto.build(user);
	}

}
