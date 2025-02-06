package com.dotflix.application.category;

import com.dotflix.domain.category.CategoryGateway;

import java.util.Objects;

public class ImplCreateCategoryUseCase extends CreateCategoryUseCase{
    private final CategoryGateway categoryGateway;

    public ImplCreateCategoryUseCase(final CategoryGateway categoryGateway){
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateCategoryOutput execute(final CreateCategoryCommand createCategoryCommand) {
        return null;
    }
}
