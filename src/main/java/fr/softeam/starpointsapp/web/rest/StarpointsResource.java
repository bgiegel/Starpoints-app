package fr.softeam.starpointsapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.softeam.starpointsapp.repository.StarPointsRepository;
import fr.softeam.starpointsapp.service.dto.StarPointsByCommunityDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

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
     * GET  /scales : get all the scales.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of scales in body
     */
    @RequestMapping(value = "/starpoints-by-community/{userId}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<StarPointsByCommunityDTO> getStarPointsByCommunity(@PathVariable Long userId) {
        log.debug("REST request to get all Scales");
        List<Object[]> results = starPointsRepository.calculateStarPointsByCommunityForUser(userId);
        return buildStarPointsByCommunity(results);
    }

    /**
     * On construit la liste de StarPointsByCommunityDTO à partir de la liste d'objets retourné par le repository.
     */
    private List<StarPointsByCommunityDTO> buildStarPointsByCommunity(List<Object[]> results) {
        List<StarPointsByCommunityDTO> starpointsList = new ArrayList<>();
        if(!CollectionUtils.isEmpty(results)){
            for (Object[] result : results) {
                String community = result[0].toString();
                Long points = 0L;
                if (result[1] != null) {
                    points = Long.valueOf(result[1].toString());
                }
                starpointsList.add(new StarPointsByCommunityDTO(community, points));
            }
        }
        return starpointsList;
    }

}
