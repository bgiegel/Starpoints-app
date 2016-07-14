package fr.softeam.starpointsapp.repository;

import fr.softeam.starpointsapp.domain.Scale;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Scale entity.
 */
@SuppressWarnings("unused")
public interface ScaleRepository extends JpaRepository<Scale,Long> {

}
