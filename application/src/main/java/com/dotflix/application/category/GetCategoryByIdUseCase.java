package com.dotflix.application.category;

import com.dotflix.application.UseCase;
import com.dotflix.application.category.dto.GetCategoryByIdDTO;
import com.dotflix.application.category.exceptions.CategoryNotFoundException;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import java.util.Objects;

public class GetCategoryByIdUseCase extends UseCase<GetCategoryByIdDTO, Category> {
    private final CategoryGateway categoryGateway;

    public GetCategoryByIdUseCase(final CategoryGateway categoryGateway){
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Category execute(final GetCategoryByIdDTO getCategoryByIdDTO) throws CategoryNotFoundException {
        return this.categoryGateway.findById(getCategoryByIdDTO.id()).orElseThrow(() -> new CategoryNotFoundException("A categoria não foi encontrada."));    // Adicionar exception
    }
}
