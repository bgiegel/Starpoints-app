package fr.softeam.starpointsapp.service;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.domain.Activity;
import fr.softeam.starpointsapp.domain.Community;
import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.domain.User;
import fr.softeam.starpointsapp.repository.ActivityRepository;
import fr.softeam.starpointsapp.repository.CommunityRepository;
import fr.softeam.starpointsapp.repository.ContributionRepository;
import fr.softeam.starpointsapp.repository.UserRepository;
import fr.softeam.starpointsapp.service.exception.CommunityReferencedByContributionsException;
import fr.softeam.starpointsapp.util.ActivityBuilder;
import fr.softeam.starpointsapp.util.CommunityBuilder;
import fr.softeam.starpointsapp.util.ContributionBuilder;
import fr.softeam.starpointsapp.util.UserBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the CommunityResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarPointsApp.class)
@Transactional
public class CommunityServiceIntTest {

    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private CommunityRepository communityRepository;

    @Inject
    private ContributionRepository contributionRepository;

    @Inject
    private CommunityService communityService;

    private Community community;

    @Test
    public void should_delete_activity() throws CommunityReferencedByContributionsException {
        givenACommunity();

        //when
        communityService.delete(community.getId());

        assertThatCommunityIsDeleted();
    }

    @Test(expected = CommunityReferencedByContributionsException.class)
    public void should_not_delete_community_when_related_contributions_are_found() throws CommunityReferencedByContributionsException {
        givenACommunityWithRelatedContributions();

        //when
        communityService.delete(community.getId());
    }

    private void givenACommunity() {
        buildCommunity("communauté");
    }

    private void givenACommunityWithRelatedContributions() {
        buildCommunity("communauté avec contributions");

        User user = new UserBuilder("contributionAuthor").build();
        Activity activity = new ActivityBuilder().build();
        Contribution contribution = new ContributionBuilder(activity, community, user).build();

        userRepository.save(user);
        activityRepository.save(activity);
        contributionRepository.save(contribution);
    }

    private void buildCommunity(String name) {
        User user = new UserBuilder("communityLeader").build();
        community = new CommunityBuilder(user).withName(name).build();

        userRepository.save(user);
        communityRepository.save(community);
    }

    private void assertThatCommunityIsDeleted() {
        Community deletedCommunity = communityRepository.findOne(community.getId());
        assertThat(deletedCommunity).isNull();
    }
}
