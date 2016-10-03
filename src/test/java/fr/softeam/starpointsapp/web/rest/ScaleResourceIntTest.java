package fr.softeam.starpointsapp.web.rest;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.domain.Scale;
import fr.softeam.starpointsapp.repository.ScaleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ScaleResource REST controller.
 *
 * @see ScaleResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarPointsApp.class)
public class ScaleResourceIntTest {


    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_VALUE = 1;
    private static final Integer UPDATED_VALUE = 2;

    @Inject
    private ScaleRepository scaleRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restScaleMockMvc;

    private Scale scale;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ScaleResource scaleResource = new ScaleResource();
        ReflectionTestUtils.setField(scaleResource, "scaleRepository", scaleRepository);
        this.restScaleMockMvc = MockMvcBuilders.standaloneSetup(scaleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Scale createEntity(EntityManager em) {
        Scale scale = new Scale();
        scale.setStartDate(DEFAULT_START_DATE);
        scale.setEndDate(DEFAULT_END_DATE);
        scale.setValue(DEFAULT_VALUE);
        return scale;
    }

    @Before
    public void initTest() {
        scale = createEntity(em);
    }

    @Test
    @Transactional
    public void createScale() throws Exception {
        int databaseSizeBeforeCreate = scaleRepository.findAll().size();

        // Create the Scale

        restScaleMockMvc.perform(post("/api/scales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(scale)))
                .andExpect(status().isCreated());

        // Validate the Scale in the database
        List<Scale> scales = scaleRepository.findAll();
        assertThat(scales).hasSize(databaseSizeBeforeCreate + 1);
        Scale testScale = scales.get(scales.size() - 1);
        assertThat(testScale.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testScale.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testScale.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    public void getAllScales() throws Exception {
        // Initialize the database
        scaleRepository.saveAndFlush(scale);

        // Get all the scales
        restScaleMockMvc.perform(get("/api/scales?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(scale.getId().intValue())))
                .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    public void getScale() throws Exception {
        // Initialize the database
        scaleRepository.saveAndFlush(scale);

        // Get the scale
        restScaleMockMvc.perform(get("/api/scales/{id}", scale.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(scale.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    public void getNonExistingScale() throws Exception {
        // Get the scale
        restScaleMockMvc.perform(get("/api/scales/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScale() throws Exception {
        // Initialize the database
        scaleRepository.saveAndFlush(scale);
        int databaseSizeBeforeUpdate = scaleRepository.findAll().size();

        // Update the scale
        Scale updatedScale = scaleRepository.findOne(scale.getId());
        updatedScale.setStartDate(UPDATED_START_DATE);
        updatedScale.setEndDate(UPDATED_END_DATE);
        updatedScale.setValue(UPDATED_VALUE);

        restScaleMockMvc.perform(put("/api/scales")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedScale)))
                .andExpect(status().isOk());

        // Validate the Scale in the database
        List<Scale> scales = scaleRepository.findAll();
        assertThat(scales).hasSize(databaseSizeBeforeUpdate);
        Scale testScale = scales.get(scales.size() - 1);
        assertThat(testScale.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testScale.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testScale.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    public void deleteScale() throws Exception {
        // Initialize the database
        scaleRepository.saveAndFlush(scale);
        int databaseSizeBeforeDelete = scaleRepository.findAll().size();

        // Get the scale
        restScaleMockMvc.perform(delete("/api/scales/{id}", scale.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Scale> scales = scaleRepository.findAll();
        assertThat(scales).hasSize(databaseSizeBeforeDelete - 1);
    }
}
