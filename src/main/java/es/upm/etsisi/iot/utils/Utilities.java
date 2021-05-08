package es.upm.etsisi.iot.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import es.upm.etsisi.iot.security.entity.MainUser;

public class Utilities {
	
	private Utilities() {
		super();
	}

	public static MainUser getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (MainUser) authentication.getPrincipal();
	}
}
