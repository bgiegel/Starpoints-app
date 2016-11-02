package fr.softeam.starpointsapp.service;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.domain.*;
import fr.softeam.starpointsapp.repository.*;
import fr.softeam.starpointsapp.service.exception.ActivityReferencedByContributionsException;
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

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarPointsApp.class)
@Transactional
public class ActivityServiceIntTest {

    @Inject
    private ActivityService activityService;

    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private ScaleRepository scaleRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private CommunityRepository communityRepository;

    @Inject
    private ContributionRepository contributionRepository;



    private Activity activity;
    private Scale scale;

    @Test
    public void should_delete_activity_and_related_scale() throws ActivityReferencedByContributionsException {
        givenAnActivityAndScale();

        //when
        activityService.deleteActivity(activity.getId());

        assertThatScaleIsDeleted();
        assertThatActivityIsDeleted();
    }

    @Test(expected = ActivityReferencedByContributionsException.class)
    public void should_not_delete_activity_when_related_contributions_are_found() throws ActivityReferencedByContributionsException {
        givenAnActivityWithRelatedContributions();

        //when
        activityService.deleteActivity(activity.getId());
    }

    private void givenAnActivityAndScale() {
        activity = new ActivityBuilder().withName("activity with Scale").build();
        activityRepository.save(activity);

        buildScale(100);
    }

    private void givenAnActivityWithRelatedContributions() {
        activity = new ActivityBuilder().withName("activity with Contributions").build();
        User leader = new UserBuilder("leader").build();
        Community community = new CommunityBuilder(leader).build();
        User author = new UserBuilder("author").build();
        Contribution contribution = new ContributionBuilder(activity, community, author).withDeliverableName("contrib 1").build();

        userRepository.save(leader);
        userRepository.save(author);
        communityRepository.save(community);
        activityRepository.save(activity);
        contributionRepository.save(contribution);
    }

    private void assertThatScaleIsDeleted() {
        Scale deletedScale = scaleRepository.findOne(scale.getId());
        assertThat(deletedScale).isNull();
    }

    private void assertThatActivityIsDeleted() {
        Activity deletedActivity = activityRepository.findOne(activity.getId());
        assertThat(deletedActivity).isNull();
    }

    private void buildScale(Integer value) {
        scale = new Scale();
        scale.setStartDate(LocalDate.of(2016,1,1));
        scale.setValue(value);
        scale.setActivity(activity);

        scale = scaleRepository.save(scale);
    }

}
