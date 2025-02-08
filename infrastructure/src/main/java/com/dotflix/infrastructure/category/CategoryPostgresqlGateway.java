package com.dotflix.infrastructure.category;

import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.category.CategorySearchQuery;
import com.dotflix.domain.lixo.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CategoryPostgresqlGateway implements CategoryGateway {
    @Autowired
    private final CategoryRepository repository;

    public CategoryPostgresqlGateway(final CategoryRepository repository){
        this.repository = repository;
    }

    @Override
    public Category create(final Category category) {
        return this.repository.save(CategoryEntity.fromDomain(category)).toDomain();
    }

    @Override
    public void deleteById(String id) {

    }

    @Override
    public Optional<Category> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Category update(Category category) {
        return null;
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {
        return null;
    }
}
