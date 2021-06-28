package es.upm.etsisi.iot.api.resources;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import es.upm.etsisi.iot.domain.exceptions.BadRequestException;
import es.upm.etsisi.iot.domain.exceptions.ConflictException;
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
	public UserDto createUser(@Valid @RequestBody NewUserDto newUser, BindingResult bindingResult){
		UserDto createdUserDto = null;
		
		if(bindingResult.hasErrors()) {
			throw new BadRequestException("Campos del usuario erroneos");
		} else if(userService.existsByUsername(newUser.getUsername())){
			throw new ConflictException("Nombre de usuario ya existe");
		} else if(userService.existsByEmail(newUser.getEmail())){
			throw new ConflictException("Email ya existe");
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
			
			createdUserDto = user.toUserDto();
		}
		
		return createdUserDto;
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping()
	public List<UserEntity> findAllUsers(){
		return this.userService.findAll();
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/{userId}")
	public UserDto findUserById(@PathVariable Long userId){
		return this.userService.findById(userId);
	}
	
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{userId}")
	public UserDto deleteUserId(@PathVariable Long userId){
		UserDto userToDelete = this.userService.findById(userId);
		
		if(userToDelete != null) {
			this.userService.deleteUserById(userId);
			return userToDelete;
		} else {
			throw new BadRequestException("The User Id is not correct");
		}
	}
	
	@GetMapping("/getCurrentUser")
	public UserDto getCurrentUser(){
		return this.userService.getCurrentUser();
	}
	
	@GetMapping("/generateTokenApi")
	public UserDto generateTokenApi(){
		return this.userService.generateTokenApi();
	}
	
	@PutMapping("/updateUserImage")
	public UserDto updateUserImage(@RequestPart("image") String base64image) throws IOException{
		return this.userService.updateUserImage(base64image);
	}
	
	@PutMapping("/updatePassword")
	public UserDto updatePassword(@RequestPart("password") String password) throws IOException{
		return this.userService.updatePassword(password);
	}
	
	@PutMapping("/updateGithub")
	public UserDto updateGithub(@RequestPart("gitHub") String gitHub) throws IOException{
		return this.userService.updateGithub(gitHub);
	}
}
