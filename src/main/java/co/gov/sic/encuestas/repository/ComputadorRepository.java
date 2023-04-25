package co.gov.sic.encuestas.repository;

import co.gov.sic.encuestas.domain.Computador;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Computador entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComputadorRepository extends JpaRepository<Computador, Long> {}
