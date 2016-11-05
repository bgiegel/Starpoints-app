package fr.softeam.starpointsapp.repository;

import fr.softeam.starpointsapp.domain.Contribution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Scale entity.
 */
public interface StarPointsRepository extends JpaRepository<Contribution, Long> {


    @Query("select authorCommunity.name as community, " +
        "(" +
        "select sum(s.value) " +
        "from Scale s, Contribution c " +
        "join s.activity sActivity " +
        "join c.activity cActivity " +
        "join c.author author " +
        "join c.community com " +
        "where author.id=comMember.id " +
        "and com.id=authorCommunity " +
        "and sActivity=cActivity" +
        ") as starpoints " +
        "from Community authorCommunity " +
        "join authorCommunity.members comMember " +
        "where comMember.id= :userId " +
        "group by authorCommunity.name")
    List<Object[]> calculateStarPointsByCommunityForUser(@Param("userId") Long userId);

    @Query("select community.name as community, " +
        "(" +
        "select sum(s.value) " +
        "from Scale s, Contribution c " +
        "join s.activity sActivity " +
        "join c.activity cActivity " +
        "join c.community com " +
        "where com.id=community.id " +
        "and sActivity=cActivity" +
        ") as starpoints " +
        "from Community community " +
        "group by community.name")
    List<Object[]> calculateStarPointsByCommunity();

    @Query("select community.name as community, " +
        "(" +
            "select sum(s.value) " +
            "from Scale s, Contribution c " +
            "join s.activity sActivity " +
            "join c.activity cActivity " +
            "join c.community com " +
            "where com.id=community.id " +
            "and sActivity=cActivity" +
        ") as starpoints " +
        "from Community community " +
        "where community.leader.id= :leaderId " +
        "group by community.name")
    List<Object[]> calculateStarPointsByCommunityLeadedBy(@Param("leaderId") Long leaderId);
}
