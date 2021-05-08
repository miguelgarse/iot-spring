package es.upm.etsisi.iot.modelo;

import java.util.Date;
import java.util.List;

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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import es.upm.etsisi.iot.dto.ProjectDto;
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

	@JsonIgnoreProperties(value = "project")
	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
	private List<SensorEntity> sensors;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User createdUser;

	@ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
	private User lastModifieduser;

	@Temporal(TemporalType.DATE)
	private Date dateCreated;

	@Temporal(TemporalType.DATE)
	private Date dateLastModified;

	public ProjectEntity(ProjectDto project) {
		BeanUtils.copyProperties(project, this);
	}

	public ProjectDto toProjectDto() {
		ProjectDto project = new ProjectDto();
		BeanUtils.copyProperties(this, project);
		return project;
	}

}
