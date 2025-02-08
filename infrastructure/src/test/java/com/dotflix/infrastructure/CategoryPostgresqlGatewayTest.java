package com.dotflix.infrastructure;

import com.dotflix.domain.category.Category;
import com.dotflix.infrastructure.category.CategoryEntity;
import com.dotflix.infrastructure.category.CategoryPostgresqlGateway;
import com.dotflix.infrastructure.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

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

    @Test
    public void testDependencies(){
        Assertions.assertNotNull(categoryPostgresqlGateway);
        Assertions.assertNotNull(categoryRepository);
    }

    @Test
    public void createCategoryTest(){
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final Boolean expectedIsActive = true;

        final Category actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count()); // Garante que não há persistências

        final Category aCategory = categoryPostgresqlGateway.create(actualCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertNotNull(aCategory);
        Assertions.assertNotNull(aCategory.getId());
        Assertions.assertEquals(actualCategory.getId(), aCategory.getId());
        Assertions.assertEquals(expectedName, aCategory.getName());
        Assertions.assertEquals(expectedDescription, aCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, aCategory.getIsActive());
        Assertions.assertEquals(actualCategory.getCreatedAt(), aCategory.getCreatedAt());
        Assertions.assertEquals(actualCategory.getUpdatedAt(), aCategory.getUpdatedAt());
        Assertions.assertEquals(actualCategory.getDeletedAt(), aCategory.getDeletedAt());
        Assertions.assertNull(aCategory.getDeletedAt());

        final CategoryEntity actualEntity = categoryRepository.findById(aCategory.getId().toString()).get();

        Assertions.assertEquals(actualCategory.getId(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.getIsActive());
        Assertions.assertEquals(actualCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(actualCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(actualCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    /**
     * CONTINUAR OS TESTES DA AULA 2104
     */
}
