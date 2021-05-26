package es.upm.etsisi.iot.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import es.upm.etsisi.iot.dto.ProjectDto;
import es.upm.etsisi.iot.dto.SensorDto;

class TestClass {

	@Autowired
	private ProjectService projectService;
	
	
	@Test
	void test() throws ParseException {
		Date date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse("16/03/2021  13:05:00".trim());
		
		System.out.println(date);
	}
	
	@Test
	void testProcessCsvData() throws Exception{
		
		ProjectDto projectDto = projectService.findById(26L);
		assertNotNull(projectDto);
		
		File file = new File("C:/Users/migue/Desktop/CO2_vs_Temperature.csv");
		FileInputStream input = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(input));
		
		List<SensorDto> sensors = this.projectService.processCsvData(projectDto, multipartFile);
		assertNotNull(sensors);
		assertFalse(sensors.isEmpty());
	}

}
