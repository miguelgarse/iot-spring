package es.upm.etsisi.iot.security.dto;

import java.util.Date;
import java.util.Set;

import es.upm.etsisi.iot.security.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserDto {

	private Long id;
	private String username;
	private String name;
	private String lastname;
	private String email;
	private Set<Role> roles;
	private UserDto createdUser;
	private Date dateCreated;
	private String githubAccount;
	private String tokenApi;
	private String profileImage;

}
