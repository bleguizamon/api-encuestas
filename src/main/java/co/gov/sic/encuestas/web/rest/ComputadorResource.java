package co.gov.sic.encuestas.web.rest;

import co.gov.sic.encuestas.repository.ComputadorRepository;
import co.gov.sic.encuestas.service.ComputadorService;
import co.gov.sic.encuestas.service.dto.ComputadorDTO;
import co.gov.sic.encuestas.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link co.gov.sic.encuestas.domain.Computador}.
 */
@RestController
@RequestMapping("/api")
public class ComputadorResource {

    private final Logger log = LoggerFactory.getLogger(ComputadorResource.class);

    private static final String ENTITY_NAME = "computador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ComputadorService computadorService;

    private final ComputadorRepository computadorRepository;

    public ComputadorResource(ComputadorService computadorService, ComputadorRepository computadorRepository) {
        this.computadorService = computadorService;
        this.computadorRepository = computadorRepository;
    }

    /**
     * {@code POST  /computadors} : Create a new computador.
     *
     * @param computadorDTO the computadorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new computadorDTO, or with status {@code 400 (Bad Request)} if the computador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/computadors")
    public ResponseEntity<ComputadorDTO> createComputador(@RequestBody ComputadorDTO computadorDTO) throws URISyntaxException {
        log.debug("REST request to save Computador : {}", computadorDTO);
        if (computadorDTO.getId() != null) {
            throw new BadRequestAlertException("A new computador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ComputadorDTO result = computadorService.save(computadorDTO);
        return ResponseEntity
            .created(new URI("/api/computadors/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /computadors/:id} : Updates an existing computador.
     *
     * @param id the id of the computadorDTO to save.
     * @param computadorDTO the computadorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated computadorDTO,
     * or with status {@code 400 (Bad Request)} if the computadorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the computadorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/computadors/{id}")
    public ResponseEntity<ComputadorDTO> updateComputador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComputadorDTO computadorDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Computador : {}, {}", id, computadorDTO);
        if (computadorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, computadorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!computadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ComputadorDTO result = computadorService.update(computadorDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, computadorDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /computadors/:id} : Partial updates given fields of an existing computador, field will ignore if it is null
     *
     * @param id the id of the computadorDTO to save.
     * @param computadorDTO the computadorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated computadorDTO,
     * or with status {@code 400 (Bad Request)} if the computadorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the computadorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the computadorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/computadors/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ComputadorDTO> partialUpdateComputador(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ComputadorDTO computadorDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Computador partially : {}, {}", id, computadorDTO);
        if (computadorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, computadorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!computadorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ComputadorDTO> result = computadorService.partialUpdate(computadorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, computadorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /computadors} : get all the computadors.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of computadors in body.
     */
    @GetMapping("/computadors")
    public ResponseEntity<List<ComputadorDTO>> getAllComputadors(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Computadors");
        Page<ComputadorDTO> page = computadorService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /computadors/:id} : get the "id" computador.
     *
     * @param id the id of the computadorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the computadorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/computadors/{id}")
    public ResponseEntity<ComputadorDTO> getComputador(@PathVariable Long id) {
        log.debug("REST request to get Computador : {}", id);
        Optional<ComputadorDTO> computadorDTO = computadorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(computadorDTO);
    }

    /**
     * {@code DELETE  /computadors/:id} : delete the "id" computador.
     *
     * @param id the id of the computadorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/computadors/{id}")
    public ResponseEntity<Void> deleteComputador(@PathVariable Long id) {
        log.debug("REST request to delete Computador : {}", id);
        computadorService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
