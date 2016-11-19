package fr.softeam.starpointsapp.web.rest;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.domain.Activity;
import fr.softeam.starpointsapp.domain.Community;
import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.domain.User;
import fr.softeam.starpointsapp.domain.enumeration.ActivityType;
import fr.softeam.starpointsapp.repository.ActivityRepository;
import fr.softeam.starpointsapp.repository.CommunityRepository;
import fr.softeam.starpointsapp.repository.ContributionRepository;
import fr.softeam.starpointsapp.repository.UserRepository;
import fr.softeam.starpointsapp.service.ActivityService;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Test class for the ActivityResource REST controller.
 *
 * @see ActivityResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarPointsApp.class)
public class ActivityResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final ActivityType DEFAULT_TYPE = ActivityType.INTERNAL_BLOG_POST;
    private static final ActivityType UPDATED_TYPE = ActivityType.TECHNICAL_EVENT;
    private static final String DEFAULT_DELIVERABLE_DEFINITION = "AAAAA";
    private static final String UPDATED_DELIVERABLE_DEFINITION = "BBBBB";

    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private CommunityRepository communityRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private ContributionRepository contributionRepository;

    @Inject
    private ActivityService activityService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restActivityMockMvc;

    private Activity activity;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ActivityResource activityResource = new ActivityResource();
        ReflectionTestUtils.setField(activityResource, "activityRepository", activityRepository);
        ReflectionTestUtils.setField(activityResource, "activityService", activityService);
        this.restActivityMockMvc = MockMvcBuilders.standaloneSetup(activityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    public Activity createEntity() {
        return new ActivityBuilder(DEFAULT_NAME, DEFAULT_TYPE).
            withDeliverableDefinition(DEFAULT_DELIVERABLE_DEFINITION).
            build();
    }

    @Before
    public void initTest() {
        activity = createEntity();
    }

    @Test
    @Transactional
    public void createActivity() throws Exception {
        int databaseSizeBeforeCreate = activityRepository.findAll().size();

        // Create the Activity

        restActivityMockMvc.perform(post("/api/activities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(activity)))
                .andExpect(status().isCreated());

        // Validate the Activity in the database
        List<Activity> activities = activityRepository.findAll();
        assertThat(activities).hasSize(databaseSizeBeforeCreate + 1);
        Activity testActivity = activities.get(activities.size() - 1);
        assertThat(testActivity.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testActivity.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testActivity.getDeliverableDefinition()).isEqualTo(DEFAULT_DELIVERABLE_DEFINITION);
    }

    @Test
    @Transactional
    public void getAllActivities() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get all the activities
        restActivityMockMvc.perform(get("/api/activities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(activity.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].deliverableDefinition").value(hasItem(DEFAULT_DELIVERABLE_DEFINITION)));
    }

    @Test
    @Transactional
    public void getActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);

        // Get the activity
        restActivityMockMvc.perform(get("/api/activities/{id}", activity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(activity.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.deliverableDefinition").value(DEFAULT_DELIVERABLE_DEFINITION));
    }

    @Test
    @Transactional
    public void getNonExistingActivity() throws Exception {
        // Get the activity
        restActivityMockMvc.perform(get("/api/activities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);
        int databaseSizeBeforeUpdate = activityRepository.findAll().size();

        // Update the activity
        Activity updatedActivity = activityRepository.findOne(activity.getId());
        updatedActivity.setName(UPDATED_NAME);
        updatedActivity.setType(UPDATED_TYPE);
        updatedActivity.setDeliverableDefinition(UPDATED_DELIVERABLE_DEFINITION);

        restActivityMockMvc.perform(put("/api/activities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedActivity)))
                .andExpect(status().isOk());

        // Validate the Activity in the database
        List<Activity> activities = activityRepository.findAll();
        assertThat(activities).hasSize(databaseSizeBeforeUpdate);
        Activity testActivity = activities.get(activities.size() - 1);
        assertThat(testActivity.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testActivity.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testActivity.getDeliverableDefinition()).isEqualTo(UPDATED_DELIVERABLE_DEFINITION);
    }

    @Test
    @Transactional
    public void deleteActivity() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);
        int databaseSizeBeforeDelete = activityRepository.findAll().size();

        // Get the activity
        restActivityMockMvc.perform(delete("/api/activities/{id}", activity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Activity> activities = activityRepository.findAll();
        assertThat(activities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void deleteActivityWithContribution() throws Exception {
        // Initialize the database
        activityRepository.saveAndFlush(activity);
        attachContributionToActivity();

        // Get the activity
        restActivityMockMvc.perform(delete("/api/activities/{id}", activity.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isBadRequest());
    }

    private void attachContributionToActivity() {
        User leader = new UserBuilder("leader").build();
        User author = new UserBuilder("author").build();

        Community community = new CommunityBuilder(leader).build();

        Contribution contribution = new ContributionBuilder(activity, community, author).
            withDeliverableName("java contribution 2").
            build();

        userRepository.save(leader);
        communityRepository.save(community);
        userRepository.save(author);
        contributionRepository.save(contribution);
    }
}
