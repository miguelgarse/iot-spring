package es.upm.etsisi.iot.security.dto;

import java.util.Date;
import java.util.Set;

import es.upm.etsisi.iot.security.entity.Role;

public class UserDto {

	private Long id;
	private String username;
	private String password;
	private String name;
	private String lastname;
	private String email;
	private Set<Role> roles;
	private UserDto createdUser;
	private Date dateCreated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public UserDto getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(UserDto createdUser) {
		this.createdUser = createdUser;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

}
