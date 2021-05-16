package es.upm.etsisi.iot.dto;

import java.util.Date;

import es.upm.etsisi.iot.security.dto.UserDto;

public class SensorDto {

	private Long id;
	private String name;
	private Long sensorTypeId;
	private UserDto createdUser;
	private UserDto lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;

	public SensorDto() {
		// Empty constructor
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSensorTypeId() {
		return sensorTypeId;
	}

	public void setSensorTypeId(Long sensorTypeId) {
		this.sensorTypeId = sensorTypeId;
	}

	public UserDto getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(UserDto createdUser) {
		this.createdUser = createdUser;
	}

	public UserDto getLastModifieduser() {
		return lastModifieduser;
	}

	public void setLastModifieduser(UserDto lastModifieduser) {
		this.lastModifieduser = lastModifieduser;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public Date getDateLastModified() {
		return dateLastModified;
	}

	public void setDateLastModified(Date dateLastModified) {
		this.dateLastModified = dateLastModified;
	}

}
