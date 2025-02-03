package com.dotflix.domain.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {
    @Test
    public void testNewCategory() {
        Assertions.assertNotNull(new Category());
    }

    @Test
    public void testInstantiateNewCategory(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final Boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(actualCategory.getName(), expectedName);
        Assertions.assertEquals(actualCategory.getDescription(), expectedDescription);
        Assertions.assertEquals(actualCategory.getIsActive(), expectedIsActive);
    }

}
