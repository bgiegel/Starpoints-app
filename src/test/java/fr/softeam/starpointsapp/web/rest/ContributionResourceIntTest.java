package fr.softeam.starpointsapp.web.rest;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.domain.Activity;
import fr.softeam.starpointsapp.domain.Community;
import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.domain.User;
import fr.softeam.starpointsapp.repository.ActivityRepository;
import fr.softeam.starpointsapp.repository.CommunityRepository;
import fr.softeam.starpointsapp.repository.ContributionRepository;
import fr.softeam.starpointsapp.repository.UserRepository;
import fr.softeam.starpointsapp.service.ContributionService;
import fr.softeam.starpointsapp.util.ActivityBuilder;
import fr.softeam.starpointsapp.util.CommunityBuilder;
import fr.softeam.starpointsapp.util.ContributionBuilder;
import fr.softeam.starpointsapp.util.UserBuilder;
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
    public static final String USER1_LOGIN = "user1";

    @Inject
    private CommunityRepository communityRepository;


    @Inject
    private ActivityRepository activityRepository;

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

    private MockMvc restContributionMockMvc;

    private Contribution contribution, javaContribution1, javaContribution2, agileContribution1;

    private Community javaCommunity;

    private Activity activity;

    private User author;

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

    public Contribution createEntity() {
        User leader = new UserBuilder("leader").build();
        author = new UserBuilder("author").build();
        activity = new ActivityBuilder().build();
        Community community = new CommunityBuilder(leader).build();

        userRepository.save(leader);
        userRepository.save(author);
        activityRepository.save(activity);
        communityRepository.save(community);

        return new ContributionBuilder(activity, community, author).
            withDeliverableName(DEFAULT_DELIVERABLE_NAME).
            withDeliverableDate(DEFAULT_DELIVERABLE_DATE).
            withDeliverableUrl(DEFAULT_DELIVERABLE_URL).
            withComment(DEFAULT_COMMENT).
            withPreparatoryDate1(DEFAULT_PREPARATORY_DATE_1).
            withPreparatoryDate2(DEFAULT_PREPARATORY_DATE_2).
            build();
    }

    @Before
    public void initTest() {
        contribution = createEntity();
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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    @Transactional
    public void getContribution() throws Exception {
        // Initialize the database
        contributionRepository.saveAndFlush(contribution);

        // Get the contribution
        restContributionMockMvc.perform(get("/api/contributions/{id}", contribution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
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

        restContributionMockMvc.perform(get("/api/contributions/{id}", javaContribution1.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.community.name").value(javaCommunity.getName()))
            .andExpect(jsonPath("$.community.members.[*].login").value("member"));
    }

    @Test
    @Transactional
    public void getContributionsFromLeaderCommunities() throws Exception {
        given_contributions_linked_to_communities();

        restContributionMockMvc.perform(get("/api/contributions-from-communities-leaded-by/{leader}", USER1_LOGIN)
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(jsonPath("$.[*].deliverableName").value(hasItem(javaContribution1.getDeliverableName())))
            .andExpect(jsonPath("$.[*].deliverableName").value(hasItem(javaContribution2.getDeliverableName())))
            .andExpect(jsonPath("$.[*].deliverableName").value(not(hasItem(agileContribution1.getDeliverableName()))));
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
    public void getUserContributionsByQuarter() throws Exception {

        restContributionMockMvc.perform(get("/api/contributions-by-quarter/{quarter}/{login}", "Q3-2016", "bgiegel")
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

    @Test
    @Transactional
    public void getContributionsByQuarter() throws Exception {

        restContributionMockMvc.perform(get("/api/contributions-by-quarter/{quarter}", "Q3-2016")
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());
    }

    @Test
    @Transactional
    public void getContributionsByQuarter_wrongQuarterFormat() throws Exception {

        restContributionMockMvc.perform(get("/api/contributions-by-quarter/{quarter}", "Q3-qsdsfq")
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
    }

    private void given_contributions_linked_to_communities() {
        User leaderOfJavaCommunity = new UserBuilder(USER1_LOGIN).build();
        Community javaCommunity = buildJavaCommunityAndContributions(leaderOfJavaCommunity);

        User leaderOfAgileCommunity = new UserBuilder("agileLeader").build();
        Community agileCommunity = buildAgileCommunityAndContributions(leaderOfAgileCommunity);

        userRepository.save(leaderOfJavaCommunity);
        userRepository.save(leaderOfAgileCommunity);
        communityRepository.save(javaCommunity);
        communityRepository.save(agileCommunity);
        contributionRepository.save(javaContribution1);
        contributionRepository.save(javaContribution2);
        contributionRepository.save(agileContribution1);
    }

    private Community buildAgileCommunityAndContributions(User leaderOfAgileCommunity) {
        Community agileCommunity = new CommunityBuilder(leaderOfAgileCommunity).withName("Agile").build();

        agileContribution1 = new ContributionBuilder(activity, agileCommunity, author).
            withDeliverableName("agile contribution 1").
            build();
        return agileCommunity;
    }

    private Community buildJavaCommunityAndContributions(User leaderOfJavaCommunity) {
        Community javaCommunity = new CommunityBuilder(leaderOfJavaCommunity).withName("Java").build();

        javaContribution1 = new ContributionBuilder(activity, javaCommunity, author).
            withDeliverableName("java contribution 1").
            build();

        javaContribution2 = new ContributionBuilder(activity, javaCommunity, author).
            withDeliverableName("java contribution 2").
            build();
        return javaCommunity;
    }

    private void given_a_contribution_linked_to_a_community() {
        User member = new UserBuilder("member").build();
        Set<User> members = new HashSet<>();
        members.add(member);

        User leaderOfJavaCommunity = new UserBuilder(USER1_LOGIN).build();
        javaCommunity = buildJavaCommunityAndContributions(leaderOfJavaCommunity);
        javaCommunity.setMembers(members);

        userRepository.save(member);
        userRepository.save(leaderOfJavaCommunity);
        communityRepository.save(javaCommunity);
        contributionRepository.save(javaContribution1);
    }

}
