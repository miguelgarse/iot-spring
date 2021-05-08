package es.upm.etsisi.iot.modelo;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.BeanUtils;

import es.upm.etsisi.iot.dto.SensorDto;
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
@Table(name = "SENSOR")
public class SensorEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SENSOR_ID_GENERATOR")
	@SequenceGenerator(name = "SENSOR_ID_GENERATOR", sequenceName = "SEQ_SENSOR", allocationSize = 1)
	private Long id;

	private String name;

	@ManyToOne(cascade = CascadeType.ALL)
	private SensorTypeEntity sensorType;
	
	@ManyToOne(targetEntity = ProjectEntity.class, fetch = FetchType.LAZY)
	private ProjectEntity project;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User createdUser;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User lastModifieduser;

	@Temporal(TemporalType.DATE)
	private Date dateCreated;

	@Temporal(TemporalType.DATE)
	private Date dateLastModified;
	

	public SensorEntity(SensorDto sensor) {
		BeanUtils.copyProperties(sensor, this);
	}

	public SensorDto toSensorDto() {
		SensorDto sensor = new SensorDto();
		BeanUtils.copyProperties(this, sensor);
		return sensor;
	}
	
}
