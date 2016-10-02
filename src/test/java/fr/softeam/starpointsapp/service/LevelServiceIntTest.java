package fr.softeam.starpointsapp.service;

import fr.softeam.starpointsapp.StarPointsApp;
import fr.softeam.starpointsapp.domain.Activity;
import fr.softeam.starpointsapp.domain.Level;
import fr.softeam.starpointsapp.repository.ActivityRepository;
import fr.softeam.starpointsapp.repository.LevelRepository;
import fr.softeam.starpointsapp.util.ActivityBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = StarPointsApp.class)
@Transactional
public class LevelServiceIntTest {

    @Inject
    private LevelRepository levelRepository;

    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private LevelService levelService;

    private Level level;
    private Activity activity;

    @Test
    public void should_delete_level_and_update_activities() throws Exception {
        givenALevelWithRelatedActivity();

        //when
        levelService.delete(level.getId());

        assertThatLevelIsDeleted();
        assertThatRelatedActivityHasBeenUpdated();
    }

    private void givenALevelWithRelatedActivity() {
        buildLevel("level 1");

        activity = activityRepository.save(new ActivityBuilder().withName("activity 1").withLevel(level).build());
    }

    private void buildLevel(String name) {
        level = new Level();
        level.setName(name);
        level.setValue("1");

        levelRepository.save(level);
    }

    private void assertThatLevelIsDeleted() {
        Level level1 = levelRepository.findOne(level.getId());
        assertThat(level1).isNull();
    }


    private void assertThatRelatedActivityHasBeenUpdated() {
        Activity activity1 = activityRepository.findOne(activity.getId());
        assertThat(activity1).isNotNull();
        assertThat(activity1.getLevel()).isNull();
    }
}
