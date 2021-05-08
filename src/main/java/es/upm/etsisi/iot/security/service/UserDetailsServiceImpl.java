package es.upm.etsisi.iot.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import es.upm.etsisi.iot.security.entity.MainUser;
import es.upm.etsisi.iot.security.entity.User;

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
		User user = userService.getByUsername(username).get();
		return MainUser.build(user);
	}

}
