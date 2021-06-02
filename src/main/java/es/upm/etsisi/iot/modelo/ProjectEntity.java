package es.upm.etsisi.iot.modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import es.upm.etsisi.iot.dto.ProjectDto;
import es.upm.etsisi.iot.dto.SensorDto;
import es.upm.etsisi.iot.dto.SensorValueDto;
import es.upm.etsisi.iot.security.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "PROJECT")
public class ProjectEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROJECT_ID_GENERATOR")
	@SequenceGenerator(name = "PROJECT_ID_GENERATOR", sequenceName = "SEQ_PROJECT", allocationSize = 1)
	private Long id;
	private String title;
	private String description;
	private String keywords;
	private String location;

	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval=true)
	private List<SensorEntity> sensors;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User createdUser;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User lastModifieduser;

	@Temporal(TemporalType.DATE)
	private Date dateCreated;

	@Temporal(TemporalType.DATE)
	private Date dateLastModified;
	
	private Boolean isActive;

	public ProjectEntity(ProjectDto project) {
		BeanUtils.copyProperties(project, this);
		
		List<SensorEntity> sensorEntityList = new ArrayList<>();
		for(SensorDto sensorDto : project.getSensors()) {
			sensorEntityList.add(new SensorEntity(sensorDto));
		}
		
		this.setSensors(sensorEntityList);
		this.setCreatedUser(new User(project.getCreatedUser()));
		this.setLastModifieduser(new User(project.getLastModifieduser()));
	}

	public ProjectDto toProjectDto() {
		ProjectDto project = new ProjectDto();
		BeanUtils.copyProperties(this, project);
		
		project.setCreatedUser(this.getCreatedUser().toUserDto());
		project.setLastModifieduser(this.getLastModifieduser().toUserDto());
		
		List<SensorDto> sensorDtoList = new ArrayList<>();
		this.getSensors().stream().forEach(sensor -> {
			List<SensorValueDto> sensorValueDtoList = new ArrayList<>();
			
			sensor.getSensorValues().stream().forEach(sensorValue -> {
				sensorValueDtoList.add(sensorValue.toSensorValueDto());
			});
			
			SensorDto sensorDto = sensor.toSensorDto();
			sensorDto.setSensorValues(sensorValueDtoList);
			sensorDto.setSensorTypeId(sensor.getSensorType().getId());
			
			sensorDtoList.add(sensorDto);
		});
		
		project.setSensors(sensorDtoList);
		
		return project;
	}

}
