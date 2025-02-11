package com.dotflix.application.category;

import com.dotflix.application.UseCase;
import com.dotflix.application.category.dto.DeleteCategoryDTO;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import java.util.Objects;

public class DeleteCategoryUseCase extends UseCase<DeleteCategoryDTO, String> {
    private final CategoryGateway categoryGateway;

    public DeleteCategoryUseCase(final CategoryGateway categoryGateway){
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public String execute(final DeleteCategoryDTO deleteCategoryDTO) {
        try{
            this.categoryGateway.deleteById(deleteCategoryDTO.id());
            return "ok";
        }catch (Exception e){
            System.out.println(e);
            return "Error";
        }
    }

}
