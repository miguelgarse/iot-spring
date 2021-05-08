package es.upm.etsisi.iot.dto;

import java.math.BigDecimal;
import java.util.Date;

import es.upm.etsisi.iot.security.entity.User;

public class SensorValueDto {

	private Long id;
	private SensorDto sensor;
	private BigDecimal valor;
	private Date timestamp;
	private User createdUser;
	private User lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;

	public SensorValueDto() {
		super();
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

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
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
