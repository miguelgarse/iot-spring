package es.upm.etsisi.iot.security.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.upm.etsisi.iot.security.dto.UserDto;
import es.upm.etsisi.iot.security.entity.User;
import es.upm.etsisi.iot.security.repository.UserRepository;
import es.upm.etsisi.iot.utils.Utilities;

@Service
@Transactional
public class UserService {

	@Autowired
	UserRepository userRepository;

	public Optional<User> getByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

	public void save(User user) {
		userRepository.save(user);
	}

	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public UserDto findById(Long userId) {
		Optional<User> user = userRepository.findById(userId);
		
		if(user.isPresent()) {
			User foundUser = user.get();
			foundUser.setPassword("");
			
			UserDto dto = foundUser.toUserDto();
			return dto;
		} else {
			return null;
		}
	}
	
	public void deleteById(Long userId) {
		userRepository.deleteById(userId);
	}
	
	public UserDto getCurrentUser(){
		Optional<User> optionalUser = this.userRepository.findByUsername(Utilities.getCurrentUser().getUsername());
		
		UserDto userDto = new UserDto();
		if(optionalUser.isPresent()) {
			userDto = optionalUser.get().toUserDto();
		}
		
		return userDto;
	}
	
	
	public UserDto generateTokenApi(){
		Optional<User> optionalUser = this.userRepository.findByUsername(Utilities.getCurrentUser().getUsername());
		
		UserDto userDto = new UserDto();
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();

			String tokenApi = RandomStringUtils.randomAlphanumeric(15);
			user.setTokenApi(tokenApi);
			this.userRepository.save(user);
			
			userDto = user.toUserDto();
		}
		
		return userDto;
	}
	
	public UserDto updateUserImage(String base64image) throws IOException{
		Optional<User> optionalUser = this.userRepository.findByUsername(Utilities.getCurrentUser().getUsername());
		
		UserDto userDto = new UserDto();
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();

			user.setProfileImage(base64image);
			this.userRepository.save(user);
			
			userDto = user.toUserDto();
		}
		
		return userDto;
	}
	
	
}
