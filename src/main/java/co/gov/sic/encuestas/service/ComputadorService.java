package co.gov.sic.encuestas.service;

import co.gov.sic.encuestas.service.dto.ComputadorDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.gov.sic.encuestas.domain.Computador}.
 */
public interface ComputadorService {
    /**
     * Save a computador.
     *
     * @param computadorDTO the entity to save.
     * @return the persisted entity.
     */
    ComputadorDTO save(ComputadorDTO computadorDTO);

    /**
     * Updates a computador.
     *
     * @param computadorDTO the entity to update.
     * @return the persisted entity.
     */
    ComputadorDTO update(ComputadorDTO computadorDTO);

    /**
     * Partially updates a computador.
     *
     * @param computadorDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ComputadorDTO> partialUpdate(ComputadorDTO computadorDTO);

    /**
     * Get all the computadors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ComputadorDTO> findAll(Pageable pageable);

    /**
     * Get the "id" computador.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ComputadorDTO> findOne(Long id);

    /**
     * Delete the "id" computador.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
