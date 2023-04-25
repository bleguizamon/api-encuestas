package co.gov.sic.encuestas.repository;

import co.gov.sic.encuestas.domain.FormularioEncuesta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the FormularioEncuesta entity.
 */
@Repository
public interface FormularioEncuestaRepository extends JpaRepository<FormularioEncuesta, Long> {
    default Optional<FormularioEncuesta> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<FormularioEncuesta> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<FormularioEncuesta> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct formularioEncuesta from FormularioEncuesta formularioEncuesta left join fetch formularioEncuesta.marcaFavoritaPC",
        countQuery = "select count(distinct formularioEncuesta) from FormularioEncuesta formularioEncuesta"
    )
    Page<FormularioEncuesta> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select distinct formularioEncuesta from FormularioEncuesta formularioEncuesta left join fetch formularioEncuesta.marcaFavoritaPC"
    )
    List<FormularioEncuesta> findAllWithToOneRelationships();

    @Query(
        "select formularioEncuesta from FormularioEncuesta formularioEncuesta left join fetch formularioEncuesta.marcaFavoritaPC where formularioEncuesta.id =:id"
    )
    Optional<FormularioEncuesta> findOneWithToOneRelationships(@Param("id") Long id);
}
