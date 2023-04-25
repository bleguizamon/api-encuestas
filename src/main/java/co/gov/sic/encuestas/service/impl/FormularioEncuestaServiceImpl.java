package co.gov.sic.encuestas.service.impl;

import co.gov.sic.encuestas.domain.FormularioEncuesta;
import co.gov.sic.encuestas.repository.FormularioEncuestaRepository;
import co.gov.sic.encuestas.service.FormularioEncuestaService;
import co.gov.sic.encuestas.service.dto.FormularioEncuestaDTO;
import co.gov.sic.encuestas.service.mapper.FormularioEncuestaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FormularioEncuesta}.
 */
@Service
@Transactional
public class FormularioEncuestaServiceImpl implements FormularioEncuestaService {

    private final Logger log = LoggerFactory.getLogger(FormularioEncuestaServiceImpl.class);

    private final FormularioEncuestaRepository formularioEncuestaRepository;

    private final FormularioEncuestaMapper formularioEncuestaMapper;

    public FormularioEncuestaServiceImpl(
        FormularioEncuestaRepository formularioEncuestaRepository,
        FormularioEncuestaMapper formularioEncuestaMapper
    ) {
        this.formularioEncuestaRepository = formularioEncuestaRepository;
        this.formularioEncuestaMapper = formularioEncuestaMapper;
    }

    @Override
    public FormularioEncuestaDTO save(FormularioEncuestaDTO formularioEncuestaDTO) {
        log.debug("Request to save FormularioEncuesta : {}", formularioEncuestaDTO);
        FormularioEncuesta formularioEncuesta = formularioEncuestaMapper.toEntity(formularioEncuestaDTO);
        formularioEncuesta = formularioEncuestaRepository.save(formularioEncuesta);
        return formularioEncuestaMapper.toDto(formularioEncuesta);
    }

    @Override
    public FormularioEncuestaDTO update(FormularioEncuestaDTO formularioEncuestaDTO) {
        log.debug("Request to update FormularioEncuesta : {}", formularioEncuestaDTO);
        FormularioEncuesta formularioEncuesta = formularioEncuestaMapper.toEntity(formularioEncuestaDTO);
        formularioEncuesta = formularioEncuestaRepository.save(formularioEncuesta);
        return formularioEncuestaMapper.toDto(formularioEncuesta);
    }

    @Override
    public Optional<FormularioEncuestaDTO> partialUpdate(FormularioEncuestaDTO formularioEncuestaDTO) {
        log.debug("Request to partially update FormularioEncuesta : {}", formularioEncuestaDTO);

        return formularioEncuestaRepository
            .findById(formularioEncuestaDTO.getId())
            .map(existingFormularioEncuesta -> {
                formularioEncuestaMapper.partialUpdate(existingFormularioEncuesta, formularioEncuestaDTO);

                return existingFormularioEncuesta;
            })
            .map(formularioEncuestaRepository::save)
            .map(formularioEncuestaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FormularioEncuestaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FormularioEncuestas");
        return formularioEncuestaRepository.findAll(pageable).map(formularioEncuestaMapper::toDto);
    }

    public Page<FormularioEncuestaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return formularioEncuestaRepository.findAllWithEagerRelationships(pageable).map(formularioEncuestaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FormularioEncuestaDTO> findOne(Long id) {
        log.debug("Request to get FormularioEncuesta : {}", id);
        return formularioEncuestaRepository.findOneWithEagerRelationships(id).map(formularioEncuestaMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FormularioEncuesta : {}", id);
        formularioEncuestaRepository.deleteById(id);
    }
}
