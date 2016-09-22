package fr.softeam.starpointsapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.softeam.starpointsapp.domain.Level;

import fr.softeam.starpointsapp.repository.LevelRepository;
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
 * REST controller for managing Level.
 */
@RestController
@RequestMapping("/api")
public class LevelResource {

    private final Logger log = LoggerFactory.getLogger(LevelResource.class);
        
    @Inject
    private LevelRepository levelRepository;

    /**
     * POST  /levels : Create a new level.
     *
     * @param level the level to create
     * @return the ResponseEntity with status 201 (Created) and with body the new level, or with status 400 (Bad Request) if the level has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/levels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Level> createLevel(@RequestBody Level level) throws URISyntaxException {
        log.debug("REST request to save Level : {}", level);
        if (level.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("level", "idexists", "A new level cannot already have an ID")).body(null);
        }
        Level result = levelRepository.save(level);
        return ResponseEntity.created(new URI("/api/levels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("level", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /levels : Updates an existing level.
     *
     * @param level the level to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated level,
     * or with status 400 (Bad Request) if the level is not valid,
     * or with status 500 (Internal Server Error) if the level couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/levels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Level> updateLevel(@RequestBody Level level) throws URISyntaxException {
        log.debug("REST request to update Level : {}", level);
        if (level.getId() == null) {
            return createLevel(level);
        }
        Level result = levelRepository.save(level);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("level", level.getId().toString()))
            .body(result);
    }

    /**
     * GET  /levels : get all the levels.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of levels in body
     */
    @RequestMapping(value = "/levels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Level> getAllLevels() {
        log.debug("REST request to get all Levels");
        List<Level> levels = levelRepository.findAll();
        return levels;
    }

    /**
     * GET  /levels/:id : get the "id" level.
     *
     * @param id the id of the level to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the level, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/levels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Level> getLevel(@PathVariable Long id) {
        log.debug("REST request to get Level : {}", id);
        Level level = levelRepository.findOne(id);
        return Optional.ofNullable(level)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /levels/:id : delete the "id" level.
     *
     * @param id the id of the level to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/levels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        log.debug("REST request to delete Level : {}", id);
        levelRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("level", id.toString())).build();
    }

}
