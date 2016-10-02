package fr.softeam.starpointsapp.service;

import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.domain.Scale;
import fr.softeam.starpointsapp.repository.ActivityRepository;
import fr.softeam.starpointsapp.repository.ContributionRepository;
import fr.softeam.starpointsapp.repository.ScaleRepository;
import fr.softeam.starpointsapp.service.exception.ActivityReferencedByContributionsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.Optional;

@Service
@Transactional
public class ActivityService {

    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 1;
    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private ScaleRepository scaleRepository;

    @Inject
    private ContributionRepository contributionRepository;


    /**
     * Supprime une activité et son barème si il y en a un.
     * Dans le cas ou des contributions sont rattachées à cette activité on renvoit une exception.
     * @param id de l'activité a supprimer
     */
    public void deleteActivity(Long id) throws ActivityReferencedByContributionsException {
        Page<Contribution> contributions = contributionRepository.findAllContributionForAnActivity(id, new PageRequest(PAGE_NUMBER, PAGE_SIZE));
        if (contributions.hasContent()){
            throw new ActivityReferencedByContributionsException();
        }

        // on vérifie d'abord si un barème est associé a l'activité
        Optional<Scale> scale = scaleRepository.findScaleFromActivityId(id);
        if (scale.isPresent()){
            scaleRepository.delete(scale.get().getId());
        }

        activityRepository.delete(id);
    }

}
