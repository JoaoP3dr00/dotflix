package com.dotflix.application.category;

import com.dotflix.application.UseCase;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.Pagination;

public class GetAllCategoriesUseCase extends UseCase<SearchQuery, Pagination<Category>> {
    private final CategoryGateway categoryGateway;

    public GetAllCategoriesUseCase(final CategoryGateway categoryGateway){
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<Category> execute(SearchQuery categorySearchQuery) {
        return this.categoryGateway.findAll(categorySearchQuery);
    }
}

