package com.dotflix.application.category;

import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryID;

public record CreateCategoryOutput(CategoryID id) {
    public static CreateCategoryOutput from(final Category category){
        return new CreateCategoryOutput(category.getId());
    }
}
