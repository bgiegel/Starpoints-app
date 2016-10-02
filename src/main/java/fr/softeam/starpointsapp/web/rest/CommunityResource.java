package fr.softeam.starpointsapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.softeam.starpointsapp.domain.Community;
import fr.softeam.starpointsapp.repository.CommunityRepository;
import fr.softeam.starpointsapp.service.CommunityService;
import fr.softeam.starpointsapp.service.exception.CommunityReferencedByContributionsException;
import fr.softeam.starpointsapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Community.
 */
@RestController
@RequestMapping("/api")
public class CommunityResource {

    private final Logger log = LoggerFactory.getLogger(CommunityResource.class);

    @Inject
    private CommunityRepository communityRepository;

    @Inject
    private CommunityService communityService;

    /**
     * POST  /communities : Create a new community.
     *
     * @param community the community to create
     * @return the ResponseEntity with status 201 (Created) and with body the new community, or with status 400 (Bad Request) if the community has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/communities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Community> createCommunity(@RequestBody Community community) throws URISyntaxException {
        log.debug("REST request to save Community : {}", community);
        if (community.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("community", "idexists", "A new community cannot already have an ID")).body(null);
        }
        Community result = communityRepository.save(community);
        return ResponseEntity.created(new URI("/api/communities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("community", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /communities : Updates an existing community.
     *
     * @param community the community to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated community,
     * or with status 400 (Bad Request) if the community is not valid,
     * or with status 500 (Internal Server Error) if the community couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/communities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Community> updateCommunity(@RequestBody Community community) throws URISyntaxException {
        log.debug("REST request to update Community : {}", community);
        if (community.getId() == null) {
            return createCommunity(community);
        }
        Community result = communityRepository.save(community);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("community", community.getId().toString()))
            .body(result);
    }

    /**
     * GET  /communities : get all the communities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of communities in body
     */
    @RequestMapping(value = "/communities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Community> getAllCommunities() {
        log.debug("REST request to get all Communities");
        return communityRepository.findAllWithEagerRelationships();
    }

    /**
     * GET  /communities/:id : get the "id" community.
     *
     * @param id the id of the community to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the community, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/communities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Community> getCommunity(@PathVariable Long id) {
        log.debug("REST request to get Community : {}", id);
        Community community = communityRepository.findOneWithEagerRelationships(id);
        return Optional.ofNullable(community)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * GET  /communities-leaded-by/:user : Récupère la liste des commmunautés dont l'utilisateur en paramètre est le leader.
     *
     * @param user Le login de l'utilisateur
     * @return the ResponseEntity with status 200 (OK) and with body the community, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/communities-leaded-by/{user}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Community> getCommunitiesLeadedBy(@PathVariable String user) {
        log.debug("REST request to get Community leaded by: {}", user);
        return communityRepository.findCommunitiesLeadedBy(user);
    }

    /**
     * DELETE  /communities/:id : delete the "id" community.
     *
     * @param id the id of the community to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/communities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id) {
        log.debug("REST request to delete Community : {}", id);
        try {
            communityService.delete(id);
        } catch (CommunityReferencedByContributionsException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(
                "communityWithRelatedContributionsDeletionImpossible",
                "Impossible de supprimer une communauté rattachée à une ou des contributions.")).build();
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("community", id.toString())).build();
    }

}
