package es.upm.etsisi.iot.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.springframework.beans.BeanUtils;

import es.upm.etsisi.iot.dto.SensorTypeDto;
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

	private String name;

	private String description;
	
	public SensorTypeEntity(SensorTypeDto sensorType) {
		BeanUtils.copyProperties(sensorType, this);
	}

	public SensorTypeDto toSensorTypeDto() {
		SensorTypeDto sensorType = new SensorTypeDto();
		BeanUtils.copyProperties(this, sensorType);
		return sensorType;
	}
	
}
