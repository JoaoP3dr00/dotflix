package com.dotflix.application.category;

import com.dotflix.application.UseCase;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.category.CategorySearchQuery;
import com.dotflix.domain.Pagination;

public class GetAllCategoriesUseCase extends UseCase<CategorySearchQuery, Pagination<Category>> {
    private final CategoryGateway categoryGateway;

    public GetAllCategoriesUseCase(final CategoryGateway categoryGateway){
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<Category> execute(CategorySearchQuery categorySearchQuery) {
        return this.categoryGateway.findAll(categorySearchQuery);
    }
}

