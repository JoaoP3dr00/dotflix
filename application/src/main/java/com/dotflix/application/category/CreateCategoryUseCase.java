package com.dotflix.application.category;

import com.dotflix.application.UseCase;
import com.dotflix.application.category.dto.CreateCategoryDTO;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;

import java.util.Objects;

public class CreateCategoryUseCase extends UseCase<CreateCategoryDTO, Category> {
    private final CategoryGateway categoryGateway;

    public CreateCategoryUseCase(final CategoryGateway categoryGateway){
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Category execute(final CreateCategoryDTO createCategoryDTO) {
        final String name = createCategoryDTO.name();
        final String description = createCategoryDTO.description();
        final boolean isActive = createCategoryDTO.isActive();

        final Category category = Category.newCategory(name, description, isActive);

        return this.categoryGateway.create(category);
    }

}
