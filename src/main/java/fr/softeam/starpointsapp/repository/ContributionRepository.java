package fr.softeam.starpointsapp.repository;

import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Contribution entity.c
 */
public interface ContributionRepository extends JpaRepository<Contribution,Long> {

    @Query(value = "select distinct contribution from Contribution contribution order by contribution.deliverableDate desc",
        countQuery = "select count(contribution) from Contribution contribution")
    Page<Contribution> findAllContributions(Pageable pageable);

    @Query("select c from Contribution c left join fetch c.community community left join fetch community.members where c.id = :id")
    Contribution findOneWithCommunityMembers(@Param("id") Long id);

    @Query(value = "select c from Contribution c left join fetch c.community community where community.leader.login = :leader",
        countQuery = "select count(c) from Contribution c left join c.community community where community.leader.login = :leader")
    Page<Contribution> findAllFromCommunitiesLeadedBy(@Param("leader") String leader, Pageable pageable);

    @Query("select c from Contribution c join c.activity activity where activity.id = :activityId")
    Page<Contribution> findAllForAnActivity(@Param("activityId") Long id, Pageable pageable);

    @Query("select c from Contribution c join c.community community where community.id = :communityId")
    Page<Contribution> findAllForACommunity(@Param("communityId") Long id, Pageable pageable);

    @Query("select c from Contribution c where c.author.login= :login order by c.deliverableDate desc")
    Page<Contribution> findAllFromAnAuthor(@Param("login") String login, Pageable pageable);

    @Query("select c " +
        "from Contribution c " +
        "where c.author.login= :login " +
        "and month(c.deliverableDate) <= :endMonth " +
        "and month(c.deliverableDate) >= :startMonth " +
        "and year(c.deliverableDate) = :year " +
        "order by c.deliverableDate desc")
    Page<Contribution> findAllFromAnAuthorByQuarter(@Param("login") String login, @Param("startMonth") Integer startMonth, @Param("endMonth") Integer endMonth, @Param("year") Integer year, Pageable pageable);

    @Query("select c " +
        "from Contribution c " +
        "where month(c.deliverableDate) <= :endMonth " +
        "and month(c.deliverableDate) >= :startMonth " +
        "and year(c.deliverableDate) = :year " +
        "order by c.deliverableDate desc")
    Page<Contribution> findAllByQuarter(@Param("startMonth") Integer startMonth, @Param("endMonth") Integer endMonth, @Param("year") Integer year, Pageable pageable);
}
