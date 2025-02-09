package com.dotflix.domain.category;

import com.dotflix.domain.lixo.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category category);

    void deleteById(String id);

    Optional<Category> findById(String id);

    Category update(Category category);

    Pagination<Category> findAll(CategorySearchQuery query);
}
