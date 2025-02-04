package com.dotflix.domain;

import com.dotflix.domain.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test TDD class for implementation of Category com.dotflix.domain.Entity
 */
public class CategoryTest {
    @Test
    public void testNewCategory() {
        Assertions.assertNotNull(new Category());
    }

    /**
     * Test method for new Category instance
     */
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
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }
}
