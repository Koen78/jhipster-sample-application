package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Gebruiker;
import com.mycompany.myapp.repository.GebruikerRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link GebruikerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class GebruikerResourceIT {

    private static final Long DEFAULT_OBJECT_ID = 1L;
    private static final Long UPDATED_OBJECT_ID = 2L;

    private static final String DEFAULT_NAAM = "AAAAAAAAAA";
    private static final String UPDATED_NAAM = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/gebruikers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private GebruikerRepository gebruikerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGebruikerMockMvc;

    private Gebruiker gebruiker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gebruiker createEntity(EntityManager em) {
        Gebruiker gebruiker = new Gebruiker().objectId(DEFAULT_OBJECT_ID).naam(DEFAULT_NAAM);
        return gebruiker;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Gebruiker createUpdatedEntity(EntityManager em) {
        Gebruiker gebruiker = new Gebruiker().objectId(UPDATED_OBJECT_ID).naam(UPDATED_NAAM);
        return gebruiker;
    }

    @BeforeEach
    public void initTest() {
        gebruiker = createEntity(em);
    }

    @Test
    @Transactional
    void createGebruiker() throws Exception {
        int databaseSizeBeforeCreate = gebruikerRepository.findAll().size();
        // Create the Gebruiker
        restGebruikerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gebruiker)))
            .andExpect(status().isCreated());

        // Validate the Gebruiker in the database
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeCreate + 1);
        Gebruiker testGebruiker = gebruikerList.get(gebruikerList.size() - 1);
        assertThat(testGebruiker.getObjectId()).isEqualTo(DEFAULT_OBJECT_ID);
        assertThat(testGebruiker.getNaam()).isEqualTo(DEFAULT_NAAM);
    }

    @Test
    @Transactional
    void createGebruikerWithExistingId() throws Exception {
        // Create the Gebruiker with an existing ID
        gebruiker.setId(1L);

        int databaseSizeBeforeCreate = gebruikerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restGebruikerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gebruiker)))
            .andExpect(status().isBadRequest());

        // Validate the Gebruiker in the database
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllGebruikers() throws Exception {
        // Initialize the database
        gebruikerRepository.saveAndFlush(gebruiker);

        // Get all the gebruikerList
        restGebruikerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(gebruiker.getId().intValue())))
            .andExpect(jsonPath("$.[*].objectId").value(hasItem(DEFAULT_OBJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].naam").value(hasItem(DEFAULT_NAAM)));
    }

    @Test
    @Transactional
    void getGebruiker() throws Exception {
        // Initialize the database
        gebruikerRepository.saveAndFlush(gebruiker);

        // Get the gebruiker
        restGebruikerMockMvc
            .perform(get(ENTITY_API_URL_ID, gebruiker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(gebruiker.getId().intValue()))
            .andExpect(jsonPath("$.objectId").value(DEFAULT_OBJECT_ID.intValue()))
            .andExpect(jsonPath("$.naam").value(DEFAULT_NAAM));
    }

    @Test
    @Transactional
    void getNonExistingGebruiker() throws Exception {
        // Get the gebruiker
        restGebruikerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewGebruiker() throws Exception {
        // Initialize the database
        gebruikerRepository.saveAndFlush(gebruiker);

        int databaseSizeBeforeUpdate = gebruikerRepository.findAll().size();

        // Update the gebruiker
        Gebruiker updatedGebruiker = gebruikerRepository.findById(gebruiker.getId()).get();
        // Disconnect from session so that the updates on updatedGebruiker are not directly saved in db
        em.detach(updatedGebruiker);
        updatedGebruiker.objectId(UPDATED_OBJECT_ID).naam(UPDATED_NAAM);

        restGebruikerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedGebruiker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedGebruiker))
            )
            .andExpect(status().isOk());

        // Validate the Gebruiker in the database
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeUpdate);
        Gebruiker testGebruiker = gebruikerList.get(gebruikerList.size() - 1);
        assertThat(testGebruiker.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
        assertThat(testGebruiker.getNaam()).isEqualTo(UPDATED_NAAM);
    }

    @Test
    @Transactional
    void putNonExistingGebruiker() throws Exception {
        int databaseSizeBeforeUpdate = gebruikerRepository.findAll().size();
        gebruiker.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGebruikerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, gebruiker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gebruiker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gebruiker in the database
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchGebruiker() throws Exception {
        int databaseSizeBeforeUpdate = gebruikerRepository.findAll().size();
        gebruiker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGebruikerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(gebruiker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gebruiker in the database
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamGebruiker() throws Exception {
        int databaseSizeBeforeUpdate = gebruikerRepository.findAll().size();
        gebruiker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGebruikerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(gebruiker)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gebruiker in the database
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateGebruikerWithPatch() throws Exception {
        // Initialize the database
        gebruikerRepository.saveAndFlush(gebruiker);

        int databaseSizeBeforeUpdate = gebruikerRepository.findAll().size();

        // Update the gebruiker using partial update
        Gebruiker partialUpdatedGebruiker = new Gebruiker();
        partialUpdatedGebruiker.setId(gebruiker.getId());

        partialUpdatedGebruiker.objectId(UPDATED_OBJECT_ID).naam(UPDATED_NAAM);

        restGebruikerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGebruiker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGebruiker))
            )
            .andExpect(status().isOk());

        // Validate the Gebruiker in the database
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeUpdate);
        Gebruiker testGebruiker = gebruikerList.get(gebruikerList.size() - 1);
        assertThat(testGebruiker.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
        assertThat(testGebruiker.getNaam()).isEqualTo(UPDATED_NAAM);
    }

    @Test
    @Transactional
    void fullUpdateGebruikerWithPatch() throws Exception {
        // Initialize the database
        gebruikerRepository.saveAndFlush(gebruiker);

        int databaseSizeBeforeUpdate = gebruikerRepository.findAll().size();

        // Update the gebruiker using partial update
        Gebruiker partialUpdatedGebruiker = new Gebruiker();
        partialUpdatedGebruiker.setId(gebruiker.getId());

        partialUpdatedGebruiker.objectId(UPDATED_OBJECT_ID).naam(UPDATED_NAAM);

        restGebruikerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedGebruiker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedGebruiker))
            )
            .andExpect(status().isOk());

        // Validate the Gebruiker in the database
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeUpdate);
        Gebruiker testGebruiker = gebruikerList.get(gebruikerList.size() - 1);
        assertThat(testGebruiker.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
        assertThat(testGebruiker.getNaam()).isEqualTo(UPDATED_NAAM);
    }

    @Test
    @Transactional
    void patchNonExistingGebruiker() throws Exception {
        int databaseSizeBeforeUpdate = gebruikerRepository.findAll().size();
        gebruiker.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGebruikerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, gebruiker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gebruiker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gebruiker in the database
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchGebruiker() throws Exception {
        int databaseSizeBeforeUpdate = gebruikerRepository.findAll().size();
        gebruiker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGebruikerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(gebruiker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Gebruiker in the database
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamGebruiker() throws Exception {
        int databaseSizeBeforeUpdate = gebruikerRepository.findAll().size();
        gebruiker.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restGebruikerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(gebruiker))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Gebruiker in the database
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteGebruiker() throws Exception {
        // Initialize the database
        gebruikerRepository.saveAndFlush(gebruiker);

        int databaseSizeBeforeDelete = gebruikerRepository.findAll().size();

        // Delete the gebruiker
        restGebruikerMockMvc
            .perform(delete(ENTITY_API_URL_ID, gebruiker.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Gebruiker> gebruikerList = gebruikerRepository.findAll();
        assertThat(gebruikerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
