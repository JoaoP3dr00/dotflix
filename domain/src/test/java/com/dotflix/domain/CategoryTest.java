package com.dotflix.domain;

import com.dotflix.domain.category.Category;
import com.dotflix.domain.exceptions.DomainException;
import com.dotflix.domain.validation.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * TDD class for implementation of Category com.dotflix.domain.Entity
 */
public class CategoryTest {
    /**
     * Test method for new Category instance
     */
    @Test
    public void instantiateNewCategoryTest(){
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

    /**
     * Test method for new Category instance with null name, expecting 1 error
     */
    @Test
    public void invalidNullNameTest(){
        final String expectedName = null;
        final Integer expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should not be null";
        final String expectedDescription = "A categoria mais assistida";
        final Boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    }
}
