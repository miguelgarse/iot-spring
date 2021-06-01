package es.upm.etsisi.iot.security.controller;

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

import es.upm.etsisi.iot.security.dto.JwtDto;
import es.upm.etsisi.iot.security.dto.LoginUser;
import es.upm.etsisi.iot.security.entity.User;
import es.upm.etsisi.iot.security.jwt.JwtProvider;
import es.upm.etsisi.iot.security.service.RoleService;
import es.upm.etsisi.iot.security.service.UserService;


@RestController
@RequestMapping("/auth")
@CrossOrigin(value = "*")
public class AuthController {
	PasswordEncoder passwordEncoder;
	AuthenticationManager authenticationManager;
	UserService userService;
	RoleService roleService;
	JwtProvider jwtProvider;
	
	@Autowired
	public AuthController(PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
			UserService userService, RoleService roleService, JwtProvider jwtProvider) {
		this.passwordEncoder = passwordEncoder;
		this.authenticationManager = authenticationManager;
		this.userService = userService;
		this.roleService = roleService;
		this.jwtProvider = jwtProvider;
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUser loginUser, BindingResult bindingResult) {
		if(bindingResult.hasErrors()) {
			return new ResponseEntity("Campos del usuario erroneos", HttpStatus.BAD_REQUEST);
		} else {
			Authentication authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginUser.getUsername(), loginUser.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String jwt = jwtProvider.generateToken(authentication);
			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
			
			Optional<User> optionalUser = this.userService.findByUsername(loginUser.getUsername());
			if(optionalUser.isPresent()) {
				User user = optionalUser.get();
				user.setDateLastLogin(new Date());
				jwtDto.setDateLastLogin(new Date());
				this.userService.save(user);
			}
			
			return new ResponseEntity<>(jwtDto, HttpStatus.OK);
		}
	}
}
