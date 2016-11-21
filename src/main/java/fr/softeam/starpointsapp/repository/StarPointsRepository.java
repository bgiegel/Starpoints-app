package fr.softeam.starpointsapp.repository;

import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.service.dto.StarPointsByCommunityDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Scale entity.
 */
public interface StarPointsRepository extends JpaRepository<Contribution, Long> {


    @Query("select new fr.softeam.starpointsapp.service.dto.StarPointsByCommunityDTO(com.name, sum(s.value)) " +
        "from Scale s, Contribution c " +
        "join c.community com " +
        "where s.activity=c.activity " +
        "and c.author.id = :userId " +
        "and s.startDate <= c.deliverableDate " +
        "and (s.endDate >= c.deliverableDate or s.endDate is null) " +
        "and com.id in ( " +
            "select community.id from User user " +
            "join user.communities community " +
            "where user.id = c.author" +
        " ) " +
        "group by com.name")
    List<StarPointsByCommunityDTO> calculateStarPointsByCommunityForUser(@Param("userId") Long userId);

    @Query("select new fr.softeam.starpointsapp.service.dto.StarPointsByCommunityDTO(com.name, sum(s.value)) " +
        "from Scale s, Contribution c " +
        "join c.community com " +
        "where s.activity=c.activity " +
        "and s.startDate <= c.deliverableDate " +
        "and (s.endDate >= c.deliverableDate or s.endDate is null) " +
        "and com.id in ( " +
            "select community.id from User user " +
            "join user.communities community " +
        " ) " +
        "group by com.name")
    List<StarPointsByCommunityDTO> calculateStarPointsByCommunity();

    @Query("select new fr.softeam.starpointsapp.service.dto.StarPointsByCommunityDTO(com.name, sum(s.value)) " +
        "from Scale s, Contribution c " +
        "join c.community com " +
        "where s.activity=c.activity " +
        "and s.startDate <= c.deliverableDate " +
        "and (s.endDate >= c.deliverableDate or s.endDate is null) " +
        "and com.id in ( " +
            "select community.id from User user " +
            "join user.communities community " +
            "where community.leader.id = :leaderId" +
        " ) " +
        "group by com.name")
    List<StarPointsByCommunityDTO> calculateStarPointsByCommunityLeadedBy(@Param("leaderId") Long leaderId);
}
