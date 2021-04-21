package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Overview;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Overview entity.
 */
@Repository
public interface OverviewRepository extends JpaRepository<Overview, Long> {
    @Query(
        value = "select distinct overview from Overview overview left join fetch overview.gebruikers left join fetch overview.documentTypes",
        countQuery = "select count(distinct overview) from Overview overview"
    )
    Page<Overview> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct overview from Overview overview left join fetch overview.gebruikers left join fetch overview.documentTypes")
    List<Overview> findAllWithEagerRelationships();

    @Query(
        "select overview from Overview overview left join fetch overview.gebruikers left join fetch overview.documentTypes where overview.id =:id"
    )
    Optional<Overview> findOneWithEagerRelationships(@Param("id") Long id);
}
