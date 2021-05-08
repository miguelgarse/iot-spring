package es.upm.etsisi.iot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.etsisi.iot.dto.SensorDto;
import es.upm.etsisi.iot.modelo.dao.SensorRepository;
import es.upm.etsisi.iot.modelo.dao.SensorValueRepository;

@Service
public class SensorValueService {
	
	private SensorRepository sensorRepository;
	
	private SensorValueRepository sensorValueRepository;
	
	@Autowired
	public SensorValueService(SensorRepository sensorRepository, SensorValueRepository sensorValueRepository) {
		this.sensorRepository = sensorRepository;
		this.sensorValueRepository = sensorValueRepository;
	}

	public List<SensorDto> findAllSensorValueBySensorId(String sensorId) {
		// Empty service
		return null;
	}

}
