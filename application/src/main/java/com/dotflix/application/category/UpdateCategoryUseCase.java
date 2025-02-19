package com.dotflix.application.category;

import com.dotflix.application.UseCase;
import com.dotflix.application.category.dto.UpdateCategoryDTO;
import com.dotflix.application.category.exceptions.CategoryNotFoundException;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;

public class UpdateCategoryUseCase extends UseCase<UpdateCategoryDTO, Category> {
    private final CategoryGateway categoryGateway;

    public UpdateCategoryUseCase(final CategoryGateway categoryGateway){
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Category execute(UpdateCategoryDTO updateCategoryDTO) {
        try {
            final Category category = this.categoryGateway.findById(updateCategoryDTO.id()).orElseThrow(() -> new CategoryNotFoundException("A categoria " + updateCategoryDTO.id() + " não foi encontrada."));

            category.update(updateCategoryDTO.name(), updateCategoryDTO.description(), updateCategoryDTO.isActive());

            this.categoryGateway.update(category);

            return category;
        } catch (Exception e){
            System.out.println("Error: " + e);
            return null;
        }
    }
}
