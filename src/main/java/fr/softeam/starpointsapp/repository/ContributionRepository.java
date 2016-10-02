package fr.softeam.starpointsapp.repository;

import fr.softeam.starpointsapp.domain.Contribution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    @Query("select contribution from Contribution contribution join contribution.activity activity where activity.id = :activityId")
    Page<Contribution> findAllContributionForAnActivity(@Param("activityId") Long id, Pageable pageable);

    @Query("select contribution from Contribution contribution join contribution.community community where community.id = :communityId")
    Page<Contribution> findAllContributionForACommunity(@Param("communityId") Long id, Pageable pageable);
}
