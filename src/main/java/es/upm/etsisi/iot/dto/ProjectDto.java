package es.upm.etsisi.iot.dto;

import java.util.Date;
import java.util.List;

import es.upm.etsisi.iot.security.entity.User;

public class ProjectDto {

	private Long id;
	private String title;
	private String description;
	private String keywords;
	private String location;
	private List<SensorDto> sensors;
	private User createdUser;
	private User lastModifieduser;
	private Date dateCreated;
	private Date dateLastModified;

	public ProjectDto() {
		// Empty constructor
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public List<SensorDto> getSensors() {
		return sensors;
	}

	public void setSensors(List<SensorDto> sensors) {
		this.sensors = sensors;
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
