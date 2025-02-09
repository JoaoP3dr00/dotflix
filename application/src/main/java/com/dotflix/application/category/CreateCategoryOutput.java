package com.dotflix.application.category;

import com.dotflix.domain.category.Category;

public record CreateCategoryOutput(String id) {
    public static CreateCategoryOutput from(final Category category){
        return new CreateCategoryOutput(category.getId());
    }
}
