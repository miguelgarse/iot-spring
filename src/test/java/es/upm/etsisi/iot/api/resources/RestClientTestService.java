package es.upm.etsisi.iot.api.resources;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.upm.etsisi.iot.domain.services.ProjectService;


@Service
public class RestClientTestService {

    @Autowired
    private ProjectService projectService;

    private String token;

    

    public String getToken() {
        return token;
    }

}
