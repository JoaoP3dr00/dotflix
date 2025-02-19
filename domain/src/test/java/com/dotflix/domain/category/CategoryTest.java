package com.dotflix.domain.category;

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
    public void instantiateNewCategoryTest()throws Exception{
        // Arrange
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        // Act
        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // Arrange
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
        // Arrange
        final String expectedErrorMessage = "'name' should not be null";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        // Act
        Assertions.assertDoesNotThrow(() -> Category.newCategory(null, expectedDescription, expectedIsActive));
    }

    @Test
    public void invalidEmptyNameTest() {
        // Arrange
        final String expectedName = " ";
        final String expectedErrorMessage = "'name' should not be empty";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        // Act
        Assertions.assertDoesNotThrow(() -> Category.newCategory(expectedName, expectedDescription, expectedIsActive));
    }

    @Test
    public void invalidLengthLessThan3CharNameTest() {
        // Arrange
        final String expectedName = "Jo ";
        final String expectedErrorMessage = "'name' should have between 3 and 255 characters";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        // Act
        Assertions.assertDoesNotThrow(() -> Category.newCategory(expectedName, expectedDescription, expectedIsActive));
    }

    @Test
    public void invalidLengthMoreThan255CharNameTest() {
        // Arrange
        final String expectedName = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghisdsf";
        final String expectedErrorMessage = "'name' should have between 3 and 255 characters";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        // Act
        Assertions.assertDoesNotThrow(() -> Category.newCategory(expectedName, expectedDescription, expectedIsActive));
    }

    @Test
    public void validEmptyDescriptionTest() throws Exception{
        // Arrange
        final String expectedName = "Filmao de Pedro";
        final String expectedDescription = " ";
        final boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // Act
        Assertions.assertDoesNotThrow(actualCategory::validate);

        // Assert
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
    public void validIsActiveFalseTest() throws Exception{
        // Arrange
        final String expectedName = "Filmao de Pedro";
        final String expectedDescription = "descricao";
        final boolean expectedIsActive = false;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // Act
        Assertions.assertDoesNotThrow(actualCategory::validate);

        // Assert
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
    public void disableAnActiveCategoryTest() throws Exception {
        // Arrange
        final String expectedName = "Filmao de Pedro";
        final String expectedDescription = "descricao";
        final boolean expectedIsActive = false;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, true);

        Assertions.assertDoesNotThrow(actualCategory::validate);

        final Instant updatedAt = actualCategory.getUpdatedAt();

        Assertions.assertTrue(actualCategory.getIsActive());
        Assertions.assertNull(actualCategory.getDeletedAt());

        // Act
        final var aCategory = actualCategory.deactivate();

        // Assert
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
    public void activeADisabledCategoryTest() throws Exception {
        // Arrange
        final String expectedName = "Filmao de Pedro";
        final String expectedDescription = "descricao";
        final boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, false);

        Assertions.assertDoesNotThrow(actualCategory::validate);

        final Instant updatedAt = actualCategory.getUpdatedAt();

        Assertions.assertFalse(actualCategory.getIsActive());
        Assertions.assertNotNull(actualCategory.getDeletedAt());

        // Act
        final var aCategory = actualCategory.activate();


        // Act
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
    public void updateAValidCategoryTest() throws Exception {
        // Arrange
        final String expectedName = "Filmao de Pedro";
        final String expectedDescription = "descricao";
        final boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory("film", "sla", false);

        Assertions.assertDoesNotThrow(actualCategory::validate);

        // Act
        final Instant updatedAt = actualCategory.getUpdatedAt();

        // Assert
        try {
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
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    @Test
    public void updateAValidCategoryWithFalseIsActivate(){
        // A category active turns into inactive
        // Do later
    }
}
