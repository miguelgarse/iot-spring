package es.upm.etsisi.iot.data.daos;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import es.upm.etsisi.iot.TestConfig;

@TestConfig
class ProjectRepositoryIT {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    void testFindByMobile() {
        assertTrue(this.projectRepository.findById(1L).isPresent());
    }

    @Test
    void testFindByCreatedUserAndIsActiveTrueOrderByDateLastModifiedDesc() {
        this.projectRepository.findByIsActiveTrueOrderByDateLastModifiedDesc();
    }

    @Test
    void testFindByTitleLike() {
        this.projectRepository.findByTitleLike("");
    }

    @Test
    void testFindByIsActiveTrueOrderByDateLastModifiedDesc() {
        this.projectRepository.findByIsActiveTrueOrderByDateLastModifiedDesc();
    }

}
