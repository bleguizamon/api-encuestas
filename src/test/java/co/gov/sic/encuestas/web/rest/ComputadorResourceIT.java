package co.gov.sic.encuestas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.gov.sic.encuestas.IntegrationTest;
import co.gov.sic.encuestas.domain.Computador;
import co.gov.sic.encuestas.repository.ComputadorRepository;
import co.gov.sic.encuestas.service.dto.ComputadorDTO;
import co.gov.sic.encuestas.service.mapper.ComputadorMapper;
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
 * Integration tests for the {@link ComputadorResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComputadorResourceIT {

    private static final String DEFAULT_MARCA = "AAAAAAAAAA";
    private static final String UPDATED_MARCA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/computadors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ComputadorRepository computadorRepository;

    @Autowired
    private ComputadorMapper computadorMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComputadorMockMvc;

    private Computador computador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Computador createEntity(EntityManager em) {
        Computador computador = new Computador().marca(DEFAULT_MARCA);
        return computador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Computador createUpdatedEntity(EntityManager em) {
        Computador computador = new Computador().marca(UPDATED_MARCA);
        return computador;
    }

    @BeforeEach
    public void initTest() {
        computador = createEntity(em);
    }

    @Test
    @Transactional
    void createComputador() throws Exception {
        int databaseSizeBeforeCreate = computadorRepository.findAll().size();
        // Create the Computador
        ComputadorDTO computadorDTO = computadorMapper.toDto(computador);
        restComputadorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computadorDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Computador in the database
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeCreate + 1);
        Computador testComputador = computadorList.get(computadorList.size() - 1);
        assertThat(testComputador.getMarca()).isEqualTo(DEFAULT_MARCA);
    }

    @Test
    @Transactional
    void createComputadorWithExistingId() throws Exception {
        // Create the Computador with an existing ID
        computador.setId(1L);
        ComputadorDTO computadorDTO = computadorMapper.toDto(computador);

        int databaseSizeBeforeCreate = computadorRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComputadorMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Computador in the database
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllComputadors() throws Exception {
        // Initialize the database
        computadorRepository.saveAndFlush(computador);

        // Get all the computadorList
        restComputadorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(computador.getId().intValue())))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA)));
    }

    @Test
    @Transactional
    void getComputador() throws Exception {
        // Initialize the database
        computadorRepository.saveAndFlush(computador);

        // Get the computador
        restComputadorMockMvc
            .perform(get(ENTITY_API_URL_ID, computador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(computador.getId().intValue()))
            .andExpect(jsonPath("$.marca").value(DEFAULT_MARCA));
    }

    @Test
    @Transactional
    void getNonExistingComputador() throws Exception {
        // Get the computador
        restComputadorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingComputador() throws Exception {
        // Initialize the database
        computadorRepository.saveAndFlush(computador);

        int databaseSizeBeforeUpdate = computadorRepository.findAll().size();

        // Update the computador
        Computador updatedComputador = computadorRepository.findById(computador.getId()).get();
        // Disconnect from session so that the updates on updatedComputador are not directly saved in db
        em.detach(updatedComputador);
        updatedComputador.marca(UPDATED_MARCA);
        ComputadorDTO computadorDTO = computadorMapper.toDto(updatedComputador);

        restComputadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, computadorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computadorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Computador in the database
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeUpdate);
        Computador testComputador = computadorList.get(computadorList.size() - 1);
        assertThat(testComputador.getMarca()).isEqualTo(UPDATED_MARCA);
    }

    @Test
    @Transactional
    void putNonExistingComputador() throws Exception {
        int databaseSizeBeforeUpdate = computadorRepository.findAll().size();
        computador.setId(count.incrementAndGet());

        // Create the Computador
        ComputadorDTO computadorDTO = computadorMapper.toDto(computador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComputadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, computadorDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Computador in the database
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComputador() throws Exception {
        int databaseSizeBeforeUpdate = computadorRepository.findAll().size();
        computador.setId(count.incrementAndGet());

        // Create the Computador
        ComputadorDTO computadorDTO = computadorMapper.toDto(computador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputadorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Computador in the database
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComputador() throws Exception {
        int databaseSizeBeforeUpdate = computadorRepository.findAll().size();
        computador.setId(count.incrementAndGet());

        // Create the Computador
        ComputadorDTO computadorDTO = computadorMapper.toDto(computador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputadorMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(computadorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Computador in the database
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComputadorWithPatch() throws Exception {
        // Initialize the database
        computadorRepository.saveAndFlush(computador);

        int databaseSizeBeforeUpdate = computadorRepository.findAll().size();

        // Update the computador using partial update
        Computador partialUpdatedComputador = new Computador();
        partialUpdatedComputador.setId(computador.getId());

        partialUpdatedComputador.marca(UPDATED_MARCA);

        restComputadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComputador.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComputador))
            )
            .andExpect(status().isOk());

        // Validate the Computador in the database
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeUpdate);
        Computador testComputador = computadorList.get(computadorList.size() - 1);
        assertThat(testComputador.getMarca()).isEqualTo(UPDATED_MARCA);
    }

    @Test
    @Transactional
    void fullUpdateComputadorWithPatch() throws Exception {
        // Initialize the database
        computadorRepository.saveAndFlush(computador);

        int databaseSizeBeforeUpdate = computadorRepository.findAll().size();

        // Update the computador using partial update
        Computador partialUpdatedComputador = new Computador();
        partialUpdatedComputador.setId(computador.getId());

        partialUpdatedComputador.marca(UPDATED_MARCA);

        restComputadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComputador.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedComputador))
            )
            .andExpect(status().isOk());

        // Validate the Computador in the database
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeUpdate);
        Computador testComputador = computadorList.get(computadorList.size() - 1);
        assertThat(testComputador.getMarca()).isEqualTo(UPDATED_MARCA);
    }

    @Test
    @Transactional
    void patchNonExistingComputador() throws Exception {
        int databaseSizeBeforeUpdate = computadorRepository.findAll().size();
        computador.setId(count.incrementAndGet());

        // Create the Computador
        ComputadorDTO computadorDTO = computadorMapper.toDto(computador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComputadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, computadorDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(computadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Computador in the database
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComputador() throws Exception {
        int databaseSizeBeforeUpdate = computadorRepository.findAll().size();
        computador.setId(count.incrementAndGet());

        // Create the Computador
        ComputadorDTO computadorDTO = computadorMapper.toDto(computador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputadorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(computadorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Computador in the database
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComputador() throws Exception {
        int databaseSizeBeforeUpdate = computadorRepository.findAll().size();
        computador.setId(count.incrementAndGet());

        // Create the Computador
        ComputadorDTO computadorDTO = computadorMapper.toDto(computador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComputadorMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(computadorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Computador in the database
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComputador() throws Exception {
        // Initialize the database
        computadorRepository.saveAndFlush(computador);

        int databaseSizeBeforeDelete = computadorRepository.findAll().size();

        // Delete the computador
        restComputadorMockMvc
            .perform(delete(ENTITY_API_URL_ID, computador.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Computador> computadorList = computadorRepository.findAll();
        assertThat(computadorList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
