package fr.softeam.starpointsapp.service;

import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.repository.ContributionRepository;
import fr.softeam.starpointsapp.service.dto.QuarterDTO;
import fr.softeam.starpointsapp.service.util.QuarterUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Service
@Transactional
public class ContributionService {


    @Inject
    private ContributionRepository contributionRepository;


    /**
     * Récupère les contributions par trimestre d'un utilisateur.
     */
    public Page<Contribution> getUserContributionsByQuarter(String quarter, String login, Pageable pageable) {

        QuarterDTO quarterDTO = QuarterUtil.convertToQuarterDTO(quarter);
        return contributionRepository.findAllFromUserByQuarter(login, quarterDTO.getStartMonth(), quarterDTO.getEndMonth(), quarterDTO.getYear(), pageable);
    }

}
