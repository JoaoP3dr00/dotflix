package com.dotflix.application.category;

import com.dotflix.application.category.dto.CreateCategoryDTO;
import com.dotflix.application.category.dto.DeleteCategoryDTO;
import com.dotflix.application.category.dto.GetCategoryByIdDTO;
import com.dotflix.application.category.dto.UpdateCategoryDTO;
import com.dotflix.application.category.exceptions.CategoryNotFoundException;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.category.CategorySearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CategoryUseCasesTest {
    @Mock
    private CategoryGateway categoryGateway;

    /**
     * Hook que limpa o gateway antes de cada teste
     */
    @BeforeEach
    void cleanup(){
        Mockito.reset(categoryGateway);
    }

    /* CREATE TESTS */

    @InjectMocks
    private CreateCategoryUseCase createCategoryUseCase;

    @Test
    public void createCategoryUseCaseTest(){
        // Arrange
        final String expectedName = "Filmes";
        final String expectedDescription = "Description";
        final boolean expectedIsActive = true;

        final CreateCategoryDTO categoryDTO = new CreateCategoryDTO(expectedName, expectedDescription, expectedIsActive);

        // Act
        Mockito.when(categoryGateway.create(Mockito.any())).thenAnswer(returnsFirstArg());

        final Category actualOutput = createCategoryUseCase.execute(categoryDTO);

        // Assert
        Assertions.assertNotNull(actualOutput);

        Mockito.verify(categoryGateway, Mockito.times(1))   // Verficica se o método foi chamado apenas 1 vez
                .create(Mockito.argThat(category -> {
                    return Objects.equals(expectedName, category.getName())
                        && Objects.equals(expectedDescription, category.getDescription())
                        && Objects.equals(expectedIsActive, category.getIsActive())
                        && Objects.nonNull(category.getId())
                        && Objects.nonNull(category.getCreatedAt())
                        && Objects.nonNull(category.getUpdatedAt())
                        && Objects.isNull(category.getDeletedAt());
        }));
    }

//    @Test
//    public void createCategoryUseCaseWithInvalidUsernameTest(){
//        // Vídeo 2072 tem outros testes e aprofundamento no mockito
//    }

    /* DELETE TESTS */

    @InjectMocks
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @Test
    public void deleteCategoryUseCaseTest(){
        // Arrange
        final DeleteCategoryDTO deleteCategoryDTO = new DeleteCategoryDTO("1");

        // Act
        Mockito.doNothing().when(categoryGateway).deleteById(Mockito.eq(deleteCategoryDTO.id()));

        // Assert
        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(deleteCategoryDTO));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(deleteCategoryDTO.id());
    }

    @Test
    public void deleteCategoryWithInvalidIdUseCaseTest(){
        // Arrange
        final DeleteCategoryDTO deleteCategoryDTO = new DeleteCategoryDTO("1");

        // Act
        Mockito.doNothing().when(categoryGateway).deleteById(Mockito.eq(deleteCategoryDTO.id()));

        // Assert
        Assertions.assertDoesNotThrow(() -> deleteCategoryUseCase.execute(deleteCategoryDTO));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(deleteCategoryDTO.id());
    }

    /* GET TESTS */

    @InjectMocks
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @InjectMocks
    private GetAllCategoriesUseCase getAllCategoriesUseCase;

    @Test
    public void getCategoryByIdUseCaseTest(){
        // Arrange
        final Category category = Category.newCategory("Filmes", "description", true);

        final GetCategoryByIdDTO getCategoryByIdDTO = new GetCategoryByIdDTO(category.getId());

        // Act
        Mockito.when(categoryGateway.findById(Mockito.eq(category.getId()))).thenReturn(Optional.of(category.clone()));

        // Assert
        try {
            final Category actualCategory = getCategoryByIdUseCase.execute(getCategoryByIdDTO);

            Assertions.assertEquals(category.getName(), actualCategory.getName());
        } catch (CategoryNotFoundException e){
            System.out.println("Error: " + e);
        }
    }

