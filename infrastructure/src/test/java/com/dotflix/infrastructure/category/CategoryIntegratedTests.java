package com.dotflix.infrastructure.category;

import com.dotflix.application.category.*;
import com.dotflix.application.category.dto.CreateCategoryDTO;
import com.dotflix.application.category.dto.DeleteCategoryDTO;
import com.dotflix.application.category.dto.GetCategoryByIdDTO;
import com.dotflix.application.category.dto.UpdateCategoryDTO;
import com.dotflix.application.category.exceptions.CategoryNotFoundException;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.Pagination;
import com.dotflix.infrastructure.IntegrationTest;
import com.dotflix.infrastructure.category.persistence.CategoryEntity;
import com.dotflix.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 * TESTES DE INTEGRAÇÃO ENTRE A CAMADA DE APLICAÇÃO E A DE INFRAESTRUTURA
 * */
@IntegrationTest
public class CategoryIntegratedTests {
    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @Autowired
    private GetAllCategoriesUseCase getAllCategoriesUseCase;

    @Autowired
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Autowired
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryGateway categoryGateway;

    @BeforeEach
    void mockUp() throws Exception {
        // Clean up before
        categoryRepository.deleteAll();
        categoryRepository.flush();

        final var categories = Stream.of(
                        Category.newCategory("Filmes", null, true),
                        Category.newCategory("Netflix Originals", "Títulos de autoria da Netflix", true),
                        Category.newCategory("Amazon Originals", "Títulos de autoria da Amazon Prime", true),
                        Category.newCategory("Documentários", null, true),
                        Category.newCategory("Sports", null, true),
                        Category.newCategory("Kids", "Categoria para crianças", true),
                        Category.newCategory("Series", null, true)
                )
                .map(CategoryEntity::fromDomain)
                .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    /* CREATE INTEGRATED TESTS */

    @Test
    public void createCategoryTest() throws Exception {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        Assertions.assertEquals(7, categoryRepository.count());

        final CreateCategoryDTO aCommand = new CreateCategoryDTO(expectedName, expectedDescription, expectedIsActive);

        final Category actualOutput = createCategoryUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.getId());

        Assertions.assertEquals(8, categoryRepository.count());

        final CategoryEntity actualCategory = categoryRepository.findById(actualOutput.getId()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    /* GET INTEGRATED TESTS */
    @Test
    public void getCategoryByIdTest() throws Exception{
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final GetCategoryByIdDTO getCategoryByIdDTO = new GetCategoryByIdDTO(aCategory.getId());

        categoryRepository.save(CategoryEntity.fromDomain(aCategory.clone()));

        final Category actualCategory = getCategoryByIdUseCase.execute(getCategoryByIdDTO);

        Assertions.assertEquals(getCategoryByIdDTO.id(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
    }

    @Test
    public void getCategoryByInvalidIdTest() {
        final String expectedErrorMessage = "A categoria oi não foi encontrada.";
        final GetCategoryByIdDTO getCategoryByIdDTO = new GetCategoryByIdDTO("oi");

        final var actualException = Assertions.assertThrows(
                CategoryNotFoundException.class,
                () -> getCategoryByIdUseCase.execute(getCategoryByIdDTO)
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    /* GET ALL INTEGRATED TESTS */
    @Test
    public void getAllCategoriesTest() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ji1j3i 1j3i1oj";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = getAllCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Filmes",
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "KI,0,10,1,1,Kids",
            "crianças,0,10,1,1,Kids",
            "da Amazon,0,10,1,1,Amazon Originals",
    })
    public void getAllCategoriesWithValidTermsTest(final String expectedTerms, final int expectedPage, final int expectedPerPage, final int expectedItemsCount, final long expectedTotal, final String expectedCategoryName) {
        final String expectedSort = "name";
        final String expectedDirection = "asc";

        final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final Pagination<Category> actualResult = getAllCategoriesUseCase.execute(aQuery);

        System.out.println(expectedTerms + " " + expectedItemsCount + " " + actualResult.items().size() + " " + actualResult.items());
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon Originals",
            "name,desc,0,10,7,7,Sports",
            "createdAt,asc,0,10,7,7,Filmes",
            "createdAt,desc,0,10,7,7,Series",
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(final String expectedSort, final String expectedDirection, final int expectedPage, final int expectedPerPage, final int expectedItemsCount, final long expectedTotal, final String expectedCategoryName) {
        final var expectedTerms = "";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = getAllCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Amazon Originals;Documentários",
            "1,2,2,7,Filmes;Kids",
            "2,2,2,7,Netflix Originals;Series",
            "3,2,1,7,Sports",
    })
    public void givenAValidPage_whenCallsListCategories_shouldReturnCategoriesPaginated(final int expectedPage, final int expectedPerPage, final int expectedItemsCount, final long expectedTotal, final String expectedCategoriesName) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = getAllCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (final String expectedName : expectedCategoriesName.split(";")) {
            final String actualName = actualResult.items().get(index).getName();
            Assertions.assertEquals(expectedName, actualName);
            index++;
        }
    }

    /* DELETE INTEGRATED TESTS */
    @Test
    public void deleteCategoryTest() {
        final Category aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final DeleteCategoryDTO deleteCategoryDTO = new DeleteCategoryDTO(aCategory.getId());

        categoryRepository.saveAllAndFlush(
                Arrays.stream(new Category[]{aCategory})
                        .map(CategoryEntity::fromDomain)
                        .toList()
        );

        Assertions.assertEquals(8, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(deleteCategoryDTO));

        Assertions.assertEquals(7, categoryRepository.count());
    }

    @Test
    public void deleteCategoryWithInvalidIdTest() {
        final DeleteCategoryDTO deleteCategoryDTO = new DeleteCategoryDTO("oi");

        Assertions.assertEquals(7, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(deleteCategoryDTO));

        Assertions.assertEquals(7, categoryRepository.count());
    }

    /* UPDATE INTEGRATED TESTS */
    @Test
    public void updateCategoryTest() {
        final Category aCategory = Category.newCategory("Film", null, true);

        categoryRepository.saveAllAndFlush(
                Arrays.stream(new Category[]{aCategory})
                        .map(CategoryEntity::fromDomain)
                        .toList()
        );

        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedId = aCategory.getId();

        final UpdateCategoryDTO updateCategoryDTO = new UpdateCategoryDTO(expectedId, expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(8, categoryRepository.count());

        try {
            final Category actualOutput = updateCategoryUseCase.execute(updateCategoryDTO);

            Assertions.assertNotNull(actualOutput);
            Assertions.assertNotNull(actualOutput.getId());

            final CategoryEntity actualCategory = categoryRepository.findById(expectedId).get();

            Assertions.assertEquals(expectedName, actualCategory.getName());
            Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
            Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
            Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
            Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
            Assertions.assertNull(actualCategory.getDeletedAt());
        } catch (Exception e){
            System.out.println("Error: " + e);
        }
    }
}