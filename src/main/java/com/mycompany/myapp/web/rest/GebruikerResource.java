package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Gebruiker;
import com.mycompany.myapp.repository.GebruikerRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Gebruiker}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GebruikerResource {

    private final Logger log = LoggerFactory.getLogger(GebruikerResource.class);

    private static final String ENTITY_NAME = "gebruiker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GebruikerRepository gebruikerRepository;

    public GebruikerResource(GebruikerRepository gebruikerRepository) {
        this.gebruikerRepository = gebruikerRepository;
    }

    /**
     * {@code POST  /gebruikers} : Create a new gebruiker.
     *
     * @param gebruiker the gebruiker to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gebruiker, or with status {@code 400 (Bad Request)} if the gebruiker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gebruikers")
    public ResponseEntity<Gebruiker> createGebruiker(@RequestBody Gebruiker gebruiker) throws URISyntaxException {
        log.debug("REST request to save Gebruiker : {}", gebruiker);
        if (gebruiker.getId() != null) {
            throw new BadRequestAlertException("A new gebruiker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Gebruiker result = gebruikerRepository.save(gebruiker);
        return ResponseEntity
            .created(new URI("/api/gebruikers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gebruikers/:id} : Updates an existing gebruiker.
     *
     * @param id the id of the gebruiker to save.
     * @param gebruiker the gebruiker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gebruiker,
     * or with status {@code 400 (Bad Request)} if the gebruiker is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gebruiker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gebruikers/{id}")
    public ResponseEntity<Gebruiker> updateGebruiker(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Gebruiker gebruiker
    ) throws URISyntaxException {
        log.debug("REST request to update Gebruiker : {}, {}", id, gebruiker);
        if (gebruiker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gebruiker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gebruikerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Gebruiker result = gebruikerRepository.save(gebruiker);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gebruiker.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gebruikers/:id} : Partial updates given fields of an existing gebruiker, field will ignore if it is null
     *
     * @param id the id of the gebruiker to save.
     * @param gebruiker the gebruiker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gebruiker,
     * or with status {@code 400 (Bad Request)} if the gebruiker is not valid,
     * or with status {@code 404 (Not Found)} if the gebruiker is not found,
     * or with status {@code 500 (Internal Server Error)} if the gebruiker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gebruikers/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Gebruiker> partialUpdateGebruiker(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Gebruiker gebruiker
    ) throws URISyntaxException {
        log.debug("REST request to partial update Gebruiker partially : {}, {}", id, gebruiker);
        if (gebruiker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gebruiker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gebruikerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Gebruiker> result = gebruikerRepository
            .findById(gebruiker.getId())
            .map(
                existingGebruiker -> {
                    if (gebruiker.getObjectId() != null) {
                        existingGebruiker.setObjectId(gebruiker.getObjectId());
                    }
                    if (gebruiker.getNaam() != null) {
                        existingGebruiker.setNaam(gebruiker.getNaam());
                    }

                    return existingGebruiker;
                }
            )
            .map(gebruikerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gebruiker.getId().toString())
        );
    }

    /**
     * {@code GET  /gebruikers} : get all the gebruikers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gebruikers in body.
     */
    @GetMapping("/gebruikers")
    public List<Gebruiker> getAllGebruikers() {
        log.debug("REST request to get all Gebruikers");
        return gebruikerRepository.findAll();
    }

    /**
     * {@code GET  /gebruikers/:id} : get the "id" gebruiker.
     *
     * @param id the id of the gebruiker to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gebruiker, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gebruikers/{id}")
    public ResponseEntity<Gebruiker> getGebruiker(@PathVariable Long id) {
        log.debug("REST request to get Gebruiker : {}", id);
        Optional<Gebruiker> gebruiker = gebruikerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(gebruiker);
    }

    /**
     * {@code DELETE  /gebruikers/:id} : delete the "id" gebruiker.
     *
     * @param id the id of the gebruiker to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gebruikers/{id}")
    public ResponseEntity<Void> deleteGebruiker(@PathVariable Long id) {
        log.debug("REST request to delete Gebruiker : {}", id);
        gebruikerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
