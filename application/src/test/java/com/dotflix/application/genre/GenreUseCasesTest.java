package com.dotflix.application.genre;

import com.dotflix.application.UseCaseTest;
import com.dotflix.application.genre.dto.CreateGenreDTO;
import com.dotflix.application.genre.dto.UpdateGenreDTO;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.genre.Genre;
import com.dotflix.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class GenreUseCasesTest extends UseCaseTest {
    @InjectMocks
    private CreateGenreUseCase createGenreUseCase;

    @InjectMocks DeleteGenreUseCase deleteGenreUseCase;

    @InjectMocks GetGenreByIdUseCase getGenreByIdUseCase;

    @InjectMocks GetAllGenreUseCase getAllGenresUseCase;

    @InjectMocks UpdateGenreUseCase updateGenreUseCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    /* CREATE USE CASE TESTS */

    @Test
    public void createGenreTest() throws Exception{
        // Arrange
        final var expectName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<String>of();

        final CreateGenreDTO aCommand = new CreateGenreDTO(expectName, expectedIsActive, expectedCategories);

        Mockito.when(genreGateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Act
        final Genre actualOutput = createGenreUseCase.execute(aCommand);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.getId());

        Mockito.verify(genreGateway, Mockito.times(1)).create(Mockito.argThat(aGenre ->
                Objects.equals(expectName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    public void createGenreWithCategoriesTest() throws Exception {
        // Arrange
        final String expectName = "Ação";
        final boolean expectedIsActive = true;
        final List<String> expectedCategories = List.of("123", "456");

        final CreateGenreDTO aCommand = new CreateGenreDTO(expectName, expectedIsActive, expectedCategories);

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(expectedCategories);

        Mockito.when(genreGateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Act
        final Genre actualOutput = createGenreUseCase.execute(aCommand);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.getId());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(expectedCategories);

        Mockito.verify(genreGateway, Mockito.times(1)).create(Mockito.argThat(aGenre ->
                Objects.equals(expectName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    public void createGenreWithInvalidEmptyNameTest() {
        // Arrange
        final String expectName = " ";
        final boolean expectedIsActive = true;
        final List<String> expectedCategories = List.of();

        final String expectedErrorMessage = "'name' should not be empty";

        final CreateGenreDTO aCommand = new CreateGenreDTO(expectName, expectedIsActive, expectedCategories);

        // Act
        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
            createGenreUseCase.execute(aCommand);
        });

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
    }

    // Test invalid null name

    @Test
    public void createGenreWithInvalidNonexistentCategoryTest() {
        // Arrange
        final String filmes = "456";
        final String series = "123";
        final String documentarios = "789";

        final String expectName = "Ação";
        final boolean expectedIsActive = true;
        final List<String> expectedCategories = List.of(filmes, series, documentarios);

        final String expectedErrorMessage = "Some categories could not be found: [456, 789]";

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(series));

        final CreateGenreDTO aCommand = new CreateGenreDTO(expectName, expectedIsActive, expectedCategories);

        // Act
        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
            createGenreUseCase.execute(aCommand);
        });

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).create(Mockito.any());
    }

    /* DELETE USE CASE TESTS */
    @Test
    public void deleteGenreUseCaseTest() throws Exception{
        // Arrange
        final Genre aGenre = Genre.newGenre("Ação", true);

        final String expectedId = aGenre.getId();

        Mockito.doNothing().when(genreGateway).deleteById(Mockito.any());

        // Act
        Assertions.assertDoesNotThrow(() -> deleteGenreUseCase.execute(expectedId));

        // Assert
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }

    @Test
    public void deleteGenreWithInvalidIdUseCaseTest() {
        // Arrange
        final String expectedId = "123";

        Mockito.doNothing().when(genreGateway).deleteById(Mockito.any());

        // Act
        Assertions.assertDoesNotThrow(() -> deleteGenreUseCase.execute(expectedId));

        // Assert
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }

    @Test
    public void deleteGenreWhenGatewayThrowsErrorTest() throws Exception {
        // Arrange
        final Genre aGenre = Genre.newGenre("Ação", true);
        final String expectedId = aGenre.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error")).when(genreGateway).deleteById(Mockito.any());

        // Act
        Assertions.assertThrows(IllegalStateException.class, () -> {
            deleteGenreUseCase.execute(expectedId);
        });

        // Assert
        Mockito.verify(genreGateway, Mockito.times(1)).deleteById(expectedId);
    }

    /* GET USE CASE TESTS */
    @Test
    public void getGenreUseCaseTest() throws Exception {
        // Arrange
        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final List<String > expectedCategories = List.of("123", "456");

        final Genre aGenre = Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories);

        final String expectedId = aGenre.getId();

        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(aGenre));

        // Act
        try {
            final Genre actualGenre = getGenreByIdUseCase.execute(expectedId);

            // Assert
            Assertions.assertEquals(expectedId, actualGenre.getId());
            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
            Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
            Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
            Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

            Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
        }catch (Exception e){
            System.out.println("Erro: " + e);
        }
    }

    @Test
    public void getGenreAndDoesNotExistsTest() {
        // Arrange
        final String expectedErrorMessage = "Genre with ID 123 was not found";

        final String expectedId = "123";

        Mockito.when(genreGateway.findById(Mockito.eq(expectedId))).thenReturn(Optional.empty());

        // Act
        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
            getGenreByIdUseCase.execute(expectedId);
        });

        // Assert
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    /* GET ALL GENRES USE CASE TESTS */
    @Test
    public void getAllGenresUseCaseTest() throws Exception {
        // Arrange
        final List<Genre> genres = List.of(Genre.newGenre("Ação", true), Genre.newGenre("Aventura", true));

        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "A";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";
        final int expectedTotal = 2;

        final List<Genre> expectedItems = genres.stream().toList();

        final Pagination expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, genres);

        Mockito.when(genreGateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

        final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final Pagination actualOutput = getAllGenresUseCase.execute(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(genreGateway, Mockito.times(1)).findAll(Mockito.eq(aQuery));
    }

    @Test
    public void getAllGenresWhenGatewayThrowsExceptionTest() {
        // given
        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "A";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";

        final String expectedErrorMessage = "Gateway error";

        Mockito.when(genreGateway.findAll(Mockito.any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final Exception actualOutput = Assertions.assertThrows(
                IllegalStateException.class,
                () -> getAllGenresUseCase.execute(aQuery)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        Mockito.verify(genreGateway, Mockito.times(1)).findAll(Mockito.eq(aQuery));
    }

    /* UPDATE USE CASE TESTS */
    @Test
    public void updateGenreUseCaseTest() throws Exception {
        // Arrange
        final Genre aGenre = Genre.newGenre("acao", true);

        final String expectedId = aGenre.getId();
        final String expectedName = "Ação";
        final boolean expectedIsActive = true;
        final List<String> expectedCategories = List.of();

        final UpdateGenreDTO updateGenreDTO = new UpdateGenreDTO(expectedId, expectedName, expectedIsActive, expectedCategories);

        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(aGenre));

        Mockito.when(genreGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Act
        try {
            final Genre actualOutput = updateGenreUseCase.execute(updateGenreDTO);

            // Assert
            Assertions.assertNotNull(actualOutput);
            Assertions.assertEquals(expectedId, actualOutput.getId());

            Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

            Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(aUpdatedGenre ->
                    Objects.equals(expectedId, aUpdatedGenre.getId())
                            && Objects.equals(expectedName, aUpdatedGenre.getName())
                            && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                            && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                            && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                            //&& aUpdatedGenre.getUpdatedAt().isBefore(actualOutput.getUpdatedAt())
                            && Objects.isNull(aUpdatedGenre.getDeletedAt())
            ));
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // update with catgories

    @Test
    public void updateGenreWithInvalidNameTest() throws Exception {
        // given
        final Genre aGenre = Genre.newGenre("acao", true);

        final String expectedId = aGenre.getId();
        final String expectedName = null;
        final boolean expectedIsActive = true;
        final List<String> expectedCategories = List.of();

        final String expectedErrorMessage = "'name' should not be null";

        final UpdateGenreDTO aCommand = new UpdateGenreDTO(expectedId, expectedName, expectedIsActive, expectedCategories);

        Mockito.when(genreGateway.findById(Mockito.any())).thenReturn(Optional.of(aGenre));

        // when
        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
            updateGenreUseCase.execute(aCommand);
        });

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());

        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void updateGenreWhenSomeCategoriesNonexistsTest() throws Exception {
        // given
        final String filmes = "123";
        final String series = "456";
        final String documentarios = "789";

        final Genre aGenre = Genre.newGenre("acao", true);

        final String expectedId = aGenre.getId();
        final String expectedName = null;
        final boolean expectedIsActive = true;
        final List<String > expectedCategories = List.of(filmes, series, documentarios);

        final String expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final String expectedErrorMessageTwo = "'name' should not be null";

        final UpdateGenreDTO aCommand = new UpdateGenreDTO(expectedId, expectedName, expectedIsActive, expectedCategories);

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre));

        Mockito.when(categoryGateway.existsByIds(Mockito.any())).thenReturn(List.of(filmes));

        // when
        final var actualException = Assertions.assertThrows(Exception.class, () -> {
            updateGenreUseCase.execute(aCommand);
        });

        // then
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getMessage());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedCategories));

        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }
}
