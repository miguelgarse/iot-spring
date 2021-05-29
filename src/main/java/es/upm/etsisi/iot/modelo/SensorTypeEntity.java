package es.upm.etsisi.iot.modelo;

import java.util.Date;

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

import es.upm.etsisi.iot.dto.SensorTypeDto;
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
@Table(name = "SENSOR_TYPE")
public class SensorTypeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SENSOR_TYPE_ID_GENERATOR")
	@SequenceGenerator(name = "SENSOR_TYPE_ID_GENERATOR", sequenceName = "SEQ_SENSOR_TYPE", allocationSize = 1)
	private Long id;

	private String code;
	
	private String type;
	
	private String description;
	
	private String manufacturer;
	
	private String url;
	
	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User createdUser;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User lastModifieduser;

	@Temporal(TemporalType.DATE)
	private Date dateCreated;

	@Temporal(TemporalType.DATE)
	private Date dateLastModified;

	private Boolean isActive;
	
	public SensorTypeEntity(SensorTypeDto sensorType) {
		BeanUtils.copyProperties(sensorType, this);
	}

	public SensorTypeDto toSensorTypeDto() {
		SensorTypeDto sensorType = new SensorTypeDto();
		BeanUtils.copyProperties(this, sensorType);
		return sensorType;
	}
	
}
