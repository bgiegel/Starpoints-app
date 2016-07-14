package fr.softeam.starpointsapp.repository;

import fr.softeam.starpointsapp.domain.Community;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Community entity.
 */
@SuppressWarnings("unused")
public interface CommunityRepository extends JpaRepository<Community,Long> {

    @Query("select distinct community from Community community left join fetch community.members")
    List<Community> findAllWithEagerRelationships();

    @Query("select community from Community community left join fetch community.members where community.id =:id")
    Community findOneWithEagerRelationships(@Param("id") Long id);

}
