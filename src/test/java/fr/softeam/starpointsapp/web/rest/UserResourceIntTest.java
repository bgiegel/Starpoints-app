package fr.softeam.starpointsapp.web.rest;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.domain.User;
import fr.softeam.starpointsapp.repository.UserRepository;
import fr.softeam.starpointsapp.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarPointsApp.class)
public class UserResourceIntTest {

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserMockMvc;

    /**
     * Create a User.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which has a required relationship to the User entity.
     */
    public static User createEntity(EntityManager em) {
        User user = new User();
        user.setLogin("test");
        user.setPassword(RandomStringUtils.random(60));
        user.setActivated(true);
        user.setEmail("test@test.com");
        user.setFirstName("test");
        user.setLastName("test");
        user.setLangKey("en");
        em.persist(user);
        em.flush();
        return user;
    }

    @Before
    public void setup() {
        UserResource userResource = new UserResource();
        ReflectionTestUtils.setField(userResource, "userRepository", userRepository);
        ReflectionTestUtils.setField(userResource, "userService", userService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .build();
    }

    @Test
    public void testGetExistingUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/admin")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.lastName").value("Administrator"));
    }

    @Test
    public void testGetAllUsersNames() throws Exception {
        restUserMockMvc.perform(get("/api/users/names")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$[0].lastName").exists())
            .andExpect(jsonPath("$[0].firstName").exists())
            .andExpect(jsonPath("$[0].login").exists())
            .andExpect(jsonPath("$[0].id").exists())
            .andExpect(jsonPath("$[0].email").doesNotExist()).
            andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetMembersOfCommunitiesLeadedBy() throws Exception {
        restUserMockMvc.perform(get("/api/members-of-communities-leaded-by/bgiegel")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE));
    }

    @Test
    public void testGetUnknownUser() throws Exception {
        restUserMockMvc.perform(get("/api/users/unknown")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteUser() throws Exception {
        restUserMockMvc.perform(delete("/api/users/bgiegel")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    public void testDeleteUserWhoIsLeader() throws Exception {
        restUserMockMvc.perform(delete("/api/users/skhattab")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }
}
