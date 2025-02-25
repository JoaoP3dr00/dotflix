package com.dotflix.infrastructure.category;

import com.dotflix.application.category.*;
import com.dotflix.application.category.dto.DeleteCategoryDTO;
import com.dotflix.application.category.dto.GetCategoryByIdDTO;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.category.Category;
import com.dotflix.infrastructure.ApiTest;
import com.dotflix.infrastructure.ControllerTest;
import com.dotflix.infrastructure.category.controller.CategoryAPI;
import com.dotflix.infrastructure.category.controller.dto.CreateCategoryRequest;
import com.dotflix.infrastructure.category.controller.dto.UpdateCategoryRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import java.util.Objects;

@ActiveProfiles("test")
@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockitoBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockitoBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockitoBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockitoBean
    private GetAllCategoriesUseCase getAllCategoriesUseCase;

    /* CREATE TESTS */
    @Test
    public void createCategoryTest() throws Exception {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aInput = new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any())).thenReturn(category);

        String jsonInput = String.format("{\"name\":\"%s\",\"description\":\"%s\",\"isActive\":\"%s\"}", aInput.name(), aInput.description(), aInput.active());

        // Act
        final var request = MockMvcRequestBuilders.post("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInput);

        final ResultActions response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/categories/" + category.getId()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(category.getId())));

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    /* GET CATEGORY TESTS */
    @Test
    public void getCategoryTest() throws Exception {
        // Arrange
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final String expectedId = aCategory.getId();

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any())).thenReturn(aCategory);

        // Act
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final ResultActions response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(aCategory.getUpdatedAt().toString())));

        Mockito.verify(getCategoryByIdUseCase, Mockito.times(1)).execute(Mockito.eq(new GetCategoryByIdDTO(expectedId)));
    }

    @Test
    public void getAllCategoriesTest() throws Exception {
        // Arrange
        final Category aCategory = Category.newCategory("Movies", null, true);

        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "movies";
        final String expectedSort = "description";
        final String expectedDirection = "desc";
        final int expectedItemsCount = 1;
        final int expectedTotal = 1;

        assert aCategory != null;
        final List<Category> expectedItems = List.of(aCategory);

        Mockito.when(getAllCategoriesUseCase.execute(Mockito.any())).thenReturn(new Pagination<Category>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // Act
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/categories")
                .with(ApiTest.CATEGORIES_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final ResultActions response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perPage", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aCategory.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aCategory.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.equalTo(aCategory.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(aCategory.getIsActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", Matchers.equalTo(aCategory.getDeletedAt())));

        Mockito.verify(getAllCategoriesUseCase, Mockito.times(1)).execute(Mockito.argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }

    /* UPDATE TESTS */
    @Test
    public void updateCategoryTest() throws Exception {
        // Arrange
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(updateCategoryUseCase.execute(Mockito.any())).thenReturn(aCategory);

        final UpdateCategoryRequest aCommand = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        String jsonInput = String.format("{\"name\":\"%s\",\"description\":\"%s\",\"isActive\":\"%s\"}", aCommand.name(), aCommand.description(), aCommand.active());

        // Act
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put("/categories/{id}", aCategory.getId())
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonInput);

        final ResultActions response = this.mvc.perform(request).andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(aCategory.getId())));

        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    /* DELETE TESTS */
    @Test
    public void deleteCategoryTest() throws Exception{
        // Arrange
        final String expectedId = "123";

        Mockito.when(deleteCategoryUseCase.execute(Mockito.any())).thenReturn("ok");

        // Act
        final MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .with(ApiTest.CATEGORIES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final ResultActions response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteCategoryUseCase, Mockito.times(1)).execute(Mockito.eq(new DeleteCategoryDTO(expectedId)));
    }
}
