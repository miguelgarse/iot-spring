package es.upm.etsisi.iot.security.entity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Clase encargada de genrar la seguridad. Se podria hacer en la de User.
 * 
 * Cambiamos los roles por una coleccion de permisos.
 * 
 * @author Miguel
 *
 */
public class MainUser implements UserDetails {
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String username;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	
	public MainUser(String name, String username, String email, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.authorities = authorities;
	}

	/**
	 * Metodo que convierte un usuario de la bbdd al tipo usado por la seguridad
	 * 
	 * @param user Usuario obtenido de una consulta a BBDD.
	 * @return Usuario con los GrantedAuthorities list.
	 */
	public static MainUser build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(rol -> new SimpleGrantedAuthority(rol.getRoleName().name()))
				.collect(Collectors.toList());
		
		return new MainUser(user.getName(), user.getUsername(), user.getEmail(), user.getPassword(), authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
