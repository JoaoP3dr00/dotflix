package com.dotflix.infrastructure.genre.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface GenreRepository extends JpaRepository<GenreEntity, String> {
    Page<GenreEntity> findAll(Specification<GenreEntity> whereClause, Pageable page);

    @Query(value = "select g.id from Genre g where g.id in :ids")
    List<String> existsByIds(@Param("ids") List<String> ids);
}
