package fr.softeam.starpointsapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.softeam.starpointsapp.domain.Activity;
import fr.softeam.starpointsapp.repository.ActivityRepository;
import fr.softeam.starpointsapp.security.AuthoritiesConstants;
import fr.softeam.starpointsapp.service.ActivityService;
import fr.softeam.starpointsapp.service.exception.ActivityReferencedByContributionsException;
import fr.softeam.starpointsapp.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Activity.
 */
@RestController
@RequestMapping("/api")
public class ActivityResource {

    private final Logger log = LoggerFactory.getLogger(ActivityResource.class);

    @Inject
    private ActivityRepository activityRepository;

    @Inject
    private ActivityService activityService;

    /**
     * POST  /activities : Create a new activity.
     *
     * @param activity the activity to create
     * @return the ResponseEntity with status 201 (Created) and with body the new activity, or with status 400 (Bad Request) if the activity has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/activities",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.LEADER})
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to save Activity : {}", activity);
        if (activity.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("activity", "idexists", "A new activity cannot already have an ID")).body(null);
        }
        Activity result = activityRepository.save(activity);
        return ResponseEntity.created(new URI("/api/activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("activity", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /activities : Updates an existing activity.
     *
     * @param activity the activity to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated activity,
     * or with status 400 (Bad Request) if the activity is not valid,
     * or with status 500 (Internal Server Error) if the activity couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/activities",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.LEADER})
    public ResponseEntity<Activity> updateActivity(@RequestBody Activity activity) throws URISyntaxException {
        log.debug("REST request to update Activity : {}", activity);
        if (activity.getId() == null) {
            return createActivity(activity);
        }
        Activity result = activityRepository.save(activity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("activity", activity.getId().toString()))
            .body(result);
    }

    /**
     * GET  /activities : get all the activities.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of activities in body
     */
    @RequestMapping(value = "/activities",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Activity> getAllActivities() {
        log.debug("REST request to get all Activities");
        List<Activity> activities = activityRepository.findAll();
        return activities;
    }

    /**
     * GET  /activities/:id : get the "id" activity.
     *
     * @param id the id of the activity to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the activity, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/activities/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Activity> getActivity(@PathVariable Long id) {
        log.debug("REST request to get Activity : {}", id);
        Activity activity = activityRepository.findOne(id);
        return Optional.ofNullable(activity)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /activities/:id : delete the "id" activity.
     *
     * @param id the id of the activity to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/activities/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    @Secured({AuthoritiesConstants.ADMIN, AuthoritiesConstants.LEADER})
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        log.debug("REST request to delete Activity : {}", id);
        try {
            activityService.deleteActivity(id);
        } catch (ActivityReferencedByContributionsException e) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(
                "activityWithRelatedContributionsDeletionImpossible",
                "Impossible de supprimer une activité rattachée à une ou des contributions.")).build();
        }
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("activity", id.toString())).build();
    }

}
