package fr.softeam.starpointsapp.web.rest;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.repository.StarPointsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.hamcrest.Matchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarPointsApp.class)
public class StarpointsResourceIntTest {

    @Inject
    private StarPointsRepository starPointsRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    private MockMvc restStarPointsMockMvc;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StarpointsResource starpointsResource = new StarpointsResource();
        ReflectionTestUtils.setField(starpointsResource, "starPointsRepository", starPointsRepository);
        this.restStarPointsMockMvc = MockMvcBuilders.standaloneSetup(starpointsResource)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Test
    public void testGetStarPointsByCommunity() throws Exception {
        //test avec l'utilisateur bgiegel
        restStarPointsMockMvc.perform(get("/api/starpoints-by-community/5"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].community").value(contains("Java", "Agile")));
    }

    @Test
    public void testGetStarPointsByCommunity_allUsers() throws Exception {
        //test avec l'utilisateur bgiegel
        restStarPointsMockMvc.perform(get("/api/starpoints-by-community"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].community").value(contains("Java", "Agile")));
    }
}
