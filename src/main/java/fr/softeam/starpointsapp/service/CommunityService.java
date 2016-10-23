package fr.softeam.starpointsapp.service;

import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.repository.CommunityRepository;
import fr.softeam.starpointsapp.repository.ContributionRepository;
import fr.softeam.starpointsapp.service.exception.CommunityReferencedByContributionsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class CommunityService {

    public static final int PAGE_NUMBER = 0;
    public static final int PAGE_SIZE = 1;
    @Inject
    private ContributionRepository contributionRepository;

    @Inject
    private CommunityRepository communityRepository;

    public void delete(Long id) throws CommunityReferencedByContributionsException {
        Page<Contribution> contributions = contributionRepository.findAllForACommunity(id, new PageRequest(PAGE_NUMBER, PAGE_SIZE));
        if (contributions.hasContent()){
            throw new CommunityReferencedByContributionsException();
        }

        communityRepository.delete(id);
    }
}
