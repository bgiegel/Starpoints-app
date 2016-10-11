package fr.softeam.starpointsapp.repository;

import fr.softeam.starpointsapp.domain.Scale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/**
 * Spring Data JPA repository for the Scale entity.
 */
public interface ScaleRepository extends JpaRepository<Scale,Long> {

    @Query("select scale from Scale scale join fetch scale.activity activity where activity.id= :activityId")
    Optional<Scale> findScaleFromActivityId(@Param("activityId") Long activityId);

}
