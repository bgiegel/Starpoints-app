package fr.softeam.starpointsapp.web.rest;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.repository.ContributionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ContributionResource REST controller.
 *
 * @see ContributionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StarPointsApp.class)
@WebAppConfiguration
@IntegrationTest
public class ContributionResourceIntTest {


    private static final LocalDate DEFAULT_DELIVERABLE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DELIVERABLE_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final String DEFAULT_DELIVERABLE_URL = "AAAAA";
    private static final String UPDATED_DELIVERABLE_URL = "BBBBB";
    private static final String DEFAULT_DELIVERABLE_NAME = "AAAAA";
    private static final String UPDATED_DELIVERABLE_NAME = "BBBBB";
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    private static final LocalDate DEFAULT_PREPARATORY_DATE_1 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PREPARATORY_DATE_1 = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_PREPARATORY_DATE_2 = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PREPARATORY_DATE_2 = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private ContributionRepository contributionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restContributionMockMvc;

    private Contribution contribution;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContributionResource contributionResource = new ContributionResource();
        ReflectionTestUtils.setField(contributionResource, "contributionRepository", contributionRepository);
        this.restContributionMockMvc = MockMvcBuilders.standaloneSetup(contributionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        contribution = new Contribution();
        contribution.setDeliverableDate(DEFAULT_DELIVERABLE_DATE);
        contribution.setDeliverableUrl(DEFAULT_DELIVERABLE_URL);
        contribution.setDeliverableName(DEFAULT_DELIVERABLE_NAME);
        contribution.setComment(DEFAULT_COMMENT);
        contribution.setPreparatoryDate1(DEFAULT_PREPARATORY_DATE_1);
        contribution.setPreparatoryDate2(DEFAULT_PREPARATORY_DATE_2);
    }

    @Test
    @Transactional
    public void createContribution() throws Exception {
        int databaseSizeBeforeCreate = contributionRepository.findAll().size();

        // Create the Contribution

        restContributionMockMvc.perform(post("/api/contributions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(contribution)))
                .andExpect(status().isCreated());

        // Validate the Contribution in the database
        List<Contribution> contributions = contributionRepository.findAll();
        assertThat(contributions).hasSize(databaseSizeBeforeCreate + 1);
        Contribution testContribution = contributions.get(contributions.size() - 1);
        assertThat(testContribution.getDeliverableDate()).isEqualTo(DEFAULT_DELIVERABLE_DATE);
        assertThat(testContribution.getDeliverableUrl()).isEqualTo(DEFAULT_DELIVERABLE_URL);
        assertThat(testContribution.getDeliverableName()).isEqualTo(DEFAULT_DELIVERABLE_NAME);
        assertThat(testContribution.getComment()).isEqualTo(DEFAULT_COMMENT);
        assertThat(testContribution.getPreparatoryDate1()).isEqualTo(DEFAULT_PREPARATORY_DATE_1);
        assertThat(testContribution.getPreparatoryDate2()).isEqualTo(DEFAULT_PREPARATORY_DATE_2);
    }

    @Test
    @Transactional
    public void getAllContributions() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        // Get all the contributions
        restContributionMockMvc.perform(get("/api/contributions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contribution.getId().intValue())))
                .andExpect(jsonPath("$.[*].deliverableDate").value(hasItem(DEFAULT_DELIVERABLE_DATE.toString())))
                .andExpect(jsonPath("$.[*].deliverableUrl").value(hasItem(DEFAULT_DELIVERABLE_URL.toString())))
                .andExpect(jsonPath("$.[*].deliverableName").value(hasItem(DEFAULT_DELIVERABLE_NAME.toString())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
                .andExpect(jsonPath("$.[*].preparatoryDate1").value(hasItem(DEFAULT_PREPARATORY_DATE_1.toString())))
                .andExpect(jsonPath("$.[*].preparatoryDate2").value(hasItem(DEFAULT_PREPARATORY_DATE_2.toString())));
    }

    @Test
    @Transactional
    public void getContribution() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        // Get the contribution
        restContributionMockMvc.perform(get("/api/contributions/{id}", contribution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(contribution.getId().intValue()))
            .andExpect(jsonPath("$.deliverableDate").value(DEFAULT_DELIVERABLE_DATE.toString()))
            .andExpect(jsonPath("$.deliverableUrl").value(DEFAULT_DELIVERABLE_URL.toString()))
            .andExpect(jsonPath("$.deliverableName").value(DEFAULT_DELIVERABLE_NAME.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()))
            .andExpect(jsonPath("$.preparatoryDate1").value(DEFAULT_PREPARATORY_DATE_1.toString()))
            .andExpect(jsonPath("$.preparatoryDate2").value(DEFAULT_PREPARATORY_DATE_2.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingContribution() throws Exception {
        // Get the contribution
        restContributionMockMvc.perform(get("/api/contributions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateContribution() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);
        int databaseSizeBeforeUpdate = contributionRepository.findAll().size();

        // Update the contribution
        Contribution updatedContribution = new Contribution();
        updatedContribution.setId(contribution.getId());
        updatedContribution.setDeliverableDate(UPDATED_DELIVERABLE_DATE);
        updatedContribution.setDeliverableUrl(UPDATED_DELIVERABLE_URL);
        updatedContribution.setDeliverableName(UPDATED_DELIVERABLE_NAME);
        updatedContribution.setComment(UPDATED_COMMENT);
        updatedContribution.setPreparatoryDate1(UPDATED_PREPARATORY_DATE_1);
        updatedContribution.setPreparatoryDate2(UPDATED_PREPARATORY_DATE_2);

        restContributionMockMvc.perform(put("/api/contributions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedContribution)))
                .andExpect(status().isOk());

        // Validate the Contribution in the database
        List<Contribution> contributions = contributionRepository.findAll();
        assertThat(contributions).hasSize(databaseSizeBeforeUpdate);
        Contribution testContribution = contributions.get(contributions.size() - 1);
        assertThat(testContribution.getDeliverableDate()).isEqualTo(UPDATED_DELIVERABLE_DATE);
        assertThat(testContribution.getDeliverableUrl()).isEqualTo(UPDATED_DELIVERABLE_URL);
        assertThat(testContribution.getDeliverableName()).isEqualTo(UPDATED_DELIVERABLE_NAME);
        assertThat(testContribution.getComment()).isEqualTo(UPDATED_COMMENT);
        assertThat(testContribution.getPreparatoryDate1()).isEqualTo(UPDATED_PREPARATORY_DATE_1);
        assertThat(testContribution.getPreparatoryDate2()).isEqualTo(UPDATED_PREPARATORY_DATE_2);
    }

    @Test
    @Transactional
    public void deleteContribution() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);
        int databaseSizeBeforeDelete = contributionRepository.findAll().size();

        // Get the contribution
        restContributionMockMvc.perform(delete("/api/contributions/{id}", contribution.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Contribution> contributions = contributionRepository.findAll();
        assertThat(contributions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
