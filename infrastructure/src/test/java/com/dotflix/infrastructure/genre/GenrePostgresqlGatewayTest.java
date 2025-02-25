package com.dotflix.infrastructure.genre;

import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.genre.Genre;
import com.dotflix.infrastructure.PostgresqlGatewayTest;
import com.dotflix.infrastructure.category.persistence.CategoryPostgresqlGateway;
import com.dotflix.infrastructure.genre.persistence.GenreEntity;
import com.dotflix.infrastructure.genre.persistence.GenrePostgresqlGateway;
import com.dotflix.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@PostgresqlGatewayTest
public class GenrePostgresqlGatewayTest {
    @Autowired
    private CategoryPostgresqlGateway categoryGateway;

    @Autowired
    private GenrePostgresqlGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testDependenciesInjected() {
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(genreRepository);
    }

    /* CREATE GENRE TESTS */

    @Test
    public void createGenreTest() {
        try {
            // Arrange
            final Category filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

            final String expectedName = "Ação";
            final boolean expectedIsActive = true;
            final List<String> expectedCategories = List.of(filmes.getId());

            final Genre aGenre = Genre.newGenre(expectedName, expectedIsActive);
            aGenre.addCategories(expectedCategories);

            final String expectedId = aGenre.getId();

            Assertions.assertEquals(0, genreRepository.count());

            // Act
            final Genre actualGenre = genreGateway.create(aGenre);

            // Assert
            Assertions.assertEquals(1, genreRepository.count());

            Assertions.assertEquals(expectedId, actualGenre.getId());
            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
            Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
            Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
            Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
            Assertions.assertNull(actualGenre.getDeletedAt());

            final GenreEntity persistedGenre = genreRepository.findById(expectedId).get();

            Assertions.assertEquals(expectedName, persistedGenre.getName());
            Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
            Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
            Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
            Assertions.assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
            Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
            Assertions.assertNull(persistedGenre.getDeletedAt());
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    /* EXISTS BY ID TEST */

    @Test
    public void existsByIdWithTwoGenresTest() {
        try {
            // Arrange
            final Genre aGenre = Genre.newGenre("Genre 1", true);

            final int expectedItems = 1;
            final String expectedId = aGenre.getId();

            Assertions.assertEquals(0, genreRepository.count());

            genreRepository.saveAndFlush(GenreEntity.fromDomain(aGenre));

            // Act
            final List<String> actualGenre = genreGateway.existsByIds(List.of("123", expectedId));

            // Assert
            Assertions.assertEquals(expectedItems, actualGenre.size());
            Assertions.assertEquals(expectedId, actualGenre.get(0));
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    /* DELETE GENRE TESTS */

    @Test
    public void deleteGenreTest() {
        try {
            // Arrange
            final Genre aGenre = Genre.newGenre("Ação", true);

            genreRepository.saveAndFlush(GenreEntity.fromDomain(aGenre));

            Assertions.assertEquals(1, genreRepository.count());

            // Act
            genreGateway.deleteById(aGenre.getId());

            // Assert
            Assertions.assertEquals(0, genreRepository.count());
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    @Test
    public void deleteGenreWithInvalidGenreTest() {
        // Arrange
        Assertions.assertEquals(0, genreRepository.count());

        // Act
        genreGateway.deleteById("123");

        // Assert
        Assertions.assertEquals(0, genreRepository.count());
    }

    /* GET GENRE TESTS */

    @Test
    public void getGenreTest() {
        try {
            // Arrange
            final Category filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

            final Category series = categoryGateway.create(Category.newCategory("Séries", null, true));

            final String expectedName = "Ação";
            final boolean expectedIsActive = true;
            final List<String> expectedCategories = List.of(filmes.getId(), series.getId());

            final Genre aGenre = Genre.newGenre(expectedName, expectedIsActive);
            aGenre.addCategories(expectedCategories);

            final String expectedId = aGenre.getId();

            genreRepository.saveAndFlush(GenreEntity.fromDomain(aGenre));

            Assertions.assertEquals(1, genreRepository.count());

            // Act
            final Genre actualGenre = genreGateway.findById(expectedId).get();

            // Assert
            Assertions.assertEquals(expectedId, actualGenre.getId());
            Assertions.assertEquals(expectedName, actualGenre.getName());
            Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
            //Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
            Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
            Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
            Assertions.assertNull(actualGenre.getDeletedAt());
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    @Test
    public void getAllGenresWithInvalidTermsTest() {
        // Arrange
        final int expectedPage = 0;
        final int expectedPerPage = 1;
        final String expectedTerms = "";
        final String expectedSort = "name";
        final String expectedDirection = "asc";
        final int expectedTotal = 0;

        final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final Pagination<Genre> actualPage = genreGateway.findAll(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "aç,0,10,1,1,Ação",
            "dr,0,10,1,1,Drama",
            "com,0,10,1,1,Comédia romântica",
            "cien,0,10,1,1,Ficção científica",
            "terr,0,10,1,1,Terror",
    })
    public void getAllGenresWithValidTermsTest(final String expectedTerms, final int expectedPage, final int expectedPerPage, final int expectedItemsCount, final long expectedTotal, final String expectedGenreName) {
        try {
            // Arrange
            mockGenres();
            final String expectedSort = "name";
            final String expectedDirection = "asc";

            final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

            // Act
            final Pagination<Genre> actualPage = genreGateway.findAll(aQuery);

            // Assert
            Assertions.assertEquals(expectedPage, actualPage.currentPage());
            Assertions.assertEquals(expectedPerPage, actualPage.perPage());
            Assertions.assertEquals(expectedTotal, actualPage.total());
            Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
            Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Ação",
            "name,desc,0,10,5,5,Terror",
            "createdAt,asc,0,10,5,5,Comédia romântica",
            "createdAt,desc,0,10,5,5,Ficção científica",
    })
    public void getAllGenresWithSortAndDirectionTest(final String expectedSort, final String expectedDirection, final int expectedPage, final int expectedPerPage, final int expectedItemsCount, final long expectedTotal, final String expectedGenreName) {
        try {
            // Arrange
            mockGenres();
            final String expectedTerms = "";

            final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

            // Act
            final Pagination<Genre> actualPage = genreGateway.findAll(aQuery);

            // Assert
            Assertions.assertEquals(expectedPage, actualPage.currentPage());
            Assertions.assertEquals(expectedPerPage, actualPage.perPage());
            Assertions.assertEquals(expectedTotal, actualPage.total());
            Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
            Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Ação;Comédia romântica",
            "1,2,2,5,Drama;Ficção científica",
            "2,2,1,5,Terror",
    })
    public void getAllGenresWithPagingTest(final int expectedPage, final int expectedPerPage, final int expectedItemsCount, final long expectedTotal, final String expectedGenres) {
        try {
            // Arrange
            mockGenres();
            final String expectedTerms = "";
            final String expectedSort = "name";
            final String expectedDirection = "asc";

            final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

            // Act
            final Pagination<Genre> actualPage = genreGateway.findAll(aQuery);

            // Assert
            Assertions.assertEquals(expectedPage, actualPage.currentPage());
            Assertions.assertEquals(expectedPerPage, actualPage.perPage());
            //Assertions.assertEquals(expectedTotal, actualPage.total());
            Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

            int index = 0;
            for (final String expectedName : expectedGenres.split(";")) {
                final String actualName = actualPage.items().get(index).getName();
                Assertions.assertEquals(expectedName, actualName);
                index++;
            }
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }

    private void mockGenres() throws Exception{
        genreRepository.saveAllAndFlush(List.of(
                GenreEntity.fromDomain(Genre.newGenre("Comédia romântica", true)),
                GenreEntity.fromDomain(Genre.newGenre("Ação", true)),
                GenreEntity.fromDomain(Genre.newGenre("Drama", true)),
                GenreEntity.fromDomain(Genre.newGenre("Terror", true)),
                GenreEntity.fromDomain(Genre.newGenre("Ficção científica", true))
        ));
    }
}