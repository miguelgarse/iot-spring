package es.upm.etsisi.iot.dto;

import java.util.Date;

import es.upm.etsisi.iot.security.entity.User;

public class SensorDto {

	private Long id;
	private String name;
	private String sensorTypeId;
	private User createdUser;
	private User lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;

	public SensorDto() {
		super();
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

	public String getSensorTypeId() {
		return sensorTypeId;
	}

	public void setSensorTypeId(String sensorTypeId) {
		this.sensorTypeId = sensorTypeId;
	}

	public User getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(User createdUser) {
		this.createdUser = createdUser;
	}

	public User getLastModifieduser() {
		return lastModifieduser;
	}

	public void setLastModifieduser(User lastModifieduser) {
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
