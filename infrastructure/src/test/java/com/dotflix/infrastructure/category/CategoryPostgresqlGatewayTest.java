package com.dotflix.infrastructure.category;

import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.category.CategorySearchQuery;
import com.dotflix.domain.Pagination;
import com.dotflix.infrastructure.category.persistence.CategoryEntity;
import com.dotflix.infrastructure.category.persistence.CategoryPostgresqlGateway;
import com.dotflix.infrastructure.category.persistence.CategoryRepository;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@DataJpaTest
@ComponentScan(includeFilters = {
    @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[PostgresqlGateway]")   // Inicia todas as classes que terminam com PostgresqlGateway além das classes do DataJpaTest
})
public class CategoryPostgresqlGatewayTest {
    @Autowired
    CategoryPostgresqlGateway categoryPostgresqlGateway;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanup(){
        categoryRepository.deleteAll();
        categoryRepository.flush();
    }

    @Test
    public void testDependencies(){
        Assertions.assertNotNull(categoryPostgresqlGateway);
        Assertions.assertNotNull(categoryRepository);
    }

    /* CREATE INTEGRATED TESTS */

    @Test
    public void createCategoryTest(){
        // Arrange
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final Boolean expectedIsActive = true;

        final Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // Act
        final Category aCategory = categoryPostgresqlGateway.create(category);

        // Assert
        Assertions.assertEquals(1, categoryRepository.count()); // Garante que há uma persistência

        Assertions.assertNotNull(aCategory);
        Assertions.assertNotNull(aCategory.getId());
        Assertions.assertEquals(category.getId(), aCategory.getId());
        Assertions.assertEquals(expectedName, aCategory.getName());
        Assertions.assertEquals(expectedDescription, aCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, aCategory.getIsActive());
        Assertions.assertEquals(category.getCreatedAt(), aCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), aCategory.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), aCategory.getDeletedAt());
        Assertions.assertNull(aCategory.getDeletedAt());

        final CategoryEntity actualEntity = categoryRepository.findById(aCategory.getId()).get();

        Assertions.assertEquals(category.getId(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.getIsActive());
        Assertions.assertEquals(category.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void createCategoryWithInvalidNullNameTest(){
        // Arrange
        final Category category = Category.newCategory("Filme", null, true);

        final CategoryEntity entity = CategoryEntity.fromDomain(category);

        // Act
        entity.setName(null);

        // Assert
        final DataIntegrityViolationException exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(entity));

        final PropertyValueException actualExcepetion = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());    // Garante que a exception é do tipo PropertyValueException, que é a excpetion de validação do hibernate

        Assertions.assertEquals("name", actualExcepetion.getPropertyName());
        Assertions.assertEquals("not-null property references a null or transient value: com.dotflix.infrastructure.category.persistence.CategoryEntity.name", actualExcepetion.getMessage());
    }

    @Test
    public void createCategoryWithInvalidNullCreatedAtTest(){
        // Arrange
        final Category category = Category.newCategory("Filme", null, true);

        final CategoryEntity entity = CategoryEntity.fromDomain(category);

        // Act
        entity.setCreatedAt(null);

        // Assert
        final DataIntegrityViolationException exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(entity));

        final PropertyValueException actualExcepetion = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());    // Garante que a exception é do tipo PropertyValueException, que é a excpetion de validação do hibernate

        Assertions.assertEquals("createdAt", actualExcepetion.getPropertyName());
        Assertions.assertEquals("not-null property references a null or transient value: com.dotflix.infrastructure.category.persistence.CategoryEntity.createdAt", actualExcepetion.getMessage());
    }

    @Test
    public void createCategoryWithInvalidNullUpdatedAtTest(){
        // Arrange
        final Category category = Category.newCategory("Filme", null, true);

        final CategoryEntity entity = CategoryEntity.fromDomain(category);

        // Act
        entity.setUpdatedAt(null);

        // Assert
        final DataIntegrityViolationException exception = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(entity));

        final PropertyValueException actualExcepetion = Assertions.assertInstanceOf(PropertyValueException.class, exception.getCause());    // Garante que a exception é do tipo PropertyValueException, que é a excpetion de validação do hibernate

        Assertions.assertEquals("updatedAt", actualExcepetion.getPropertyName());
        Assertions.assertEquals("not-null property references a null or transient value: com.dotflix.infrastructure.category.persistence.CategoryEntity.updatedAt", actualExcepetion.getMessage());
    }

    /* UPDATE INTEGRATED TESTES */
    @Test
    public void updateCategoryTest(){
        // Arrange
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final Boolean expectedIsActive = true;

        final Category category = Category.newCategory(expectedName, null, expectedIsActive);

        // Act
        categoryRepository.saveAndFlush(CategoryEntity.fromDomain(category));

        final Category updatedCategory = category.clone().update(expectedName,expectedDescription,expectedIsActive);    // Clonar o objeto para poder comparar o atualizado com o antigo
        final Category actualCategory = categoryGateway.update(updatedCategory);

        // Assert
        Assertions.assertEquals(1, categoryRepository.count()); // Garante que há uma persistência

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final CategoryEntity actualEntity = categoryRepository.findById(actualCategory.getId()).get();

        Assertions.assertEquals(category.getId(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.getIsActive());
        Assertions.assertEquals(category.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(category.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(category.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    /* DELETE INTEGRATED TESTS */

    @Test
    public void deleteCategoryTest(){
        // Arrange
        final Category category = Category.newCategory("Filmes", null, true);

        // Act and Assert
        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.save(CategoryEntity.fromDomain(category));

        Assertions.assertEquals(1, categoryRepository.count());

        categoryRepository.deleteById(category.getId());

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void deleteCategoryWithInvalidIdTest(){
        // Act and Assert
        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.deleteById("i");

        Assertions.assertEquals(0, categoryRepository.count());
    }

    /* GET INTEGRATED TESTS */

    @Test
    public void getCategoryByIdTest(){
        // Arrange
        final String expectedName = "Filmes";
        final String expectedDescription = null;
        final Boolean expectedIsActive = true;

        final Category category = Category.newCategory(expectedName, null, expectedIsActive);

        // Act
        categoryRepository.saveAndFlush(CategoryEntity.fromDomain(category));

        final Category actualCategory = categoryGateway.findById(category.getId()).get();

        // Assert
        Assertions.assertEquals(1, categoryRepository.count()); // Garante que há uma persistência

        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(category.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void getCategoryByInvalidIdTest(){
        // Act and Assert
        Assertions.assertEquals(0, categoryRepository.count());

        final Optional<Category> actualCategory = categoryGateway.findById("i");

        Assertions.assertTrue(actualCategory.isEmpty());
    }

    /* GET ALL TESTS */

    @Test
    public void getCategoriesTest(){
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final Category filmes = Category.newCategory("Filmes", null, true);
        final Category series = Category.newCategory("Series", null, true);
        final Category documentarios = Category.newCategory("Documentarios", null, true);
        Assertions.assertEquals(0, categoryRepository.count());

        // Act
        categoryRepository.saveAll(List.of(CategoryEntity.fromDomain(filmes), CategoryEntity.fromDomain(series), CategoryEntity.fromDomain(documentarios)));

        Assertions.assertEquals(3, categoryRepository.count());

        final CategorySearchQuery query = new CategorySearchQuery(0, 1, "", "name", "asc");

        final Pagination<Category> actualResult = categoryPostgresqlGateway.findAll(query);

        // Asserts
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void getCategoriesWithEmptyTableTest(){
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, categoryRepository.count());

        // Act
        final CategorySearchQuery query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final Pagination<Category> actualResult = categoryPostgresqlGateway.findAll(query);

        // Asserts
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    public void getCategoriesWithFollowPaginationTest(){
        // Arrange
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final Category filmes = Category.newCategory("Filmes", null, true);
        final Category series = Category.newCategory("Series", null, true);
        final Category documentarios = Category.newCategory("Documentarios", null, true);
        Assertions.assertEquals(0, categoryRepository.count());

        // Act
        categoryRepository.saveAll(List.of(CategoryEntity.fromDomain(filmes), CategoryEntity.fromDomain(series), CategoryEntity.fromDomain(documentarios)));

        Assertions.assertEquals(3, categoryRepository.count());

        // PAGE 0
        CategorySearchQuery query = new CategorySearchQuery(0, 1, "", "name", "asc");

        Pagination<Category> actualResult = categoryPostgresqlGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());

        // PAGE 1
        expectedPage = 1;
        query = new CategorySearchQuery(1, 1, "", "name", "asc");

        actualResult = categoryPostgresqlGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(filmes.getId(), actualResult.items().get(0).getId());

        // PAGE 2
        expectedPage = 2;
        query = new CategorySearchQuery(2, 1, "", "name", "asc");

        actualResult = categoryPostgresqlGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(series.getId(), actualResult.items().get(0).getId());

    }

    @Test
    public void getCategoriesThatMatchCategoryNameTest(){
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final Category filmes = Category.newCategory("Filmes", null, true);
        final Category series = Category.newCategory("Series", null, true);
        final Category documentarios = Category.newCategory("Documentarios", null, true);
        Assertions.assertEquals(0, categoryRepository.count());

        // Act
        categoryRepository.saveAll(List.of(CategoryEntity.fromDomain(filmes), CategoryEntity.fromDomain(series), CategoryEntity.fromDomain(documentarios)));

        Assertions.assertEquals(3, categoryRepository.count());

        final CategorySearchQuery query = new CategorySearchQuery(0, 1, "doc", "name", "asc");

        final Pagination<Category> actualResult = categoryPostgresqlGateway.findAll(query);

        // Asserts
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void getCategoriesThatMatchCategoryDescriptionTest(){
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final Category filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final Category series = Category.newCategory("Series", "Uma categoria assistida", true);
        final Category documentarios = Category.newCategory("Documentarios", "A categoria menos assistida", true);
        Assertions.assertEquals(0, categoryRepository.count());

        // Act
        categoryRepository.saveAll(List.of(CategoryEntity.fromDomain(filmes), CategoryEntity.fromDomain(series), CategoryEntity.fromDomain(documentarios)));

        Assertions.assertEquals(3, categoryRepository.count());

        final CategorySearchQuery query = new CategorySearchQuery(0, 1, "MAIS ASSISTIDA", "name", "asc");

        final Pagination<Category> actualResult = categoryPostgresqlGateway.findAll(query);

        // Asserts
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(filmes.getId(), actualResult.items().get(0).getId());
    }
}
