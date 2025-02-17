package com.dotflix.infrastructure.category.controller.presenter;

import com.dotflix.domain.category.Category;
import com.dotflix.infrastructure.category.controller.dto.CategoryGetAllResponse;
import com.dotflix.infrastructure.category.controller.dto.CategoryResponse;

public interface CategoryApiPresenter {

    static CategoryResponse present(final Category output) {
        return new CategoryResponse(
                output.getId(),
                output.getName(),
                output.getDescription(),
                output.getIsActive(),
                output.getCreatedAt(),
                output.getUpdatedAt(),
                output.getDeletedAt()
        );
    }

    static CategoryGetAllResponse presentGetAll(final Category output) {
        return new CategoryGetAllResponse(
                output.getId(),
                output.getName(),
                output.getDescription(),
                output.getIsActive(),
                output.getCreatedAt(),
                output.getDeletedAt()
        );
    }
}