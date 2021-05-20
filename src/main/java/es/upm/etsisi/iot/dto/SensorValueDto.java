package es.upm.etsisi.iot.dto;

import java.math.BigDecimal;
import java.util.Date;

import es.upm.etsisi.iot.security.dto.UserDto;

public class SensorValueDto {

	private Long id;
	private SensorDto sensor;
	private BigDecimal value;
	private Date timestamp;
	private UserDto createdUser;
	private UserDto lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;

	public SensorValueDto() {
		// Empty constructor
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public SensorDto getSensor() {
		return sensor;
	}

	public void setSensor(SensorDto sensor) {
		this.sensor = sensor;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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
