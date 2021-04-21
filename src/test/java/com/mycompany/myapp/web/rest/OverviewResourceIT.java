package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Overview;
import com.mycompany.myapp.repository.OverviewRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link OverviewResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class OverviewResourceIT {

    private static final Long DEFAULT_OBJECT_ID = 1L;
    private static final Long UPDATED_OBJECT_ID = 2L;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/overviews";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private OverviewRepository overviewRepository;

    @Mock
    private OverviewRepository overviewRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOverviewMockMvc;

    private Overview overview;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Overview createEntity(EntityManager em) {
        Overview overview = new Overview().objectId(DEFAULT_OBJECT_ID).name(DEFAULT_NAME);
        return overview;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Overview createUpdatedEntity(EntityManager em) {
        Overview overview = new Overview().objectId(UPDATED_OBJECT_ID).name(UPDATED_NAME);
        return overview;
    }

    @BeforeEach
    public void initTest() {
        overview = createEntity(em);
    }

    @Test
    @Transactional
    void createOverview() throws Exception {
        int databaseSizeBeforeCreate = overviewRepository.findAll().size();
        // Create the Overview
        restOverviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(overview)))
            .andExpect(status().isCreated());

        // Validate the Overview in the database
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeCreate + 1);
        Overview testOverview = overviewList.get(overviewList.size() - 1);
        assertThat(testOverview.getObjectId()).isEqualTo(DEFAULT_OBJECT_ID);
        assertThat(testOverview.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createOverviewWithExistingId() throws Exception {
        // Create the Overview with an existing ID
        overview.setId(1L);

        int databaseSizeBeforeCreate = overviewRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOverviewMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(overview)))
            .andExpect(status().isBadRequest());

        // Validate the Overview in the database
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllOverviews() throws Exception {
        // Initialize the database
        overviewRepository.saveAndFlush(overview);

        // Get all the overviewList
        restOverviewMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(overview.getId().intValue())))
            .andExpect(jsonPath("$.[*].objectId").value(hasItem(DEFAULT_OBJECT_ID.intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOverviewsWithEagerRelationshipsIsEnabled() throws Exception {
        when(overviewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOverviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(overviewRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllOverviewsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(overviewRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restOverviewMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(overviewRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getOverview() throws Exception {
        // Initialize the database
        overviewRepository.saveAndFlush(overview);

        // Get the overview
        restOverviewMockMvc
            .perform(get(ENTITY_API_URL_ID, overview.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(overview.getId().intValue()))
            .andExpect(jsonPath("$.objectId").value(DEFAULT_OBJECT_ID.intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingOverview() throws Exception {
        // Get the overview
        restOverviewMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewOverview() throws Exception {
        // Initialize the database
        overviewRepository.saveAndFlush(overview);

        int databaseSizeBeforeUpdate = overviewRepository.findAll().size();

        // Update the overview
        Overview updatedOverview = overviewRepository.findById(overview.getId()).get();
        // Disconnect from session so that the updates on updatedOverview are not directly saved in db
        em.detach(updatedOverview);
        updatedOverview.objectId(UPDATED_OBJECT_ID).name(UPDATED_NAME);

        restOverviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedOverview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedOverview))
            )
            .andExpect(status().isOk());

        // Validate the Overview in the database
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeUpdate);
        Overview testOverview = overviewList.get(overviewList.size() - 1);
        assertThat(testOverview.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
        assertThat(testOverview.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingOverview() throws Exception {
        int databaseSizeBeforeUpdate = overviewRepository.findAll().size();
        overview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, overview.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(overview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Overview in the database
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOverview() throws Exception {
        int databaseSizeBeforeUpdate = overviewRepository.findAll().size();
        overview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(overview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Overview in the database
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOverview() throws Exception {
        int databaseSizeBeforeUpdate = overviewRepository.findAll().size();
        overview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(overview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Overview in the database
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOverviewWithPatch() throws Exception {
        // Initialize the database
        overviewRepository.saveAndFlush(overview);

        int databaseSizeBeforeUpdate = overviewRepository.findAll().size();

        // Update the overview using partial update
        Overview partialUpdatedOverview = new Overview();
        partialUpdatedOverview.setId(overview.getId());

        partialUpdatedOverview.objectId(UPDATED_OBJECT_ID).name(UPDATED_NAME);

        restOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOverview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOverview))
            )
            .andExpect(status().isOk());

        // Validate the Overview in the database
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeUpdate);
        Overview testOverview = overviewList.get(overviewList.size() - 1);
        assertThat(testOverview.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
        assertThat(testOverview.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateOverviewWithPatch() throws Exception {
        // Initialize the database
        overviewRepository.saveAndFlush(overview);

        int databaseSizeBeforeUpdate = overviewRepository.findAll().size();

        // Update the overview using partial update
        Overview partialUpdatedOverview = new Overview();
        partialUpdatedOverview.setId(overview.getId());

        partialUpdatedOverview.objectId(UPDATED_OBJECT_ID).name(UPDATED_NAME);

        restOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOverview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedOverview))
            )
            .andExpect(status().isOk());

        // Validate the Overview in the database
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeUpdate);
        Overview testOverview = overviewList.get(overviewList.size() - 1);
        assertThat(testOverview.getObjectId()).isEqualTo(UPDATED_OBJECT_ID);
        assertThat(testOverview.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingOverview() throws Exception {
        int databaseSizeBeforeUpdate = overviewRepository.findAll().size();
        overview.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, overview.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(overview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Overview in the database
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOverview() throws Exception {
        int databaseSizeBeforeUpdate = overviewRepository.findAll().size();
        overview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(overview))
            )
            .andExpect(status().isBadRequest());

        // Validate the Overview in the database
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOverview() throws Exception {
        int databaseSizeBeforeUpdate = overviewRepository.findAll().size();
        overview.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOverviewMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(overview)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Overview in the database
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOverview() throws Exception {
        // Initialize the database
        overviewRepository.saveAndFlush(overview);

        int databaseSizeBeforeDelete = overviewRepository.findAll().size();

        // Delete the overview
        restOverviewMockMvc
            .perform(delete(ENTITY_API_URL_ID, overview.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Overview> overviewList = overviewRepository.findAll();
        assertThat(overviewList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
