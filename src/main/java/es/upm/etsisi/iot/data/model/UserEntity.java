package es.upm.etsisi.iot.data.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.upm.etsisi.iot.api.dtos.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "USERS")
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Column(unique = true)
	private String username;
	
	@NotNull
	@Column(nullable = false)
	private String password;
	
	@NotNull
	private String name;
	
	private String lastname;
	
	@NotNull
	@Column(nullable = false)
	private String email;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<RoleEntity> roles;

	@JsonIgnoreProperties({"createdUser"})
	@OneToOne(targetEntity = UserEntity.class, fetch = FetchType.EAGER)
	private UserEntity createdUser;
	
	private Date dateCreated;
	
	private String githubAccount;
	
	private String tokenApi;
	
	@Lob
	private String profileImage;

	private Boolean isActive;
	
	private Date dateLastLogin;
	
	
	public UserEntity(@NotNull String name, @NotNull String username, @NotNull String email, @NotNull String password,
			Set<RoleEntity> roles) {
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.roles = roles;
	}
	
	public UserEntity(@NotNull String name, @NotNull String username, @NotNull String email, @NotNull String password) {
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	public UserEntity(UserDto userDto) {
		BeanUtils.copyProperties(userDto, this);
	}

	public UserDto toUserDto() {
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(this, userDto);
		
		if(this.getCreatedUser() != null) {
			userDto.setCreatedUser(this.getCreatedUser().toUserDto());
		}
		
		return userDto;
	}
	
}
