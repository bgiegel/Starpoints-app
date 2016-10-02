package fr.softeam.starpointsapp.service;

import fr.softeam.starpointsapp.domain.Activity;
import fr.softeam.starpointsapp.repository.ActivityRepository;
import fr.softeam.starpointsapp.repository.LevelRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.List;

@Service
@Transactional
public class LevelService {

    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private LevelRepository levelRepository;

    public void delete(Long id) {
        List<Activity> activities = activityRepository.findAllActivitiesForALevel(id);
        activities.forEach(activity -> {
            activity.setLevel(null);
            activityRepository.save(activity);
        });

        levelRepository.delete(id);
    }
}
