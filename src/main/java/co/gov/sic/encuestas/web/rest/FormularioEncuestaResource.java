package co.gov.sic.encuestas.web.rest;

import co.gov.sic.encuestas.repository.FormularioEncuestaRepository;
import co.gov.sic.encuestas.service.FormularioEncuestaService;
import co.gov.sic.encuestas.service.dto.FormularioEncuestaDTO;
import co.gov.sic.encuestas.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.gov.sic.encuestas.domain.FormularioEncuesta}.
 */
@RestController
@RequestMapping("/api")
public class FormularioEncuestaResource {

    private final Logger log = LoggerFactory.getLogger(FormularioEncuestaResource.class);

    private static final String ENTITY_NAME = "formularioEncuesta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FormularioEncuestaService formularioEncuestaService;

    private final FormularioEncuestaRepository formularioEncuestaRepository;

    public FormularioEncuestaResource(
        FormularioEncuestaService formularioEncuestaService,
        FormularioEncuestaRepository formularioEncuestaRepository
    ) {
        this.formularioEncuestaService = formularioEncuestaService;
        this.formularioEncuestaRepository = formularioEncuestaRepository;
    }

    /**
     * {@code POST  /formulario-encuestas} : Create a new formularioEncuesta.
     *
     * @param formularioEncuestaDTO the formularioEncuestaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new formularioEncuestaDTO, or with status {@code 400 (Bad Request)} if the formularioEncuesta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/formulario-encuestas")
    public ResponseEntity<FormularioEncuestaDTO> createFormularioEncuesta(@Valid @RequestBody FormularioEncuestaDTO formularioEncuestaDTO)
        throws URISyntaxException {
        log.debug("REST request to save FormularioEncuesta : {}", formularioEncuestaDTO);
        if (formularioEncuestaDTO.getId() != null) {
            throw new BadRequestAlertException("A new formularioEncuesta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        formularioEncuestaDTO.setFechaRespuesta(Instant.now());
        FormularioEncuestaDTO result = formularioEncuestaService.save(formularioEncuestaDTO);
        return ResponseEntity
            .created(new URI("/api/formulario-encuestas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /formulario-encuestas/:id} : Updates an existing formularioEncuesta.
     *
     * @param id the id of the formularioEncuestaDTO to save.
     * @param formularioEncuestaDTO the formularioEncuestaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formularioEncuestaDTO,
     * or with status {@code 400 (Bad Request)} if the formularioEncuestaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the formularioEncuestaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/formulario-encuestas/{id}")
    public ResponseEntity<FormularioEncuestaDTO> updateFormularioEncuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FormularioEncuestaDTO formularioEncuestaDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FormularioEncuesta : {}, {}", id, formularioEncuestaDTO);
        if (formularioEncuestaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formularioEncuestaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formularioEncuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        formularioEncuestaDTO.setFechaRespuesta(Instant.now());
        FormularioEncuestaDTO result = formularioEncuestaService.update(formularioEncuestaDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formularioEncuestaDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /formulario-encuestas/:id} : Partial updates given fields of an existing formularioEncuesta, field will ignore if it is null
     *
     * @param id the id of the formularioEncuestaDTO to save.
     * @param formularioEncuestaDTO the formularioEncuestaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated formularioEncuestaDTO,
     * or with status {@code 400 (Bad Request)} if the formularioEncuestaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the formularioEncuestaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the formularioEncuestaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/formulario-encuestas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FormularioEncuestaDTO> partialUpdateFormularioEncuesta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FormularioEncuestaDTO formularioEncuestaDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FormularioEncuesta partially : {}, {}", id, formularioEncuestaDTO);
        if (formularioEncuestaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, formularioEncuestaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!formularioEncuestaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        formularioEncuestaDTO.setFechaRespuesta(Instant.now());
        Optional<FormularioEncuestaDTO> result = formularioEncuestaService.partialUpdate(formularioEncuestaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, formularioEncuestaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /formulario-encuestas} : get all the formularioEncuestas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of formularioEncuestas in body.
     */
    @GetMapping("/formulario-encuestas")
    public ResponseEntity<List<FormularioEncuestaDTO>> getAllFormularioEncuestas(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of FormularioEncuestas");
        Page<FormularioEncuestaDTO> page;
        if (eagerload) {
            page = formularioEncuestaService.findAllWithEagerRelationships(pageable);
        } else {
            page = formularioEncuestaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /formulario-encuestas/:id} : get the "id" formularioEncuesta.
     *
     * @param id the id of the formularioEncuestaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the formularioEncuestaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/formulario-encuestas/{id}")
    public ResponseEntity<FormularioEncuestaDTO> getFormularioEncuesta(@PathVariable Long id) {
        log.debug("REST request to get FormularioEncuesta : {}", id);
        Optional<FormularioEncuestaDTO> formularioEncuestaDTO = formularioEncuestaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(formularioEncuestaDTO);
    }

    /**
     * {@code DELETE  /formulario-encuestas/:id} : delete the "id" formularioEncuesta.
     *
     * @param id the id of the formularioEncuestaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/formulario-encuestas/{id}")
    public ResponseEntity<Void> deleteFormularioEncuesta(@PathVariable Long id) {
        log.debug("REST request to delete FormularioEncuesta : {}", id);
        formularioEncuestaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
