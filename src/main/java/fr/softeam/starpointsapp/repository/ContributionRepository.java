package fr.softeam.starpointsapp.repository;

import fr.softeam.starpointsapp.domain.Contribution;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Contribution entity.
 */
@SuppressWarnings("unused")
public interface ContributionRepository extends JpaRepository<Contribution,Long> {

}
