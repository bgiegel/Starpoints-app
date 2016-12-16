package fr.softeam.starpointsapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.softeam.starpointsapp.domain.Contribution;
import fr.softeam.starpointsapp.repository.ContributionRepository;
import fr.softeam.starpointsapp.security.AuthoritiesConstants;
import fr.softeam.starpointsapp.service.ContributionService;
import fr.softeam.starpointsapp.service.exception.QuarterFormatException;
import fr.softeam.starpointsapp.web.rest.util.HeaderUtil;
import fr.softeam.starpointsapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
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

    @Inject
    private ContributionService contributionService;

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
    @Secured({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN, AuthoritiesConstants.LEADER})
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
    @Secured({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN, AuthoritiesConstants.LEADER})
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
    public ResponseEntity<?> getAllContributions(Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get all Contributions");
        Page page = contributionRepository.findAllContributions(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contributions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /contributions/author/{login} : Récupère toutes les contributions créée par un utilisateur.
     *
     */
    @RequestMapping(value = "/contributions/author/{login}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<?> getAllContributionsFromAnAuthor(@PathVariable String login, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to getAllContributionsFromAnAuthor");
        Page<Contribution> page = contributionRepository.findAllFromAnAuthor(login, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contributions/author");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  contributions-from-communities-leaded-by/:leader : Récupère toutes les contributions des communautés que dirige le leader passé en paramètre.
     *
     */
    @RequestMapping(value = "/contributions-from-communities-leaded-by/{leader}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.LEADER})
    public ResponseEntity<?> getContributionsFromLeaderCommunities(@PathVariable String leader, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to getContributionsFromLeaderCommunities");
        Page page = contributionRepository.findAllFromCommunitiesLeadedBy(leader, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contributions-from-communities-leaded-by");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  contributions-by-quarter/:quarter/:login : Récupère toutes les contributions par trimestre d'un utilisateur.
     *
     */
    @RequestMapping(value = "/contributions-by-quarter/{quarter}/{login}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<?> getUserContributionsByQuarter(@PathVariable String quarter, @PathVariable String login, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get all Contributions");

        Page<Contribution> page;
        try {
            page = contributionService.getUserContributionsByQuarter(quarter, login, pageable);
        } catch (QuarterFormatException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(
                "wrongQuarterFormat",
                "Le format du trimestre est incorrect. Ex: Q3-2016")).build();
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contributions-by-quarter");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  contributions-by-quarter/:quarter : Récupère toutes les contributions par trimestre.
     *
     */
    @RequestMapping(value = "/contributions-by-quarter/{quarter}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<?> getContributionsByQuarter(@PathVariable String quarter, Pageable pageable) throws URISyntaxException {
        log.debug("REST request to get all Contributions");

        Page<Contribution> page;
        try {
            page = contributionService.getContributionsByQuarter(quarter, pageable);
        } catch (QuarterFormatException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(
                "wrongQuarterFormat",
                "Le format du trimestre est incorrect. Ex: Q3-2016")).build();
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/contributions-by-quarter");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
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
    @Secured(AuthoritiesConstants.USER)
    public ResponseEntity<Contribution> getContribution(@PathVariable Long id) {
        log.debug("REST request to get Contribution : {}", id);
        Contribution contribution = contributionRepository.findOneWithCommunityMembers(id);
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
    @Secured({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN, AuthoritiesConstants.LEADER})
    public ResponseEntity<Void> deleteContribution(@PathVariable Long id) {
        log.debug("REST request to delete Contribution : {}", id);
        contributionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("contribution", id.toString())).build();
    }

}
