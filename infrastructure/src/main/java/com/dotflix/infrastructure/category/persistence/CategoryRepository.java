package com.dotflix.infrastructure.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, String> {
    Page<CategoryEntity> findAll(Specification<CategoryEntity> whereClause, Pageable page);

    @Query(value = "select c.id from CategoryEntity c where c.id in :ids")
    List<String> existsByIds(@Param("ids") List<String> ids);
}
