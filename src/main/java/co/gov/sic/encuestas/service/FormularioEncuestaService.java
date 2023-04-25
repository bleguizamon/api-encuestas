package co.gov.sic.encuestas.service;

import co.gov.sic.encuestas.service.dto.FormularioEncuestaDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.gov.sic.encuestas.domain.FormularioEncuesta}.
 */
public interface FormularioEncuestaService {
    /**
     * Save a formularioEncuesta.
     *
     * @param formularioEncuestaDTO the entity to save.
     * @return the persisted entity.
     */
    FormularioEncuestaDTO save(FormularioEncuestaDTO formularioEncuestaDTO);

    /**
     * Updates a formularioEncuesta.
     *
     * @param formularioEncuestaDTO the entity to update.
     * @return the persisted entity.
     */
    FormularioEncuestaDTO update(FormularioEncuestaDTO formularioEncuestaDTO);

    /**
     * Partially updates a formularioEncuesta.
     *
     * @param formularioEncuestaDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FormularioEncuestaDTO> partialUpdate(FormularioEncuestaDTO formularioEncuestaDTO);

    /**
     * Get all the formularioEncuestas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormularioEncuestaDTO> findAll(Pageable pageable);

    /**
     * Get all the formularioEncuestas with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FormularioEncuestaDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" formularioEncuesta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FormularioEncuestaDTO> findOne(Long id);

    /**
     * Delete the "id" formularioEncuesta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
