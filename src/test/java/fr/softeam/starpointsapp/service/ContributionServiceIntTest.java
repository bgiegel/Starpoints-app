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
import fr.softeam.starpointsapp.util.ActivityBuilder;
import fr.softeam.starpointsapp.util.CommunityBuilder;
import fr.softeam.starpointsapp.util.ContributionBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarPointsApp.class)
@Transactional
public class ContributionServiceIntTest {

    public static final String Q3_2016 = "Q3-2016";
    public static final String DEFAULT_LOGIN = "toto";
    private static final String DEFAULT_PASSWORD = "$2a$10$qMNi7ijFejR1n8yP0750IuDnrMeQ.p67PjiHuEZgV0VTW939S5Zbq";
    public static final PageRequest PAGE_REQUEST = new PageRequest(0, 10);

    @Inject
    private UserRepository userRepository;

    @Inject
    private CommunityRepository communityRepository;

    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private ContributionRepository contributionRepository;

    @Inject
    private ContributionService contributionService;

    private Contribution julyContrib;
    private Contribution augustContrib;
    private Contribution septemberContrib;

    private User user, user2;

    @Test
    public void should_get_all_user_contribution_of_a_quarter(){
        givenAUserWithSeveralContributions();

        //when
        Page<Contribution> contributions = contributionService.getUserContributionsByQuarter(Q3_2016, DEFAULT_LOGIN, PAGE_REQUEST);

        assertThat(contributions.getContent()).containsExactly(septemberContrib,augustContrib,julyContrib);
    }

    @Test
    public void should_return_empty_list_when_no_contributions_is_found(){
        givenNoContributionsAssignedToUser();

        //when
        Page<Contribution> contributions = contributionService.getUserContributionsByQuarter(Q3_2016, DEFAULT_LOGIN, PAGE_REQUEST);

        assertThat(contributions.getContent()).hasSize(0);
    }

    private void givenNoContributionsAssignedToUser() {
        buildUsers();
    }

    private void givenAUserWithSeveralContributions() {
        buildUsers();
        buildContributions();
    }

    private void buildUsers() {
        user = new User(DEFAULT_LOGIN, DEFAULT_PASSWORD);
        user2 = new User("tata", DEFAULT_PASSWORD);

        userRepository.save(user);
        userRepository.save(user2);
    }

    private void buildContributions() {
        Activity activity = new ActivityBuilder().build();
        Community community = new CommunityBuilder(user2).build();


        julyContrib = new ContributionBuilder(activity, community, user).
            withDeliverableName("july contribution").
            withDeliverableDate(LocalDate.of(2016, 7, 1)).
            build();
        Contribution july2015Contrib = new ContributionBuilder(activity, community, user).
            withDeliverableName("july 2015 contribution").
            withDeliverableDate(LocalDate.of(2015, 7, 1)).
            build();
        augustContrib = new ContributionBuilder(activity, community, user).
            withDeliverableName("august contribution").
            withDeliverableDate(LocalDate.of(2016, 8, 31)).
            build();
        septemberContrib = new ContributionBuilder(activity, community, user).
            withDeliverableName("september contribution").
            withDeliverableDate(LocalDate.of(2016,9,1)).
            build();
        Contribution juneContrib = new ContributionBuilder(activity, community, user).
            withDeliverableName("june contribution").
            withDeliverableDate(LocalDate.of(2016, 6, 30)).
            build();
        Contribution user2Contribution = new ContributionBuilder(activity, community, user2).
            withDeliverableName("Not concerned user contribution").
            withDeliverableDate(LocalDate.of(2016,7,1)).
            build();

        activityRepository.save(activity);
        communityRepository.save(community);

        List<Contribution> contributions = Arrays.asList(julyContrib, july2015Contrib, septemberContrib, augustContrib, juneContrib, user2Contribution);
        contributionRepository.save(contributions);
    }
}
