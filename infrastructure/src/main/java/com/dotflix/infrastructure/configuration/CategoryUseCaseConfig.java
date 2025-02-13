package com.dotflix.infrastructure.configuration;

import com.dotflix.application.category.*;
import com.dotflix.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração para permitir que o springboot reconheça as classes de caso de uso como beans para utilização
 */
@Configuration
public class CategoryUseCaseConfig {
    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(final CategoryGateway categoryGateway){
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(){
        return new CreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(){
        return new UpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase(){
        return new GetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public GetAllCategoriesUseCase getAllCategoriesUseCase() {
        return new GetAllCategoriesUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(){
        return new DeleteCategoryUseCase(categoryGateway);
    }
}
