package es.upm.etsisi.iot.data.model;

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

import org.hibernate.annotations.OrderBy;
import org.springframework.beans.BeanUtils;

import es.upm.etsisi.iot.api.dtos.SensorDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "SENSOR")
public class SensorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SENSOR_ID_GENERATOR")
	@SequenceGenerator(name = "SENSOR_ID_GENERATOR", sequenceName = "SEQ_SENSOR", allocationSize = 1)
	private Long id;

	private String name;

	@ManyToOne(fetch = FetchType.EAGER)
	private SensorTypeEntity sensorType;
	
	@ManyToOne
	private ProjectEntity project;
	
	@OrderBy(clause = "timestamp asc")
	@OneToMany(mappedBy = "sensor", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval=true)
	private List<SensorValueEntity> sensorValues;
	
	@ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
	private UserEntity createdUser;

	@ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
	private UserEntity lastModifieduser;

	@Temporal(TemporalType.DATE)
	private Date dateCreated;

	@Temporal(TemporalType.DATE)
	private Date dateLastModified;
	

	public SensorEntity(SensorDto sensor) {
		BeanUtils.copyProperties(sensor, this);
		
		this.setCreatedUser(new UserEntity(sensor.getCreatedUser()));
		this.setLastModifieduser(new UserEntity(sensor.getLastModifieduser()));
	}

	public SensorDto toSensorDto() {
		SensorDto sensor = new SensorDto();
		BeanUtils.copyProperties(this, sensor);
		return sensor;
	}
	
}
