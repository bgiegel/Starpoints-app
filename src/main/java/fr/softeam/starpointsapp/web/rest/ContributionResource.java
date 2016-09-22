package fr.softeam.starpointsapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.softeam.starpointsapp.domain.Contribution;

import fr.softeam.starpointsapp.repository.ContributionRepository;
import fr.softeam.starpointsapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
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
 * REST controller for managing Contribution.
 */
@RestController
@RequestMapping("/api")
public class ContributionResource {

    private final Logger log = LoggerFactory.getLogger(ContributionResource.class);
        
    @Inject
    private ContributionRepository contributionRepository;

    /**
     * POST  /contributions : Create a new contribution.
     *
     * @param contribution the contribution to create
     * @return the ResponseEntity with status 201 (Created) and with body the new contribution, or with status 400 (Bad Request) if the contribution has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contributions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contribution> createContribution(@RequestBody Contribution contribution) throws URISyntaxException {
        log.debug("REST request to save Contribution : {}", contribution);
        if (contribution.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("contribution", "idexists", "A new contribution cannot already have an ID")).body(null);
        }
        Contribution result = contributionRepository.save(contribution);
        return ResponseEntity.created(new URI("/api/contributions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("contribution", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /contributions : Updates an existing contribution.
     *
     * @param contribution the contribution to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated contribution,
     * or with status 400 (Bad Request) if the contribution is not valid,
     * or with status 500 (Internal Server Error) if the contribution couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/contributions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contribution> updateContribution(@RequestBody Contribution contribution) throws URISyntaxException {
        log.debug("REST request to update Contribution : {}", contribution);
        if (contribution.getId() == null) {
            return createContribution(contribution);
        }
        Contribution result = contributionRepository.save(contribution);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("contribution", contribution.getId().toString()))
            .body(result);
    }

    /**
     * GET  /contributions : get all the contributions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of contributions in body
     */
    @RequestMapping(value = "/contributions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Contribution> getAllContributions() {
        log.debug("REST request to get all Contributions");
        List<Contribution> contributions = contributionRepository.findAll();
        return contributions;
    }

    /**
     * GET  /contributions/:id : get the "id" contribution.
     *
     * @param id the id of the contribution to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the contribution, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/contributions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Contribution> getContribution(@PathVariable Long id) {
        log.debug("REST request to get Contribution : {}", id);
        Contribution contribution = contributionRepository.findOne(id);
        return Optional.ofNullable(contribution)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /contributions/:id : delete the "id" contribution.
     *
     * @param id the id of the contribution to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/contributions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteContribution(@PathVariable Long id) {
        log.debug("REST request to delete Contribution : {}", id);
        contributionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contribution", id.toString())).build();
    }

}
