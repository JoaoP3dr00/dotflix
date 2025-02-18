package com.dotflix.infrastructure.e2e.category;

import com.dotflix.domain.category.Category;
import com.dotflix.infrastructure.ApiTest;
import com.dotflix.infrastructure.E2ETest;
import com.dotflix.infrastructure.category.controller.dto.CreateCategoryRequest;
import com.dotflix.infrastructure.category.persistence.CategoryRepository;
import com.dotflix.infrastructure.configuration.WebServerConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers  // Gerencia automaticamente o ciclo de vida dos contêineres Docker para que eles sejam iniciados e parados corretamente.
public class CategoryE2ETest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    /**
     * Um contêiner PostgreSQL está sendo criado usando a imagem oficial postgres:17.
     * Só existe durante os testes.
     */
    @Container
    private static final PostgreSQLContainer POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres:17")
            .withPassword("postgres")
            .withUsername("postgres")
            .withDatabaseName("dotflix");

    /**
     * Aqui, a porta do banco de dados dentro do contêiner é recuperada e registrada para que o Spring Boot possa acessá-la.
     * @param dynamicPropertyRegistry
     */
    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry dynamicPropertyRegistry){
        final int mappedPort = POSTGRE_SQL_CONTAINER.getMappedPort(5432);
        System.out.println("Container ir running in port: " + mappedPort);
        dynamicPropertyRegistry.add("postgres.port", () -> mappedPort);
    }

    /**
     * Isso garante que o ambiente de testes foi inicializado corretamente.
     */
    @Test
    public void worksTest(){
        Assertions.assertTrue(POSTGRE_SQL_CONTAINER.isRunning());
    }

    @Test
    public void createCategoryAsACatalogAdminTest() throws Exception {
        // Arrange
        final String expectedName = "Filmes";
        final String expectedDescription = "Categoria de filmes";
        final boolean expectedIsActive = true;

        Assertions.assertEquals(0, categoryRepository.count());

        final Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final String id = category.getId();

        final CreateCategoryRequest requestBody = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        // Act
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(requestBody));

        final var actualId = this.mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn()
                .getResponse().getHeader("Location")
                .replace("%s/".formatted("/categories"), "");

        // Assert
        final var actualCategory = categoryRepository.findById(actualId).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.getIsActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }
}
