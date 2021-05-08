package es.upm.etsisi.iot.security.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Comprueba si hay token valido, sino envia un 403
 * 
 * El unico método que tiene es invocado cuando no es valido.
 * 
 * @author Miguel
 *
 */
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

	private static final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Error en metodo commence");
		
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No autorizado");
	}

}
