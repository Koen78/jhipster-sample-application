package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Overview;
import com.mycompany.myapp.repository.OverviewRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Overview}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class OverviewResource {

    private final Logger log = LoggerFactory.getLogger(OverviewResource.class);

    private static final String ENTITY_NAME = "overview";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OverviewRepository overviewRepository;

    public OverviewResource(OverviewRepository overviewRepository) {
        this.overviewRepository = overviewRepository;
    }

    /**
     * {@code POST  /overviews} : Create a new overview.
     *
     * @param overview the overview to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new overview, or with status {@code 400 (Bad Request)} if the overview has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/overviews")
    public ResponseEntity<Overview> createOverview(@RequestBody Overview overview) throws URISyntaxException {
        log.debug("REST request to save Overview : {}", overview);
        if (overview.getId() != null) {
            throw new BadRequestAlertException("A new overview cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Overview result = overviewRepository.save(overview);
        return ResponseEntity
            .created(new URI("/api/overviews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /overviews/:id} : Updates an existing overview.
     *
     * @param id the id of the overview to save.
     * @param overview the overview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated overview,
     * or with status {@code 400 (Bad Request)} if the overview is not valid,
     * or with status {@code 500 (Internal Server Error)} if the overview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/overviews/{id}")
    public ResponseEntity<Overview> updateOverview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Overview overview
    ) throws URISyntaxException {
        log.debug("REST request to update Overview : {}, {}", id, overview);
        if (overview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, overview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!overviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Overview result = overviewRepository.save(overview);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, overview.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /overviews/:id} : Partial updates given fields of an existing overview, field will ignore if it is null
     *
     * @param id the id of the overview to save.
     * @param overview the overview to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated overview,
     * or with status {@code 400 (Bad Request)} if the overview is not valid,
     * or with status {@code 404 (Not Found)} if the overview is not found,
     * or with status {@code 500 (Internal Server Error)} if the overview couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/overviews/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Overview> partialUpdateOverview(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Overview overview
    ) throws URISyntaxException {
        log.debug("REST request to partial update Overview partially : {}, {}", id, overview);
        if (overview.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, overview.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!overviewRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Overview> result = overviewRepository
            .findById(overview.getId())
            .map(
                existingOverview -> {
                    if (overview.getObjectId() != null) {
                        existingOverview.setObjectId(overview.getObjectId());
                    }
                    if (overview.getName() != null) {
                        existingOverview.setName(overview.getName());
                    }

                    return existingOverview;
                }
            )
            .map(overviewRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, overview.getId().toString())
        );
    }

    /**
     * {@code GET  /overviews} : get all the overviews.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of overviews in body.
     */
    @GetMapping("/overviews")
    public List<Overview> getAllOverviews(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Overviews");
        return overviewRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /overviews/:id} : get the "id" overview.
     *
     * @param id the id of the overview to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the overview, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/overviews/{id}")
    public ResponseEntity<Overview> getOverview(@PathVariable Long id) {
        log.debug("REST request to get Overview : {}", id);
        Optional<Overview> overview = overviewRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(overview);
    }

    /**
     * {@code DELETE  /overviews/:id} : delete the "id" overview.
     *
     * @param id the id of the overview to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/overviews/{id}")
    public ResponseEntity<Void> deleteOverview(@PathVariable Long id) {
        log.debug("REST request to delete Overview : {}", id);
        overviewRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
