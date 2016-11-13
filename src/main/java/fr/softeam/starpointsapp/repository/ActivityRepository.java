package fr.softeam.starpointsapp.repository;

import fr.softeam.starpointsapp.domain.Activity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Activity entity.
 */
public interface ActivityRepository extends JpaRepository<Activity,Long> {

    @Query("from Activity activity where activity.level.id = :levelId")
    List<Activity> findAllActivitiesForALevel(@Param("levelId") Long id);

    @Query("select distinct activity from Activity activity")
    Page<Activity> findAllActivities(Pageable pageable);
}
