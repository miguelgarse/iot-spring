package es.upm.etsisi.iot.modelo;

import java.util.Date;

import javax.persistence.Column;
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

import es.upm.etsisi.iot.dto.SensorCategoryDto;
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
@Table(name = "SENSOR_CATEGORY")
public class SensorCategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SENSOR_CATEGORY_ID_GENERATOR")
	@SequenceGenerator(name = "SENSOR_CATEGORY_ID_GENERATOR", sequenceName = "SEQ_SENSOR_CATEGORY", allocationSize = 1)
	private Long id;
	
	@Column(length = 64)
	private String category;
	
	@Column(length = 2000)
	private String description;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User createdUser;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User lastModifieduser;

	@Temporal(TemporalType.DATE)
	private Date dateCreated;

	@Temporal(TemporalType.DATE)
	private Date dateLastModified;
	
	private Boolean isActive;

	public SensorCategoryEntity(SensorCategoryDto sensorCategoryDto) {
		BeanUtils.copyProperties(sensorCategoryDto, this);
		
		this.setCreatedUser(new User(sensorCategoryDto.getCreatedUser()));
		this.setLastModifieduser(new User(sensorCategoryDto.getLastModifieduser()));
	}

	public SensorCategoryDto toSensorCategoryDto() {
		SensorCategoryDto sensorCategoryDto = new SensorCategoryDto();
		BeanUtils.copyProperties(this, sensorCategoryDto);
		
		sensorCategoryDto.setCreatedUser(this.getCreatedUser().toUserDto());
		sensorCategoryDto.setLastModifieduser(this.getLastModifieduser().toUserDto());
		
		return sensorCategoryDto;
	}

}
