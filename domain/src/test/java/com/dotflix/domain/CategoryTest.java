package com.dotflix.domain;

import com.dotflix.domain.category.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.Instant;

/**
 * TDD class for implementation of Category com.dotflix.domain.lixo.Entity
 */
public class CategoryTest {
    /**
     * Test method for new Category instance
     */
    @Test
    public void instantiateNewCategoryTest(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    /**
     * Test method for new Category instance with null name, expecting 1 error
     */
    @Test
    public void invalidNullNameTest() {
        final String expectedErrorMessage = "'name' should not be null";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(null, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(Exception.class, actualCategory::validate);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void invalidEmptyNameTest() {
        final String expectedName = " ";
        final String expectedErrorMessage = "'name' should not be empty";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(Exception.class, actualCategory::validate);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void invalidLengthLessThan3CharNameTest() {
        final String expectedName = "Jo ";
        final String expectedErrorMessage = "'name' should have between 3 and 255 characters";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(Exception.class, actualCategory::validate);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void invalidLengthMoreThan255CharNameTest() {
        final String expectedName = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghisdsf";
        final String expectedErrorMessage = "'name' should have between 3 and 255 characters";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(Exception.class, actualCategory::validate);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void validEmptyDescriptionTest() {
        final String expectedName = "Filmao de Pedro";
        final String expectedDescription = " ";
        final boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(actualCategory::validate);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void validIsActiveFalseTest() {
        final String expectedName = "Filmao de Pedro";
        final String expectedDescription = "descricao";
        final boolean expectedIsActive = false;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(actualCategory::validate);

        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());    // It's deleted by default with isActive = false
    }

    @Test
    public void disableAnActiveCategoryTest(){
        final String expectedName = "Filmao de Pedro";
        final String expectedDescription = "descricao";
        final boolean expectedIsActive = false;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(actualCategory::validate);

        final Instant updatedAt = actualCategory.getUpdatedAt();

        Assertions.assertTrue(actualCategory.getIsActive());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final var aCategory = actualCategory.deactivate();

        Assertions.assertDoesNotThrow(actualCategory::validate);

        Assertions.assertNotNull(aCategory.getId());
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, aCategory.getName());
        Assertions.assertEquals(expectedDescription, aCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, aCategory.getIsActive());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(aCategory.getDeletedAt());
    }

    @Test
    public void activeADisabledCategoryTest(){
        final String expectedName = "Filmao de Pedro";
        final String expectedDescription = "descricao";
        final boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, false);

        Assertions.assertDoesNotThrow(actualCategory::validate);

        final Instant updatedAt = actualCategory.getUpdatedAt();

        Assertions.assertFalse(actualCategory.getIsActive());
        Assertions.assertNotNull(actualCategory.getDeletedAt());

        final var aCategory = actualCategory.activate();

        Assertions.assertDoesNotThrow(actualCategory::validate);

        Assertions.assertNotNull(aCategory.getId());
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, aCategory.getName());
        Assertions.assertEquals(expectedDescription, aCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, aCategory.getIsActive());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(aCategory.getDeletedAt());
    }

    @Test
    public void updateAValidCategoryTest(){
        final String expectedName = "Filmao de Pedro";
        final String expectedDescription = "descricao";
        final boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory("film", "sla", false);

        Assertions.assertDoesNotThrow(actualCategory::validate);

        final Instant updatedAt = actualCategory.getUpdatedAt();

        final Category aCategory = actualCategory.update(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(actualCategory::validate);

        Assertions.assertNotNull(aCategory.getId());
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, aCategory.getName());
        Assertions.assertEquals(expectedDescription, aCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, aCategory.getIsActive());
        Assertions.assertNotNull(aCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isAfter(updatedAt));
        Assertions.assertNull(aCategory.getDeletedAt());
    }

    @Test
    public void updateAValidCategoryWithFalseIsActivate(){
        // A category active turns into inactive
        // Do later
    }
}
