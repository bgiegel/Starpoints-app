package fr.softeam.starpointsapp.service;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.domain.Community;
import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.domain.PersistentToken;
import fr.softeam.starpointsapp.domain.User;
import fr.softeam.starpointsapp.repository.CommunityRepository;
import fr.softeam.starpointsapp.repository.ContributionRepository;
import fr.softeam.starpointsapp.repository.PersistentTokenRepository;
import fr.softeam.starpointsapp.repository.UserRepository;
import fr.softeam.starpointsapp.service.exception.LeadersCannotBeDeletedException;
import fr.softeam.starpointsapp.service.util.RandomUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarPointsApp.class)
@Transactional
public class UserServiceIntTest {

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private CommunityRepository communityRepository;

    @Inject
    private ContributionRepository contributionRepository;

    private Community community;
    private Contribution contribution;

    @Test
    public void testRemoveOldPersistentTokens() {
        User admin = userRepository.findOneByLogin("admin").get();
        int existingCount = persistentTokenRepository.findByUser(admin).size();
        generateUserToken(admin, "1111-1111", LocalDate.now());
        LocalDate now = LocalDate.now();
        generateUserToken(admin, "2222-2222", now.minusDays(32));
        assertThat(persistentTokenRepository.findByUser(admin)).hasSize(existingCount + 2);
        userService.removeOldPersistentTokens();
        assertThat(persistentTokenRepository.findByUser(admin)).hasSize(existingCount + 1);
    }

    @Test
    public void assertThatUserMustExistToResetPassword() {
        Optional<User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
        assertThat(maybeUser.isPresent()).isFalse();

        maybeUser = userService.requestPasswordReset("admin@localhost");
        assertThat(maybeUser.isPresent()).isTrue();

        assertThat(maybeUser.get().getEmail()).isEqualTo("admin@localhost");
        assertThat(maybeUser.get().getResetDate()).isNotNull();
        assertThat(maybeUser.get().getResetKey()).isNotNull();
    }

    @Test
    public void assertThatOnlyActivatedUserCanRequestPasswordReset() {
        User user = userService.createUser("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US", LocalDate.of(2016, 8, 9));
        Optional<User> maybeUser = userService.requestPasswordReset("john.doe@localhost");
        assertThat(maybeUser.isPresent()).isFalse();
        userRepository.delete(user);
    }

    @Test
    public void assertThatResetKeyMustNotBeOlderThan24Hours() {
        User user = userService.createUser("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US", LocalDate.of(2016, 8, 9));

        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(25);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);

        userRepository.save(user);

        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());

        assertThat(maybeUser.isPresent()).isFalse();

        userRepository.delete(user);
    }

    @Test
    public void assertThatResetKeyMustBeValid() {
        User user = userService.createUser("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US", LocalDate.of(2016, 8, 9));

        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(25);
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey("1234");
        userRepository.save(user);
        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser.isPresent()).isFalse();
        userRepository.delete(user);
    }

    @Test
    public void assertThatUserCanResetPassword() {
        User user = userService.createUser("johndoe", "johndoe", "John", "Doe", "john.doe@localhost", "en-US", LocalDate.of(2016,8,9));
        String oldPassword = user.getPassword();
        ZonedDateTime daysAgo = ZonedDateTime.now().minusHours(2);
        String resetKey = RandomUtil.generateResetKey();
        user.setActivated(true);
        user.setResetDate(daysAgo);
        user.setResetKey(resetKey);
        userRepository.save(user);
        Optional<User> maybeUser = userService.completePasswordReset("johndoe2", user.getResetKey());
        assertThat(maybeUser.isPresent()).isTrue();
        assertThat(maybeUser.get().getResetDate()).isNull();
        assertThat(maybeUser.get().getResetKey()).isNull();
        assertThat(maybeUser.get().getPassword()).isNotEqualTo(oldPassword);

        userRepository.delete(user);
    }

    @Test
    public void should_remove_contributions_and_link_to_communities_on_user_deletion() throws LeadersCannotBeDeletedException {
        given_a_member_of_community_that_have_made_contributions();

        //when
        userService.deleteUser("johndoe");

        assertThatUserHasBeenDeleted();
        assertThatUserHasBeenRemovedFromCommunityMembersList();
        assertThatContributionHasBeenRemoved();
    }

    @Test(expected = LeadersCannotBeDeletedException.class)
    public void should_not_remove_leader_of_community() throws LeadersCannotBeDeletedException {
        given_a_member_of_community_which_is_also_the_leader();

        //when
        userService.deleteUser("johndoe");
    }

    private void given_a_member_of_community_that_have_made_contributions() {
        User user = buildUserAndCommunity();

        contribution = new Contribution();
        contribution.setAuthor(user);
        contribution.setDeliverableName("Livrable contribution");

        user.getContributions().add(contribution);

        community = communityRepository.save(community);
        contribution = contributionRepository.save(contribution);
        communityRepository.flush();
        contributionRepository.flush();
        userRepository.save(user);
        userRepository.flush();
    }

    private User buildUserAndCommunity() {
        User user = userService.createUser("johndoe", "johndoe", "John", "Doe",
            "john.doe@localhost", "en-US", LocalDate.of(2016, 8, 9));

        community = new Community();
        community.setName("community1");
        community.getMembers().add(user);

        user.getCommunities().add(community);

        return user;
    }

    private void given_a_member_of_community_which_is_also_the_leader() {
        User user = buildUserAndCommunity();
        community.setLeader(user);
        communityRepository.save(community);
        communityRepository.flush();
    }

    private void assertThatContributionHasBeenRemoved() {
        Contribution contributionResult = contributionRepository.findOne(contribution.getId());
        assertThat(contributionResult).isNull();
    }

    private void assertThatUserHasBeenRemovedFromCommunityMembersList() {
        Community communityResult = communityRepository.findOne(community.getId());
        assertThat(communityResult.getMembers()).isEmpty();
    }

    private void assertThatUserHasBeenDeleted() {
        Optional<User> johndoe = userRepository.findOneByLogin("johndoe");
        assertFalse(johndoe.isPresent());
    }

    @Test
    public void testFindNotActivatedUsersByCreationDateBefore() {
        userService.removeNotActivatedUsers();
        ZonedDateTime now = ZonedDateTime.now();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        assertThat(users).isEmpty();
    }

    private void generateUserToken(User user, String tokenSeries, LocalDate localDate) {
        PersistentToken token = new PersistentToken();
        token.setSeries(tokenSeries);
        token.setUser(user);
        token.setTokenValue(tokenSeries + "-data");
        token.setTokenDate(localDate);
        token.setIpAddress("127.0.0.1");
        token.setUserAgent("Test agent");
        persistentTokenRepository.saveAndFlush(token);
    }
}
