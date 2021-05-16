package es.upm.etsisi.iot.modelo;

import java.math.BigDecimal;
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
@Table(name = "SENSOR_VALUE")
public class SensorValueEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SENSOR_VALUE_ID_GENERATOR")
	@SequenceGenerator(name = "SENSOR_VALUE_ID_GENERATOR", sequenceName = "SEQ_SENSOR_VALUE", allocationSize = 1)
	private Long id;

	private BigDecimal valor;

	private Date timestamp;

	@ManyToOne(cascade = CascadeType.ALL)
	private SensorEntity sensor;

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

	public SensorValueEntity(SensorValueDto sensorValue) {
		BeanUtils.copyProperties(sensorValue, this);
	}

	public SensorValueDto toSensorValueDto() {
		SensorValueDto sensorValue = new SensorValueDto();
		BeanUtils.copyProperties(this, sensorValue);
		return sensorValue;
	}

}