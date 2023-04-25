package co.gov.sic.encuestas.service.impl;

import co.gov.sic.encuestas.domain.Computador;
import co.gov.sic.encuestas.repository.ComputadorRepository;
import co.gov.sic.encuestas.service.ComputadorService;
import co.gov.sic.encuestas.service.dto.ComputadorDTO;
import co.gov.sic.encuestas.service.mapper.ComputadorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Computador}.
 */
@Service
@Transactional
public class ComputadorServiceImpl implements ComputadorService {

    private final Logger log = LoggerFactory.getLogger(ComputadorServiceImpl.class);

    private final ComputadorRepository computadorRepository;

    private final ComputadorMapper computadorMapper;

    public ComputadorServiceImpl(ComputadorRepository computadorRepository, ComputadorMapper computadorMapper) {
        this.computadorRepository = computadorRepository;
        this.computadorMapper = computadorMapper;
    }

    @Override
    public ComputadorDTO save(ComputadorDTO computadorDTO) {
        log.debug("Request to save Computador : {}", computadorDTO);
        Computador computador = computadorMapper.toEntity(computadorDTO);
        computador = computadorRepository.save(computador);
        return computadorMapper.toDto(computador);
    }

    @Override
    public ComputadorDTO update(ComputadorDTO computadorDTO) {
        log.debug("Request to update Computador : {}", computadorDTO);
        Computador computador = computadorMapper.toEntity(computadorDTO);
        computador = computadorRepository.save(computador);
        return computadorMapper.toDto(computador);
    }

    @Override
    public Optional<ComputadorDTO> partialUpdate(ComputadorDTO computadorDTO) {
        log.debug("Request to partially update Computador : {}", computadorDTO);

        return computadorRepository
            .findById(computadorDTO.getId())
            .map(existingComputador -> {
                computadorMapper.partialUpdate(existingComputador, computadorDTO);

                return existingComputador;
            })
            .map(computadorRepository::save)
            .map(computadorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ComputadorDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Computadors");
        return computadorRepository.findAll(pageable).map(computadorMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ComputadorDTO> findOne(Long id) {
        log.debug("Request to get Computador : {}", id);
        return computadorRepository.findById(id).map(computadorMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Computador : {}", id);
        computadorRepository.deleteById(id);
    }
}
