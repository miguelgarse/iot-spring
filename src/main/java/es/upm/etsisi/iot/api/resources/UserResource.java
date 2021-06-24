package es.upm.etsisi.iot.api.resources;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import es.upm.etsisi.iot.api.dtos.NewUserDto;
import es.upm.etsisi.iot.api.dtos.UserDto;
import es.upm.etsisi.iot.data.model.RoleEntity;
import es.upm.etsisi.iot.data.model.UserEntity;
import es.upm.etsisi.iot.domain.services.RoleService;
import es.upm.etsisi.iot.domain.services.UserService;
import es.upm.etsisi.iot.utils.RoleName;
import es.upm.etsisi.iot.utils.Utilities;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/api/user")
public class UserResource {

	@Autowired
	private Utilities utilities;
	private PasswordEncoder passwordEncoder;
	private UserService userService;
	private RoleService roleService;

	@Autowired
	public UserResource(PasswordEncoder passwordEncoder, UserService userService, RoleService roleService) {
		this.passwordEncoder = passwordEncoder;
		this.userService = userService;
		this.roleService = roleService;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/createUser")
	public ResponseEntity createUser(@Valid @RequestBody NewUserDto newUser, BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			return new ResponseEntity<>("Campos del usuario erroneos", HttpStatus.BAD_REQUEST);
		} else if(userService.existsByUsername(newUser.getUsername())){
			return new ResponseEntity<>("Nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
		} else if(userService.existsByEmail(newUser.getEmail())){
			return new ResponseEntity<>("Email ya existe", HttpStatus.BAD_REQUEST);
		} else {
			UserEntity user = new UserEntity(newUser.getName(), newUser.getUsername(), newUser.getEmail(),
					passwordEncoder.encode(newUser.getPassword()));
			
			user.setLastname(newUser.getLastname());
			
			Set<RoleEntity> roles = new HashSet<>();
			roles.add(roleService.getByRoleName(RoleName.ROLE_USER).get());
			
			if(newUser.getRoles().contains("admin")) {
				roles.add(roleService.getByRoleName(RoleName.ROLE_ADMIN).get());
			}
		
			user.setRoles(roles);
			
			// Auditoría
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			UserEntity createdBy = userService.findByUsername(authentication.getName()).get();
			user.setCreatedUser(createdBy);
			user.setDateCreated(new Date());
			user.setIsActive(Boolean.TRUE);
			
			userService.save(user);
			
			try {
				utilities.sendMail(user, newUser.getPassword());
			} catch (Exception excep) {
				excep.printStackTrace();
			}
			
			return new ResponseEntity<>(user.toUserDto(), HttpStatus.CREATED);
		}
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping()
	public ResponseEntity<List<UserEntity>> findAllUsers(){
		return new ResponseEntity<>(this.userService.findAll(), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> findUserById(@PathVariable Long userId){
		return new ResponseEntity<>(this.userService.findById(userId), HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public ResponseEntity<UserDto> deleteUserId(@PathVariable Long userId){
		UserDto userToDelete = this.userService.findById(userId);
		
		if(userToDelete != null) {
			this.userService.deleteUserById(userId);
			return new ResponseEntity<>(userToDelete, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/getCurrentUser")
	public ResponseEntity<UserDto> getCurrentUser(){
		return new ResponseEntity<>(this.userService.getCurrentUser(), HttpStatus.OK);
	}
	
	@GetMapping("/generateTokenApi")
	public ResponseEntity<UserDto> generateTokenApi(){
		return new ResponseEntity<>(this.userService.generateTokenApi(), HttpStatus.OK);
	}
	
	@PutMapping("/updateUserImage")
	public ResponseEntity<UserDto> updateUserImage(@RequestPart("image") String base64image) throws IOException{
		return new ResponseEntity<>(this.userService.updateUserImage(base64image), HttpStatus.OK);
	}
	
	@PutMapping("/updatePassword")
	public ResponseEntity<UserDto> updatePassword(@RequestPart("password") String password) throws IOException{
		return new ResponseEntity<>(this.userService.updatePassword(password), HttpStatus.OK);
	}
	
	@PutMapping("/updateGithub")
	public ResponseEntity<UserDto> updateGithub(@RequestPart("gitHub") String gitHub) throws IOException{
		return new ResponseEntity<>(this.userService.updateGithub(gitHub), HttpStatus.OK);
	}
}
