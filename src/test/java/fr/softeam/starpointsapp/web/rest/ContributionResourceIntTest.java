package fr.softeam.starpointsapp.web.rest;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.domain.Community;
import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.domain.User;
import fr.softeam.starpointsapp.repository.CommunityRepository;
import fr.softeam.starpointsapp.repository.ContributionRepository;
import fr.softeam.starpointsapp.repository.UserRepository;
import fr.softeam.starpointsapp.service.ContributionService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ContributionResource REST controller.
 *
 * @see ContributionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarPointsApp.class)
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
    public static final String DEFAULT_PASSWORD = "$2a$10$WGW1mRkah133EMBcJGQnf.2yHpO7gTbxQKVK2sVPVgmr3G.lFKcFO";
    public static final String USER1_LOGIN = "user1";

    @Inject
    private CommunityRepository communityRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private ContributionRepository contributionRepository;

    @Inject
    private ContributionService contributionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restContributionMockMvc;

    private Contribution contribution, javaContribution1, javaContribution2, agileContribution1;

    private Community community;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ContributionResource contributionResource = new ContributionResource();
        ReflectionTestUtils.setField(contributionResource, "contributionRepository", contributionRepository);
        ReflectionTestUtils.setField(contributionResource, "contributionService", contributionService);
        this.restContributionMockMvc = MockMvcBuilders.standaloneSetup(contributionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contribution createEntity(EntityManager em) {
        Contribution contribution = new Contribution();
        contribution.setDeliverableDate(DEFAULT_DELIVERABLE_DATE);
        contribution.setDeliverableUrl(DEFAULT_DELIVERABLE_URL);
        contribution.setDeliverableName(DEFAULT_DELIVERABLE_NAME);
        contribution.setComment(DEFAULT_COMMENT);
        contribution.setPreparatoryDate1(DEFAULT_PREPARATORY_DATE_1);
        contribution.setPreparatoryDate2(DEFAULT_PREPARATORY_DATE_2);
        return contribution;
    }

    @Before
    public void initTest() {
        contribution = createEntity(em);
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(contribution.getId().intValue())))
                .andExpect(jsonPath("$.[*].deliverableDate").value(hasItem(DEFAULT_DELIVERABLE_DATE.toString())))
                .andExpect(jsonPath("$.[*].deliverableUrl").value(hasItem(DEFAULT_DELIVERABLE_URL)))
                .andExpect(jsonPath("$.[*].deliverableName").value(hasItem(DEFAULT_DELIVERABLE_NAME)))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
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
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(contribution.getId().intValue()))
            .andExpect(jsonPath("$.deliverableDate").value(DEFAULT_DELIVERABLE_DATE.toString()))
            .andExpect(jsonPath("$.deliverableUrl").value(DEFAULT_DELIVERABLE_URL))
            .andExpect(jsonPath("$.deliverableName").value(DEFAULT_DELIVERABLE_NAME))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
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
        Contribution updatedContribution = contributionRepository.findOne(contribution.getId());
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

    @Test
    @Transactional
    public void findOneWithCommunityMembers() throws Exception {
        given_a_contribution_linked_to_a_community();

        restContributionMockMvc.perform(get("/api/contributions/{id}", contribution.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.community.name").value(community.getName()))
            .andExpect(jsonPath("$.community.members.[*].login").value(USER1_LOGIN));
    }

    @Test
    @Transactional
    public void getContributionsFromLeaderCommunities() throws Exception {
        given_a_contributions_linked_to_communities();

        restContributionMockMvc.perform(get("/api/contributions-from-communities-leaded-by/{leader}", USER1_LOGIN)
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[*].deliverableName").value(hasItem(javaContribution1.getDeliverableName())))
            .andExpect(jsonPath("$.[*].deliverableName").value(hasItem(javaContribution2.getDeliverableName())))
            .andExpect(jsonPath("$.[*].deliverableName").value(not(hasItem(agileContribution1.getDeliverableName()))));
    }

    @Test
    @Transactional
    public void getUserContributionsByQuarter() throws Exception {

        restContributionMockMvc.perform(get("/api/contributions-by-quarter/{quarter}/{login}", "Q3-2016", "bgiegel")
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getUserContributions() throws Exception {

        restContributionMockMvc.perform(get("/api/contributions/author/{login}", "bgiegel")
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getUserContributionsByQuarter_wrongQuarterFormat() throws Exception {

        restContributionMockMvc.perform(get("/api/contributions-by-quarter/{quarter}/{login}", "Q3-qsdsfq", "bgiegel")
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
    }

    private void given_a_contributions_linked_to_communities() {
        User leaderOfJavaCommunity = buildUser(USER1_LOGIN);
        Community javaCommunity = buildJavaCommunityAndContributions(leaderOfJavaCommunity);

        User leaderOfAgileCommunity = buildUser("agileLeader");
        Community agileCommunity = buildAgileCommunityAndContributions(leaderOfAgileCommunity);

        userRepository.save(leaderOfJavaCommunity);
        userRepository.save(leaderOfAgileCommunity);
        communityRepository.save(javaCommunity);
        communityRepository.save(agileCommunity);
        contributionRepository.save(javaContribution1);
        contributionRepository.save(javaContribution2);
        contributionRepository.save(agileContribution1);
    }

    private User buildUser(String agileLeader) {
        User leaderOfAgileCommunity = new User();
        leaderOfAgileCommunity.setLogin(agileLeader);
        leaderOfAgileCommunity.setPassword(DEFAULT_PASSWORD);
        return leaderOfAgileCommunity;
    }

    private Community buildAgileCommunityAndContributions(User leaderOfAgileCommunity) {
        Community agileCommunity = new Community();
        agileCommunity.setName("Agile");
        agileCommunity.setLeader(leaderOfAgileCommunity);

        agileContribution1 = new Contribution();
        agileContribution1.setDeliverableName("agile contribution 1");
        agileContribution1.setCommunity(agileCommunity);
        return agileCommunity;
    }

    private Community buildJavaCommunityAndContributions(User leaderOfJavaCommunity) {
        Community javaCommunity = new Community();
        javaCommunity.setName("Java");
        javaCommunity.setLeader(leaderOfJavaCommunity);

        javaContribution1 = new Contribution();
        javaContribution1.setDeliverableName("java contribution 1");
        javaContribution1.setCommunity(javaCommunity);

        javaContribution2 = new Contribution();
        javaContribution2.setDeliverableName("java contribution 2");
        javaContribution2.setCommunity(javaCommunity);
        return javaCommunity;
    }

    private void given_a_contribution_linked_to_a_community() {
        User user1 = buildUser(USER1_LOGIN);

        Set<User> members = new HashSet<>();
        members.add(user1);

        this.community = new Community();
        community.setName("Java");
        community.setMembers(members);

        contribution.setCommunity(community);

        userRepository.save(user1);
        communityRepository.save(community);
        contributionRepository.save(contribution);
    }

}
