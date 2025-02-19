package com.dotflix.domain.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class GenreTest {
    /* CREATE GENRE TESTS*/
    @Test
    public void createNewGenreTest() throws Exception{
        // Arrange
        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final int expectedCategories = 0;

        // Act
        final Genre actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        // Assert
        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void createNewGenreWithInvalidNullNameTest() {
        // Arrange
        final boolean expectedIsActive = true;
        final String expectedErrorMessage = "'name' should not be null";

        // Act
        Assertions.assertDoesNotThrow(() -> Genre.newGenre(null, expectedIsActive));
    }

    @Test
    public void createNewGenreWithInvalidEmptyNameTest() {
        // Arrange
        final String expectedName = " ";
        final boolean expectedIsActive = true;
        final String expectedErrorMessage = "'name' should not be empty";

        // Act
        Assertions.assertDoesNotThrow(() -> Genre.newGenre(expectedName, expectedIsActive));
    }

    @Test
    public void createNewGenreWithInvalidLongNameTest() {
        // Arrange
        final String expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """;
        final boolean expectedIsActive = true;
        final String expectedErrorMessage = "'name' should have between 3 and 255 characters";

        // Act
        Assertions.assertDoesNotThrow(() -> Genre.newGenre(expectedName, expectedIsActive));
    }

    @Test
    public void deactivateGenreTest() throws Exception{
        // Arrange
        final String expectedName = "Ação";
        final boolean expectedIsActive = false;
        final int expectedCategories = 0;

        final Genre actualGenre = Genre.newGenre(expectedName, true);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final Instant actualCreatedAt = actualGenre.getCreatedAt();
        final Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        // Act
        actualGenre.deactivate();

        // Assert
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void activateGenreTest() throws Exception{
        // Arrange
        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final int expectedCategories = 0;

        final Genre actualGenre = Genre.newGenre(expectedName, false);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final Instant actualCreatedAt = actualGenre.getCreatedAt();
        final Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        // Act
        actualGenre.activate();

        // Assert
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void updateGenreWithDeactivateGenreTest() throws Exception{
        // Arrange
        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final var expectedCategories = List.of("123");

        final Genre actualGenre = Genre.newGenre("acao", false);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final Instant actualCreatedAt = actualGenre.getCreatedAt();
        final Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        // Act
        try {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);

            // Assert
            Assertions.assertNotNull(actualGenre.getId());
            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
            Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
            Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
            Assertions.assertNull(actualGenre.getDeletedAt());
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    @Test
    public void updateGenreWithInvalidEmptyNameTest() throws Exception{
        // Arrange
        final String expectedName = " ";
        final boolean expectedIsActive = true;
        final var expectedCategories = List.of("123");
        final String expectedErrorMessage = "'name' should not be empty";

        final Genre actualGenre = Genre.newGenre("acao", false);

        // Act
        final Exception actualException = Assertions.assertThrows(Exception.class, () -> actualGenre.update(expectedName, expectedIsActive, expectedCategories));

        // Assert
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void updateGenreWithInvalidNullNameTest() throws Exception{
        // Arrange
        final boolean expectedIsActive = true;
        final var expectedCategories = List.of("123");
        final String expectedErrorMessage = "'name' should not be null";

        final Genre actualGenre = Genre.newGenre("acao", false);

        // Act
        final var actualException = Assertions.assertThrows(Exception.class, () -> actualGenre.update(null, expectedIsActive, expectedCategories));

        // Assert
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void updateGenreWithInvalidNullCategoriesTest() throws Exception{
        // Arrange
        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final ArrayList<String> expectedCategories = new ArrayList<>();

        final Genre actualGenre = Genre.newGenre("acao", expectedIsActive);

        final Instant actualCreatedAt = actualGenre.getCreatedAt();
        final Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        // Act
        Assertions.assertDoesNotThrow(() -> {
            actualGenre.update(expectedName, expectedIsActive, null);
        });

        // Assert
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void addCategoryTest() throws Exception{
        // Arrange
        final String seriesID = "123";
        final String moviesID = "456";

        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final List<String> expectedCategories = List.of(seriesID, moviesID);

        final Genre actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final Instant actualCreatedAt = actualGenre.getCreatedAt();
        final Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        // Act
        actualGenre.addCategory(seriesID);
        actualGenre.addCategory(moviesID);

        // Assert
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void addCategoriesWithInvalidIdsTest() throws Exception{
        // Arrange
        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final ArrayList<String> expectedCategories = new ArrayList<>();

        final Genre actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.getCategories().size());

        final Instant actualCreatedAt = actualGenre.getCreatedAt();
        final Instant actualUpdatedAt = actualGenre.getUpdatedAt();

        // Act
        actualGenre.addCategory(null);

        // Assert
        Assertions.assertNotNull(actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void removeCategoryTest() throws Exception{
        // Arrange
        final String seriesID = "123";
        final String moviesID = "456";

        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final List<String> expectedCategories = List.of(moviesID);

        final Genre actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        try {
            actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));

            Assertions.assertEquals(2, actualGenre.getCategories().size());

            final Instant actualCreatedAt = actualGenre.getCreatedAt();
            final Instant actualUpdatedAt = actualGenre.getUpdatedAt();

            // Act
            actualGenre.removeCategory(seriesID);

            // Assert
            Assertions.assertNotNull(actualGenre.getId());
            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
            Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
            Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
            Assertions.assertNull(actualGenre.getDeletedAt());
        } catch (Exception e) {
            System.out.println("Erro: " + e);
        }
    }

    @Test
    public void givenAnInvalidNullAsCategoryID_whenCallRemoveCategory_shouldReceiveOK() throws Exception{
        // Arrange
        final String seriesID = "123";
        final String moviesID = "456";

        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final List<String> expectedCategories = List.of(seriesID, moviesID);

        final Genre actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        try {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);

            Assertions.assertEquals(2, actualGenre.getCategories().size());

            final var actualCreatedAt = actualGenre.getCreatedAt();
            final var actualUpdatedAt = actualGenre.getUpdatedAt();

            // Act
            actualGenre.removeCategory(null);

            // Assert
            Assertions.assertNotNull(actualGenre.getId());
            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
            Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
            Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
            Assertions.assertNull(actualGenre.getDeletedAt());
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }
}