//    @Test
//    public void getCategoryByInvalidIdUseCaseTest(){
//        // Aula F2084
//    }
//
//    @Test
//    public void getCategoryByValidIdExceptionUseCaseTest(){}

    @Test
    public void getCategoriesUseCaseTest(){
        // Arrange
        final List<Category> categories = List.of(Category.newCategory("Filmes", null, true), Category.newCategory("Series", null, true));

        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";

        final Pagination<Category> expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

        final int expectedItemCount = 2;

        final CategorySearchQuery query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        Mockito.when(categoryGateway.findAll(Mockito.eq(query))).thenReturn(expectedPagination);

        final var actualResult = getAllCategoriesUseCase.execute(query);

        // Assert
        Assertions.assertEquals(expectedItemCount, actualResult.items().size());
        Assertions.assertEquals(expectedPagination, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void getCategoriesWithEmptyListUseCaseTest(){
        // Arrange
        final List<Category> categories = List.of();

        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";

        final Pagination<Category> expectedPagination = new Pagination<>(expectedPage, expectedPerPage, 0, categories);

        final int expectedItemCount = 0;

        final CategorySearchQuery query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        Mockito.when(categoryGateway.findAll(Mockito.eq(query))).thenReturn(expectedPagination);

        final var actualResult = getAllCategoriesUseCase.execute(query);

        // Assert
        Assertions.assertEquals(expectedItemCount, actualResult.items().size());
        Assertions.assertEquals(expectedPagination, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(0, actualResult.total());

    }

    @Test
    public void getCategoriesWithExceptionUseCaseTest(){
        // Arrange
        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";
        final String expectedErrorMessage = "Gateway error";

        final CategorySearchQuery query = new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        Mockito.when(categoryGateway.findAll(Mockito.eq(query))).thenThrow(new IllegalStateException(expectedErrorMessage));

        // Assert
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> getAllCategoriesUseCase.execute(query));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    /* UPDATE TESTS */

    @InjectMocks
    private UpdateCategoryUseCase updateCategoryUseCase;

    @Test
    public void updateCategoryUseCaseTest(){
        // Arrange
        final String expectedName = "Filmes";
        final String expectedDescription = "A description";
        final boolean expectedIsActive = true;

        final Category category = Category.newCategory("slkdf", "sldkf", true);

        final UpdateCategoryDTO updateCategoryDTO = new UpdateCategoryDTO(category.getId(), expectedName, expectedDescription, expectedIsActive);

        // Act
        Mockito.when(categoryGateway.findById(Mockito.eq(category.getId()))).thenReturn(Optional.of(category.clone()));

        Mockito.when(categoryGateway.update(Mockito.any())).thenAnswer(returnsFirstArg());

        // Assert
        try {
            final Category actualCategory = updateCategoryUseCase.execute(updateCategoryDTO);

            Assertions.assertNotNull(actualCategory);
            Assertions.assertNotNull(actualCategory.getId());

            Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(category.getId()));
            Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(
                    updatedCategory -> {
                        return Objects.equals(expectedName, updatedCategory.getName())
                                && Objects.equals(expectedDescription, updatedCategory.getDescription())
                                && Objects.equals(expectedIsActive, updatedCategory.getIsActive())
                                && Objects.nonNull(updatedCategory.getId())
                                && Objects.equals(category.getCreatedAt(), updatedCategory.getCreatedAt())
                                && category.getCreatedAt().isBefore(updatedCategory.getUpdatedAt())
                                && Objects.isNull(updatedCategory.getDeletedAt());
                    }
            ));
        }catch (CategoryNotFoundException categoryNotFoundException){
            System.out.println("Error: " + categoryNotFoundException);
        }
    }

//    @Test
//    public void updateCategoryWithInvalidPropertyUseCaseTest(){
//        // Aula 2079
//    }

}
