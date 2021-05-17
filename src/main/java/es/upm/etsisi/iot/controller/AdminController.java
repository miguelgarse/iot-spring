package es.upm.etsisi.iot.controller;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.upm.etsisi.iot.security.dto.NewUser;
import es.upm.etsisi.iot.security.dto.UserDto;
import es.upm.etsisi.iot.security.entity.Role;
import es.upm.etsisi.iot.security.entity.User;
import es.upm.etsisi.iot.security.enums.RoleName;
import es.upm.etsisi.iot.security.service.RoleService;
import es.upm.etsisi.iot.security.service.UserService;

@CrossOrigin(value = "*")
@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping(value = "/api/admin")
public class AdminController {

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RoleService roleService;

	
	@PostMapping("/createUser")
	public ResponseEntity<String> createUser(@Valid @RequestBody NewUser newUser, BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			return new ResponseEntity<>("Campos del usuario erroneos", HttpStatus.BAD_REQUEST);
		} else if(userService.existsByUsername(newUser.getUsername())){
			return new ResponseEntity<>("Nombre ya exsite", HttpStatus.BAD_REQUEST);
		} else if(userService.existsByEmail(newUser.getEmail())){
			return new ResponseEntity<>("Email ya exsite", HttpStatus.BAD_REQUEST);
		} else {
			User user = new User(newUser.getName(), newUser.getUsername(), newUser.getEmail(),
					passwordEncoder.encode(newUser.getPassword()));
			
			user.setLastname(newUser.getLastname());
			
			Set<Role> roles = new HashSet<>();
			roles.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
			
			if(newUser.getRoles().contains("admin")) {
				roles.add(roleService.getByRoleName(RoleName.ROLE_ADMIN).get());
			}
		
			user.setRoles(roles);
			
			// Auditoría
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			User createdBy = userService.getByUsername(authentication.getName()).get();
			user.setCreatedUser(createdBy);
			user.setDateCreated(new Date());
			
			userService.save(user);
			
			return new ResponseEntity<>("Usuario creado", HttpStatus.CREATED);
		}
	}
	
	@GetMapping()
	public ResponseEntity<List<User>> findAllUsers(){
		return new ResponseEntity<>(this.userService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> findUserById(@PathVariable Long userId){
		return new ResponseEntity<>(this.userService.findById(userId), HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}")
	public ResponseEntity<UserDto> deleteUserId(@PathVariable Long userId){
		UserDto userToDelete = this.userService.findById(userId);
		
		if(userToDelete != null) {
			this.userService.deleteById(userId);
			return new ResponseEntity<>(userToDelete, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
}
