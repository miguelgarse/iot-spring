package es.upm.etsisi.iot;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class IotApplication {

	public static void main(String[] args) {
		SpringApplication.run(IotApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	
}
