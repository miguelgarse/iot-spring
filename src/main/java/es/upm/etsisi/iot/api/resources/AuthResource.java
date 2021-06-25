package es.upm.etsisi.iot.api.resources;

import java.util.Date;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.upm.etsisi.iot.api.dtos.JwtDto;
import es.upm.etsisi.iot.api.dtos.LoginUserDto;
import es.upm.etsisi.iot.configurations.JwtProvider;
import es.upm.etsisi.iot.data.model.UserEntity;
import es.upm.etsisi.iot.domain.exceptions.BadRequestException;
import es.upm.etsisi.iot.domain.services.RoleService;
import es.upm.etsisi.iot.domain.services.UserService;

@RestController
@RequestMapping("/auth")
@CrossOrigin(value = "*")
public class AuthResource {
	PasswordEncoder passwordEncoder;
	AuthenticationManager authenticationManager;
	UserService userService;
	RoleService roleService;
	JwtProvider jwtProvider;
	
	@Autowired
	public AuthResource(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
			UserService userService, RoleService roleService, JwtProvider jwtProvider) {
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.roleService = roleService;
		this.jwtProvider = jwtProvider;
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUserDto loginUser, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			throw new BadRequestException("Campos del usuario erroneos");
		} else {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtProvider.generateToken(authentication);
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
			
			Optional<UserEntity> optionalUser = this.userService.findByUsername(loginUser.getUsername());
			if(optionalUser.isPresent()) {
				UserEntity user = optionalUser.get();
				jwtDto.setDateLastLogin(user.getDateLastLogin()!=null?user.getDateLastLogin():null);
				user.setDateLastLogin(new Date());
				this.userService.save(user);
			}
			
			return new ResponseEntity<>(jwtDto, HttpStatus.OK);
		}
	}
	
}
