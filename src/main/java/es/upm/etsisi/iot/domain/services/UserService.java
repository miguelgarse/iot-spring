package es.upm.etsisi.iot.domain.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.etsisi.iot.api.dtos.UserDto;
import es.upm.etsisi.iot.data.daos.UserRepository;
import es.upm.etsisi.iot.data.model.UserEntity;
import es.upm.etsisi.iot.utils.Utilities;

@Service
@Transactional
public class UserService {
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private Utilities utilities;
	

	public Optional<UserEntity> findByUsername(String username) {
		return userRepository.findByUsernameAndIsActiveTrue(username);
	}

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public void save(UserEntity user) {
		userRepository.save(user);
	}

	public List<UserEntity> findAll() {
		return userRepository.findAll();
	}
	
	public UserDto findById(Long userId) {
		Optional<UserEntity> user = userRepository.findById(userId);
		
		UserDto userDto = new UserDto(); 
		if(user.isPresent()) {
			UserEntity foundUser = user.get();
			
			userDto = foundUser.toUserDto();
		} 
		
		return userDto;
	}
	
	public void deleteUserById(Long userId) {
		Optional<UserEntity> optionalUser = this.userRepository.findById(userId);
		
		if(optionalUser.isPresent()) {
			UserEntity user = optionalUser.get();
			user.setIsActive(Boolean.FALSE);
			
			this.userRepository.save(user);
		} else {
			throw new EntityNotFoundException("El usuario solicitado no se encuentra almacenado");
		}
	}
	
	public UserDto getCurrentUser(){
		Optional<UserEntity> optionalUser = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername());
		
		UserDto userDto = new UserDto();
		if(optionalUser.isPresent()) {
			userDto = optionalUser.get().toUserDto();
		}
		
		return userDto;
	}
	
	
	public UserDto generateTokenApi(){
		Optional<UserEntity> optionalUser = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername());
		
		UserDto userDto = new UserDto();
		if(optionalUser.isPresent()) {
			UserEntity user = optionalUser.get();

			String tokenApi = RandomStringUtils.randomAlphanumeric(15);
			user.setTokenApi(tokenApi);
			this.userRepository.save(user);
			
			userDto = user.toUserDto();
		}
		
		return userDto;
	}
	
	public UserDto updateUserImage(String base64image) throws IOException{
		Optional<UserEntity> optionalUser = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername());
		
		UserDto userDto = new UserDto();
		if(optionalUser.isPresent()) {
			UserEntity user = optionalUser.get();

			user.setProfileImage(base64image);
			this.userRepository.save(user);
			
			userDto = user.toUserDto();
		}
		
		return userDto;
	}
	
	public UserDto updatePassword(String password) throws IOException{
		Optional<UserEntity> optionalUser = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername());
		
		UserDto userDto = new UserDto();
		if(optionalUser.isPresent()) {
			UserEntity user = optionalUser.get();

			user.setPassword(passwordEncoder.encode(password));
			this.userRepository.save(user);
			
			userDto = user.toUserDto();
		}
		
		return userDto;
	}
	
	public Optional<UserEntity> findByTokenApi(String tokenApi) {
		return userRepository.findByTokenApiAndIsActiveTrue(tokenApi);
	}
	
	public UserDto updateGithub(String gitHub) throws IOException{
		Optional<UserEntity> optionalUser = this.userRepository.findByUsernameAndIsActiveTrue(utilities.getCurrentUser().getUsername());
		
		UserDto userDto = new UserDto();
		if(optionalUser.isPresent()) {
			UserEntity user = optionalUser.get();

			user.setGithubAccount(gitHub);
			this.userRepository.save(user);
			
			userDto = user.toUserDto();
		}
		
		return userDto;
	}
	
	
}
