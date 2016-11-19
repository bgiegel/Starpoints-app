package fr.softeam.starpointsapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.softeam.starpointsapp.repository.StarPointsRepository;
import fr.softeam.starpointsapp.security.AuthoritiesConstants;
import fr.softeam.starpointsapp.service.dto.StarPointsByCommunityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.List;

import static fr.softeam.starpointsapp.service.util.StarpointsUtil.buildStarPointsByCommunityDTO;

/**
 * REST controller for managing Scale.
 */
@RestController
@RequestMapping("/api")
public class StarpointsResource {

    private final Logger log = LoggerFactory.getLogger(StarpointsResource.class);

    @Inject
    private StarPointsRepository starPointsRepository;

    /**
     * GET  /starpoints/by-community/{userId} : Récupère tous les starpoints par communauté de l'utilisateur
     * demandé.
     */
    @RequestMapping(value = "/starpoints/by-community/{userId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.USER, AuthoritiesConstants.LEADER, AuthoritiesConstants.ADMIN})
    public List<StarPointsByCommunityDTO> getStarPointsByCommunity(@PathVariable Long userId) {
        log.debug("REST request to get all Scales");
        List<Object[]> results = starPointsRepository.calculateStarPointsByCommunityForUser(userId);
        return buildStarPointsByCommunityDTO(results);
    }

    /**
     * GET  /starpoints/by-community/leaded-by/{leader} : Récupère tous les starpoints des communautés dirigé par le leader
     * demandé.
     */
    @RequestMapping(value = "/starpoints/by-community/leaded-by/{leader}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.LEADER, AuthoritiesConstants.ADMIN})
    public List<StarPointsByCommunityDTO> getStarPointsByCommunityLeadedBy(@PathVariable Long leader) {
        log.debug("REST request to get all Scales");
        List<Object[]> results = starPointsRepository.calculateStarPointsByCommunityLeadedBy(leader);
        return buildStarPointsByCommunityDTO(results);
    }

    /**
     * GET  /starpoints/by-community : Récupère tous les starpoints par communauté de tous les utilisateurs.
     */
    @RequestMapping(value = "/starpoints/by-community",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public List<StarPointsByCommunityDTO> getStarPointsByCommunity() {
        log.debug("REST request to get all Scales");
        List<Object[]> results = starPointsRepository.calculateStarPointsByCommunity();
        return buildStarPointsByCommunityDTO(results);
    }

}
