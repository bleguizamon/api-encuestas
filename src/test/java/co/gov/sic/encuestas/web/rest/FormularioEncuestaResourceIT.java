package co.gov.sic.encuestas.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.gov.sic.encuestas.IntegrationTest;
import co.gov.sic.encuestas.domain.FormularioEncuesta;
import co.gov.sic.encuestas.repository.FormularioEncuestaRepository;
import co.gov.sic.encuestas.service.FormularioEncuestaService;
import co.gov.sic.encuestas.service.dto.FormularioEncuestaDTO;
import co.gov.sic.encuestas.service.mapper.FormularioEncuestaMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FormularioEncuestaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FormularioEncuestaResourceIT {

    private static final Integer DEFAULT_NUMERO_DOCUMENTO = 1;
    private static final Integer UPDATED_NUMERO_DOCUMENTO = 2;

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_COMENTARIOS = "AAAAAAAAAA";
    private static final String UPDATED_COMENTARIOS = "BBBBBBBBBB";

    private static final Instant DEFAULT_FECHA_RESPUESTA = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FECHA_RESPUESTA = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/formulario-encuestas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FormularioEncuestaRepository formularioEncuestaRepository;

    @Mock
    private FormularioEncuestaRepository formularioEncuestaRepositoryMock;

    @Autowired
    private FormularioEncuestaMapper formularioEncuestaMapper;

    @Mock
    private FormularioEncuestaService formularioEncuestaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormularioEncuestaMockMvc;

    private FormularioEncuesta formularioEncuesta;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormularioEncuesta createEntity(EntityManager em) {
        FormularioEncuesta formularioEncuesta = new FormularioEncuesta()
            .numeroDocumento(DEFAULT_NUMERO_DOCUMENTO)
            .email(DEFAULT_EMAIL)
            .comentarios(DEFAULT_COMENTARIOS)
            .fechaRespuesta(DEFAULT_FECHA_RESPUESTA);
        return formularioEncuesta;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormularioEncuesta createUpdatedEntity(EntityManager em) {
        FormularioEncuesta formularioEncuesta = new FormularioEncuesta()
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .email(UPDATED_EMAIL)
            .comentarios(UPDATED_COMENTARIOS)
            .fechaRespuesta(UPDATED_FECHA_RESPUESTA);
        return formularioEncuesta;
    }

    @BeforeEach
    public void initTest() {
        formularioEncuesta = createEntity(em);
    }

    @Test
    @Transactional
    void createFormularioEncuesta() throws Exception {
        int databaseSizeBeforeCreate = formularioEncuestaRepository.findAll().size();
        // Create the FormularioEncuesta
        FormularioEncuestaDTO formularioEncuestaDTO = formularioEncuestaMapper.toDto(formularioEncuesta);
        restFormularioEncuestaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formularioEncuestaDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FormularioEncuesta in the database
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeCreate + 1);
        FormularioEncuesta testFormularioEncuesta = formularioEncuestaList.get(formularioEncuestaList.size() - 1);
        assertThat(testFormularioEncuesta.getNumeroDocumento()).isEqualTo(DEFAULT_NUMERO_DOCUMENTO);
        assertThat(testFormularioEncuesta.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFormularioEncuesta.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
        assertThat(testFormularioEncuesta.getFechaRespuesta()).isEqualTo(DEFAULT_FECHA_RESPUESTA);
    }

    @Test
    @Transactional
    void createFormularioEncuestaWithExistingId() throws Exception {
        // Create the FormularioEncuesta with an existing ID
        formularioEncuesta.setId(1L);
        FormularioEncuestaDTO formularioEncuestaDTO = formularioEncuestaMapper.toDto(formularioEncuesta);

        int databaseSizeBeforeCreate = formularioEncuestaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormularioEncuestaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formularioEncuestaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormularioEncuesta in the database
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNumeroDocumentoIsRequired() throws Exception {
        int databaseSizeBeforeTest = formularioEncuestaRepository.findAll().size();
        // set the field null
        formularioEncuesta.setNumeroDocumento(null);

        // Create the FormularioEncuesta, which fails.
        FormularioEncuestaDTO formularioEncuestaDTO = formularioEncuestaMapper.toDto(formularioEncuesta);

        restFormularioEncuestaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formularioEncuestaDTO))
            )
            .andExpect(status().isBadRequest());

        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailIsRequired() throws Exception {
        int databaseSizeBeforeTest = formularioEncuestaRepository.findAll().size();
        // set the field null
        formularioEncuesta.setEmail(null);

        // Create the FormularioEncuesta, which fails.
        FormularioEncuestaDTO formularioEncuestaDTO = formularioEncuestaMapper.toDto(formularioEncuesta);

        restFormularioEncuestaMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formularioEncuestaDTO))
            )
            .andExpect(status().isBadRequest());

        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormularioEncuestas() throws Exception {
        // Initialize the database
        formularioEncuestaRepository.saveAndFlush(formularioEncuesta);

        // Get all the formularioEncuestaList
        restFormularioEncuestaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formularioEncuesta.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroDocumento").value(hasItem(DEFAULT_NUMERO_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].comentarios").value(hasItem(DEFAULT_COMENTARIOS)))
            .andExpect(jsonPath("$.[*].fechaRespuesta").value(hasItem(DEFAULT_FECHA_RESPUESTA.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormularioEncuestasWithEagerRelationshipsIsEnabled() throws Exception {
        when(formularioEncuestaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFormularioEncuestaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(formularioEncuestaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFormularioEncuestasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(formularioEncuestaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFormularioEncuestaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(formularioEncuestaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFormularioEncuesta() throws Exception {
        // Initialize the database
        formularioEncuestaRepository.saveAndFlush(formularioEncuesta);

        // Get the formularioEncuesta
        restFormularioEncuestaMockMvc
            .perform(get(ENTITY_API_URL_ID, formularioEncuesta.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formularioEncuesta.getId().intValue()))
            .andExpect(jsonPath("$.numeroDocumento").value(DEFAULT_NUMERO_DOCUMENTO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.comentarios").value(DEFAULT_COMENTARIOS))
            .andExpect(jsonPath("$.fechaRespuesta").value(DEFAULT_FECHA_RESPUESTA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFormularioEncuesta() throws Exception {
        // Get the formularioEncuesta
        restFormularioEncuestaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormularioEncuesta() throws Exception {
        // Initialize the database
        formularioEncuestaRepository.saveAndFlush(formularioEncuesta);

        int databaseSizeBeforeUpdate = formularioEncuestaRepository.findAll().size();

        // Update the formularioEncuesta
        FormularioEncuesta updatedFormularioEncuesta = formularioEncuestaRepository.findById(formularioEncuesta.getId()).get();
        // Disconnect from session so that the updates on updatedFormularioEncuesta are not directly saved in db
        em.detach(updatedFormularioEncuesta);
        updatedFormularioEncuesta
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .email(UPDATED_EMAIL)
            .comentarios(UPDATED_COMENTARIOS)
            .fechaRespuesta(UPDATED_FECHA_RESPUESTA);
        FormularioEncuestaDTO formularioEncuestaDTO = formularioEncuestaMapper.toDto(updatedFormularioEncuesta);

        restFormularioEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formularioEncuestaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formularioEncuestaDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormularioEncuesta in the database
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeUpdate);
        FormularioEncuesta testFormularioEncuesta = formularioEncuestaList.get(formularioEncuestaList.size() - 1);
        assertThat(testFormularioEncuesta.getNumeroDocumento()).isEqualTo(UPDATED_NUMERO_DOCUMENTO);
        assertThat(testFormularioEncuesta.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFormularioEncuesta.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testFormularioEncuesta.getFechaRespuesta()).isEqualTo(UPDATED_FECHA_RESPUESTA);
    }

    @Test
    @Transactional
    void putNonExistingFormularioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = formularioEncuestaRepository.findAll().size();
        formularioEncuesta.setId(count.incrementAndGet());

        // Create the FormularioEncuesta
        FormularioEncuestaDTO formularioEncuestaDTO = formularioEncuestaMapper.toDto(formularioEncuesta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormularioEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formularioEncuestaDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formularioEncuestaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormularioEncuesta in the database
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormularioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = formularioEncuestaRepository.findAll().size();
        formularioEncuesta.setId(count.incrementAndGet());

        // Create the FormularioEncuesta
        FormularioEncuestaDTO formularioEncuestaDTO = formularioEncuestaMapper.toDto(formularioEncuesta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormularioEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formularioEncuestaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormularioEncuesta in the database
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormularioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = formularioEncuestaRepository.findAll().size();
        formularioEncuesta.setId(count.incrementAndGet());

        // Create the FormularioEncuesta
        FormularioEncuestaDTO formularioEncuestaDTO = formularioEncuestaMapper.toDto(formularioEncuesta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormularioEncuestaMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(formularioEncuestaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormularioEncuesta in the database
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormularioEncuestaWithPatch() throws Exception {
        // Initialize the database
        formularioEncuestaRepository.saveAndFlush(formularioEncuesta);

        int databaseSizeBeforeUpdate = formularioEncuestaRepository.findAll().size();

        // Update the formularioEncuesta using partial update
        FormularioEncuesta partialUpdatedFormularioEncuesta = new FormularioEncuesta();
        partialUpdatedFormularioEncuesta.setId(formularioEncuesta.getId());

        partialUpdatedFormularioEncuesta.fechaRespuesta(UPDATED_FECHA_RESPUESTA);

        restFormularioEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormularioEncuesta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormularioEncuesta))
            )
            .andExpect(status().isOk());

        // Validate the FormularioEncuesta in the database
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeUpdate);
        FormularioEncuesta testFormularioEncuesta = formularioEncuestaList.get(formularioEncuestaList.size() - 1);
        assertThat(testFormularioEncuesta.getNumeroDocumento()).isEqualTo(DEFAULT_NUMERO_DOCUMENTO);
        assertThat(testFormularioEncuesta.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testFormularioEncuesta.getComentarios()).isEqualTo(DEFAULT_COMENTARIOS);
        assertThat(testFormularioEncuesta.getFechaRespuesta()).isEqualTo(UPDATED_FECHA_RESPUESTA);
    }

    @Test
    @Transactional
    void fullUpdateFormularioEncuestaWithPatch() throws Exception {
        // Initialize the database
        formularioEncuestaRepository.saveAndFlush(formularioEncuesta);

        int databaseSizeBeforeUpdate = formularioEncuestaRepository.findAll().size();

        // Update the formularioEncuesta using partial update
        FormularioEncuesta partialUpdatedFormularioEncuesta = new FormularioEncuesta();
        partialUpdatedFormularioEncuesta.setId(formularioEncuesta.getId());

        partialUpdatedFormularioEncuesta
            .numeroDocumento(UPDATED_NUMERO_DOCUMENTO)
            .email(UPDATED_EMAIL)
            .comentarios(UPDATED_COMENTARIOS)
            .fechaRespuesta(UPDATED_FECHA_RESPUESTA);

        restFormularioEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormularioEncuesta.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFormularioEncuesta))
            )
            .andExpect(status().isOk());

        // Validate the FormularioEncuesta in the database
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeUpdate);
        FormularioEncuesta testFormularioEncuesta = formularioEncuestaList.get(formularioEncuestaList.size() - 1);
        assertThat(testFormularioEncuesta.getNumeroDocumento()).isEqualTo(UPDATED_NUMERO_DOCUMENTO);
        assertThat(testFormularioEncuesta.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testFormularioEncuesta.getComentarios()).isEqualTo(UPDATED_COMENTARIOS);
        assertThat(testFormularioEncuesta.getFechaRespuesta()).isEqualTo(UPDATED_FECHA_RESPUESTA);
    }

    @Test
    @Transactional
    void patchNonExistingFormularioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = formularioEncuestaRepository.findAll().size();
        formularioEncuesta.setId(count.incrementAndGet());

        // Create the FormularioEncuesta
        FormularioEncuestaDTO formularioEncuestaDTO = formularioEncuestaMapper.toDto(formularioEncuesta);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormularioEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formularioEncuestaDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formularioEncuestaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormularioEncuesta in the database
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormularioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = formularioEncuestaRepository.findAll().size();
        formularioEncuesta.setId(count.incrementAndGet());

        // Create the FormularioEncuesta
        FormularioEncuestaDTO formularioEncuestaDTO = formularioEncuestaMapper.toDto(formularioEncuesta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormularioEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formularioEncuestaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormularioEncuesta in the database
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormularioEncuesta() throws Exception {
        int databaseSizeBeforeUpdate = formularioEncuestaRepository.findAll().size();
        formularioEncuesta.setId(count.incrementAndGet());

        // Create the FormularioEncuesta
        FormularioEncuestaDTO formularioEncuestaDTO = formularioEncuestaMapper.toDto(formularioEncuesta);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormularioEncuestaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(formularioEncuestaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormularioEncuesta in the database
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormularioEncuesta() throws Exception {
        // Initialize the database
        formularioEncuestaRepository.saveAndFlush(formularioEncuesta);

        int databaseSizeBeforeDelete = formularioEncuestaRepository.findAll().size();

        // Delete the formularioEncuesta
        restFormularioEncuestaMockMvc
            .perform(delete(ENTITY_API_URL_ID, formularioEncuesta.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FormularioEncuesta> formularioEncuestaList = formularioEncuestaRepository.findAll();
        assertThat(formularioEncuestaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
