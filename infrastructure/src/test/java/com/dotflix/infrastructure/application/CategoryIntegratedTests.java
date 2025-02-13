package com.dotflix.infrastructure.application;

import com.dotflix.application.category.*;
import com.dotflix.application.category.dto.CreateCategoryDTO;
import com.dotflix.application.category.dto.DeleteCategoryDTO;
import com.dotflix.application.category.dto.GetCategoryByIdDTO;
import com.dotflix.application.category.dto.UpdateCategoryDTO;
import com.dotflix.application.category.exceptions.CategoryNotFoundException;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.category.CategorySearchQuery;
import com.dotflix.infrastructure.IntegrationTest;
import com.dotflix.infrastructure.category.CategoryEntity;
import com.dotflix.infrastructure.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.util.Arrays;
import java.util.stream.Stream;

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
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryGateway categoryGateway;
    @Autowired
    private UpdateCategoryUseCase updateCategoryUseCase;

    @BeforeEach
    void mockUp() {
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

    @BeforeEach
    void cleanup(){
        categoryRepository.deleteAll();
        categoryRepository.flush();
    }

    /* CREATE INTEGRATED TESTS */

    @Test
    public void createCategoryTest() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        Assertions.assertEquals(0, categoryRepository.count());

        final CreateCategoryDTO aCommand = new CreateCategoryDTO(expectedName, expectedDescription, expectedIsActive);

        final Category actualOutput = createCategoryUseCase.execute(aCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.getId());

        Assertions.assertEquals(1, categoryRepository.count());

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
    public void getCategoryByIdTest() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final GetCategoryByIdDTO getCategoryByIdDTO = new GetCategoryByIdDTO(aCategory.getId());

        categoryRepository.save(CategoryEntity.fromDomain(aCategory.clone()));

        try {
            final Category actualCategory = getCategoryByIdUseCase.execute(getCategoryByIdDTO);

            Assertions.assertEquals(getCategoryByIdDTO.id(), actualCategory.getId());
            Assertions.assertEquals(expectedName, actualCategory.getName());
            Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
            Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
            Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
            Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
            Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        } catch (CategoryNotFoundException e){
            System.out.println("Error: " + e);
        }
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

        final CategorySearchQuery aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

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
    public void getAllCategoriesWithValidTermsTest(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = getAllCategoriesUseCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).getName());
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

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(deleteCategoryDTO));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void deleteCategoryWithInvalidIdTest() {
        final DeleteCategoryDTO deleteCategoryDTO = new DeleteCategoryDTO("oi");

        Assertions.assertEquals(0, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(deleteCategoryDTO));

        Assertions.assertEquals(0, categoryRepository.count());
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

        Assertions.assertEquals(1, categoryRepository.count());

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
        } catch (CategoryNotFoundException categoryNotFoundException){
            System.out.println("Error: " + categoryNotFoundException);
        }
    }
}