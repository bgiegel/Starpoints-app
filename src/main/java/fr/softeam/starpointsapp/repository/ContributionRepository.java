package fr.softeam.starpointsapp.repository;

import fr.softeam.starpointsapp.domain.Contribution;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Contribution entity.
 */
@SuppressWarnings("unused")
public interface ContributionRepository extends JpaRepository<Contribution,Long> {

    @Query("select contribution from Contribution contribution left join fetch contribution.community c left join fetch c.members where contribution.id = :id")
    Contribution findOneWithCommunityMembers(@Param("id") Long id);

    @Query("select contribution from Contribution contribution left join fetch contribution.community c where c.leader.login = :leader")
    List<Contribution> findAllFromCommunitiesLeadedBy(@Param("leader") String leader);
}
