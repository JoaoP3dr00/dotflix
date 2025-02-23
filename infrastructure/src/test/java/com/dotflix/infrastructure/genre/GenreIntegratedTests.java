//package com.dotflix.infrastructure.genre;
//
//import com.dotflix.application.genre.*;
//import com.dotflix.application.genre.dto.CreateGenreDTO;
//import com.dotflix.application.genre.dto.UpdateGenreDTO;
//import com.dotflix.application.genre.excpetions.GenreNotFoundException;
//import com.dotflix.domain.Pagination;
//import com.dotflix.domain.SearchQuery;
//import com.dotflix.domain.category.Category;
//import com.dotflix.domain.category.CategoryGateway;
//import com.dotflix.domain.genre.Genre;
//import com.dotflix.domain.genre.GenreGateway;
//import com.dotflix.infrastructure.IntegrationTest;
//import com.dotflix.infrastructure.genre.persistence.GenreEntity;
//import com.dotflix.infrastructure.genre.persistence.GenreRepository;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import java.util.List;
//
//@IntegrationTest
//public class GenreIntegratedTests {
//    @Autowired
//    private CreateGenreUseCase createGenreUseCase;
//
//    @Autowired
//    private GetGenreByIdUseCase getGenreByIdUseCase;
//
//    @Autowired
//    private GetAllGenreUseCase getAllCategoriesUseCase;
//
//    @Autowired
//    private UpdateGenreUseCase updateGenreUseCase;
//
//    @Autowired
//    private DeleteGenreUseCase deleteGenreUseCase;
//
//    @InjectMocks
//    private CategoryGateway categoryGateway;
//
//    @Autowired
//    private GenreGateway genreGateway;
//
//    @InjectMocks
//    private GenreRepository genreRepository;
//
//    /* CREATE GENRE TESTS */
//
//    @Test
//    public void createGenreTest() throws Exception {
//        // Arrange
//        final Category filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
//
//        final String expectedName = "Ação";
//        final boolean expectedIsActive = true;
//        final List<String> expectedCategories = List.of(filmes.getId());
//
//        final CreateGenreDTO aCommand = new CreateGenreDTO(expectedName, expectedIsActive, expectedCategories);
//
//        // Act
//        final var actualOutput = createGenreUseCase.execute(aCommand);
//
//        // Assert
//        Assertions.assertNotNull(actualOutput);
//        Assertions.assertNotNull(actualOutput.getId());
//
//        final GenreEntity actualGenre = genreRepository.findById(actualOutput.getId()).get();
//
//        Assertions.assertEquals(expectedName, actualGenre.getName());
//        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
//        Assertions.assertTrue(
//                expectedCategories.size() == actualGenre.getCategoryIDs().size()
//                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
//        );
//        Assertions.assertNotNull(actualGenre.getCreatedAt());
//        Assertions.assertNotNull(actualGenre.getUpdatedAt());
//        Assertions.assertNull(actualGenre.getDeletedAt());
//    }
//
//    @Test
//    public void createGenreWithInvalidEmptyNameTest() {
//        // Arrange
//        final String expectedName = " ";
//        final boolean expectedIsActive = true;
//        final List<String> expectedCategories = List.of();
//
//        final String expectedErrorMessage = "'name' should not be empty";
//
//        final CreateGenreDTO aCommand = new CreateGenreDTO(expectedName, expectedIsActive, expectedCategories);
//
//        // Act
//        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
//            createGenreUseCase.execute(aCommand);
//        });
//
//        // Assert
//        Assertions.assertNotNull(actualException);
//        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
//
//        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
//        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
//    }
//
//    @Test
//    public void createGenreWithInvalidNullNameTest() {
//        // Arrange
//        final boolean expectedIsActive = true;
//        final List<String> expectedCategories = List.of();
//
//        final String expectedErrorMessage = "'name' should not be null";
//
//        final CreateGenreDTO aCommand = new CreateGenreDTO(null, expectedIsActive, expectedCategories);
//
//        // Act
//        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
//            createGenreUseCase.execute(aCommand);
//        });
//
//        // Assert
//        Assertions.assertNotNull(actualException);
//        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
//
//        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
//        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
//    }
//
//    @Test
//    public void createGenreWithInvalidNameAndCategoriesNonExistentTest() {
//        // Arrange
//        final Category series = categoryGateway.create(Category.newCategory("Séries", null, true));
//
//        final String filmes = "456";
//        final String documentarios = "789";
//
//        final String expectName = " ";
//        final boolean expectedIsActive = true;
//        final List<String> expectedCategories = List.of(filmes, series.getId(), documentarios);
//
//        final String expectedErrorMessageOne = "'name' should not be empty";
//        final String expectedErrorMessageTwo = "Some categories could not be found: [456, 789]";
//
//        final CreateGenreDTO aCommand = new CreateGenreDTO(expectName, expectedIsActive, expectedCategories);
//
//        // Act
//        final var actualException = Assertions.assertThrows(Exception.class, () -> {
//            createGenreUseCase.execute(aCommand);
//        });
//
//        // Assert
//        Assertions.assertNotNull(actualException);
//        Assertions.assertEquals(expectedErrorMessageOne, actualException.getMessage());
//
//        final CreateGenreDTO command = new CreateGenreDTO("nome", expectedIsActive, expectedCategories);
//
//        final var exception = Assertions.assertThrows(Exception.class, () -> {
//            createGenreUseCase.execute(command);
//        });
//
//        Assertions.assertNotNull(exception);
//        Assertions.assertEquals(expectedErrorMessageTwo, exception.getMessage());
//
//        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
//        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
//    }
//
//    /* DELETE GENRE TESTS */
//
//    @Test
//    public void deleteGenreTest() throws Exception {
//        // Arrange
//        final Genre aGenre = genreGateway.create(Genre.newGenre("Ação", true));
//
//        final String expectedId = aGenre.getId();
//
//        Assertions.assertEquals(1, genreRepository.count());
//
//        // Act
//        Assertions.assertDoesNotThrow(() -> deleteGenreUseCase.execute(expectedId));
//
//        // when
//        Assertions.assertEquals(0, genreRepository.count());
//    }
//
//    /* GET GENRES TESTS */
//
//    @Test
//    public void getGenreByIdTest() throws Exception {
//        // Arrange
//        final Category series = categoryGateway.create(Category.newCategory("Séries", null, true));
//
//        final Category filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
//
//        final String expectedName = "Ação";
//        final boolean expectedIsActive = true;
//        final List<String> expectedCategories = List.of(series.getId(), filmes.getId());
//
//        final Genre aGenre = genreGateway.create(
//                Genre.newGenre(expectedName, expectedIsActive)
//                        .addCategories(expectedCategories)
//        );
//
//        final String expectedId = aGenre.getId();
//
//        // Act
//        final Genre actualGenre = getGenreByIdUseCase.execute(expectedId);
//
//        // Assert
//        Assertions.assertEquals(expectedId, actualGenre.getId());
//        Assertions.assertEquals(expectedName, actualGenre.getName());
//        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
//        Assertions.assertTrue(expectedCategories.size() == actualGenre.getCategories().size() && expectedCategories.containsAll(actualGenre.getCategories()));
//        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
//        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
//        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
//    }
//
//    @Test
//    public void getGenreAndDoesNotExistsTest() {
//        // Arrange
//        final String expectedErrorMessage = "Genre with ID 123 was not found";
//
//        final String expectedId = "123";
//
//        // Act
//        final Exception actualException = Assertions.assertThrows(GenreNotFoundException.class, () -> {
//            getGenreByIdUseCase.execute(expectedId);
//        });
//
//        // Assert
//        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
//    }
//
//    @Test
//    public void getAllGenresTest() throws Exception {
//        // Arrange
//        final List<Genre> genres = List.of(
//                Genre.newGenre("Ação", true),
//                Genre.newGenre("Aventura", true)
//        );
//
//        genreRepository.saveAllAndFlush(
//                genres.stream()
//                        .map(GenreEntity::fromDomain)
//                        .toList()
//        );
//
//        final int expectedPage = 0;
//        final int expectedPerPage = 10;
//        final String expectedTerms = "A";
//        final String expectedSort = "createdAt";
//        final String expectedDirection = "asc";
//        final int expectedTotal = 2;
//
//        final List<Genre> expectedItems = genres.stream().toList();
//
//        final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
//
//        // Act
//        final Pagination<Genre> actualOutput = getAllCategoriesUseCase.execute(aQuery);
//
//        // Assert
//        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
//        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
//        Assertions.assertEquals(expectedTotal, actualOutput.total());
//        Assertions.assertTrue(
//                expectedItems.size() == actualOutput.items().size()
//                        && expectedItems.containsAll(actualOutput.items())
//        );
//    }
//
//    /* UPDATE GENRES TESTS */
//
//    @Test
//    public void updateGenreWithCategoriesTest() throws Exception {
//        // Arrange
//        final Category filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
//
//        final Category series = categoryGateway.create(Category.newCategory("Séries", null, true));
//
//        final Genre aGenre = genreGateway.create(Genre.newGenre("acao", true));
//
//        final String expectedId = aGenre.getId();
//        final String expectedName = "Ação";
//        final boolean expectedIsActive = true;
//        final List<String> expectedCategories = List.of(filmes.getId(), series.getId());
//
//        final UpdateGenreDTO aCommand = new UpdateGenreDTO(expectedId, expectedName, expectedIsActive, expectedCategories);
//
//        // Act
//        final Genre actualOutput = updateGenreUseCase.execute(aCommand);
//
//        // Assert
//        Assertions.assertNotNull(actualOutput);
//        Assertions.assertEquals(expectedId, actualOutput.getId());
//
//        final GenreEntity actualGenre = genreRepository.findById(aGenre.getId()).get();
//
//        Assertions.assertEquals(expectedName, actualGenre.getName());
//        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
//        Assertions.assertTrue(
//                expectedCategories.size() == actualGenre.getCategoryIDs().size()
//                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
//        );
//        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
//        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
//        Assertions.assertNull(actualGenre.getDeletedAt());
//    }
//
//    @Test
//    public void updateGenreWithInvalidNameTest() throws Exception {
//        // Arrange
//        final Genre aGenre = genreGateway.create(Genre.newGenre("acao", true));
//
//        final String expectedId = aGenre.getId();
//        final boolean expectedIsActive = true;
//        final List<String> expectedCategories = List.of();
//
//        final String expectedErrorMessage = "'name' should not be null";
//
//        final UpdateGenreDTO aCommand = new UpdateGenreDTO(expectedId, null, expectedIsActive, expectedCategories);
//
//        // Act
//        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
//            updateGenreUseCase.execute(aCommand);
//        });
//
//        // Assert
//        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
//
//        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
//
//        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
//
//        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
//    }
//}
