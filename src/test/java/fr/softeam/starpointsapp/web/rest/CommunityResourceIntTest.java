package fr.softeam.starpointsapp.web.rest;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.domain.Community;
import fr.softeam.starpointsapp.domain.User;
import fr.softeam.starpointsapp.repository.CommunityRepository;

import fr.softeam.starpointsapp.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the CommunityResource REST controller.
 *
 * @see CommunityResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StarPointsApp.class)
@WebAppConfiguration
@IntegrationTest
public class CommunityResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    public static final String STARTECH_AGILE = "Startech Agile";
    public static final String STARTECH_JAVASCRIPT = "Startech Javascript";
    public static final String DEFAULT_PASSWORD = "$2a$10$WGW1mRkah133EMBcJGQnf.2yHpO7gTbxQKVK2sVPVgmr3G.lFKcFO";
    public static final String STARTECH_JAVA = "Startech Java";

    @Inject
    private CommunityRepository communityRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restCommunityMockMvc;

    private Community community;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CommunityResource communityResource = new CommunityResource();
        ReflectionTestUtils.setField(communityResource, "communityRepository", communityRepository);
        this.restCommunityMockMvc = MockMvcBuilders.standaloneSetup(communityResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        community = new Community();
        community.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createCommunity() throws Exception {
        int databaseSizeBeforeCreate = communityRepository.findAll().size();

        // Create the Community

        restCommunityMockMvc.perform(post("/api/communities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(community)))
                .andExpect(status().isCreated());

        // Validate the Community in the database
        List<Community> communities = communityRepository.findAll();
        assertThat(communities).hasSize(databaseSizeBeforeCreate + 1);
        Community testCommunity = communities.get(communities.size() - 1);
        assertThat(testCommunity.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllCommunities() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get all the communities
        restCommunityMockMvc.perform(get("/api/communities?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(community.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCommunity() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);

        // Get the community
        restCommunityMockMvc.perform(get("/api/communities/{id}", community.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(community.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCommunity() throws Exception {
        // Get the community
        restCommunityMockMvc.perform(get("/api/communities/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCommunity() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        int databaseSizeBeforeUpdate = communityRepository.findAll().size();

        // Update the community
        Community updatedCommunity = new Community();
        updatedCommunity.setId(community.getId());
        updatedCommunity.setName(UPDATED_NAME);

        restCommunityMockMvc.perform(put("/api/communities")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCommunity)))
                .andExpect(status().isOk());

        // Validate the Community in the database
        List<Community> communities = communityRepository.findAll();
        assertThat(communities).hasSize(databaseSizeBeforeUpdate);
        Community testCommunity = communities.get(communities.size() - 1);
        assertThat(testCommunity.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteCommunity() throws Exception {
        // Initialize the database
        communityRepository.saveAndFlush(community);
        int databaseSizeBeforeDelete = communityRepository.findAll().size();

        // Get the community
        restCommunityMockMvc.perform(delete("/api/communities/{id}", community.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Community> communities = communityRepository.findAll();
        assertThat(communities).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void should_return_list_of_communities_leaded_by_user() throws Exception {
        givenAListOfCommunitiesInDb();

        // Get all the communities
        ResultActions perform = restCommunityMockMvc.perform(get("/api/communities-leaded-by/machinbidule?sort=id,desc"));

        perform
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.[*].name").value(hasItems(STARTECH_AGILE, STARTECH_JAVASCRIPT)))
            .andExpect(jsonPath("$.[*].name").value(not(hasItem(STARTECH_JAVA))));
    }

    @Test
    @Transactional
    public void should_return_an_empty_list_when_user_dont_exist() throws Exception {
        givenAListOfCommunitiesInDb();

        // Get all the communities
        ResultActions perform = restCommunityMockMvc.perform(get("/api/communities-leaded-by/unknownUser?sort=id,desc"));

        perform
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(0)));
    }

    private void givenAListOfCommunitiesInDb() {
        User bgiegel = new User("machinbidule", DEFAULT_PASSWORD);
        User user1 = new User("user1", DEFAULT_PASSWORD);

        Community agileCommunity = new Community();
        agileCommunity.setName(STARTECH_AGILE);
        agileCommunity.setLeader(bgiegel);
        Community javascriptCommunity = new Community();
        javascriptCommunity.setName(STARTECH_JAVASCRIPT);
        javascriptCommunity.setLeader(bgiegel);
        Community javaCommunity = new Community();
        javaCommunity.setName(STARTECH_JAVA);
        javaCommunity.setLeader(user1);

        List<Community> communities = Arrays.asList(javaCommunity, agileCommunity, javascriptCommunity);

        // Initialize the database

        userRepository.saveAndFlush(bgiegel);
        userRepository.saveAndFlush(user1);
        communityRepository.save(communities);
        communityRepository.flush();
    }
}
