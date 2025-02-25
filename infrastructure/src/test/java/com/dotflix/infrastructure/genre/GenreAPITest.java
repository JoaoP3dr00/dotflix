package com.dotflix.infrastructure.genre;

import com.dotflix.application.genre.*;
import com.dotflix.application.genre.excpetions.GenreNotFoundException;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.genre.Genre;
import com.dotflix.infrastructure.ApiTest;
import com.dotflix.infrastructure.ControllerTest;
import com.dotflix.infrastructure.genre.controller.GenreAPI;
import com.dotflix.infrastructure.genre.controller.dto.CreateGenreRequest;
import com.dotflix.infrastructure.genre.controller.dto.UpdateGenreRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import java.util.Objects;
import org.hamcrest.Matchers;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    private CreateGenreUseCase createGenreUseCase;

    @MockitoBean
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @MockitoBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockitoBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @MockitoBean
    private GetAllGenreUseCase getAllGenreUseCase;

    /* CREATE GENRES TESTS */

    @Test
    public void createGenreTest() throws Exception {
        // Arrange
        final String expectedName = "Ação";
        final List<String> expectedCategories = List.of("123", "456");
        final boolean expectedIsActive = true;
        final String expectedId = "123";

        final CreateGenreRequest aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        final Genre genre = Genre.newGenre(expectedName,expectedIsActive);

        Mockito.when(createGenreUseCase.execute(Mockito.any())).thenReturn(genre);

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.post("/genres")
                .with(ApiTest.GENRES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/genres/" + genre.getId()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(genre.getId())));

        Mockito.verify(createGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void createGenreWithInvalidNameTest() throws Exception {
        // Arrange
        final List<String > expectedCategories = List.of("123", "456");
        final boolean expectedIsActive = true;
        final String expectedErrorMessage = "'name' should not be null";

        final CreateGenreRequest aCommand = new CreateGenreRequest(null, expectedCategories, expectedIsActive);

        Mockito.when(createGenreUseCase.execute(Mockito.any())).thenThrow(new Exception("'name' should not be null"));

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.post("/genres")
                .with(ApiTest.GENRES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aCommand));

        final ResultActions response = this.mvc.perform(aRequest).andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(createGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(null, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    /* GET GENRES TEST */

    @Test
    public void getGenreTest() throws Exception {
        // Arrange
        final String expectedName = "Ação";
        final List<String> expectedCategories = List.of("123", "456");
        final boolean expectedIsActive = false;

        final Genre aGenre = Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories.stream().toList());

        final String expectedId = aGenre.getId();

        Mockito.when(getGenreByIdUseCase.execute(Mockito.any())).thenReturn(aGenre);

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.get("/genres/{id}", expectedId)
                .with(ApiTest.GENRES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.categories_id", Matchers.equalTo(expectedCategories)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(aGenre.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at", Matchers.equalTo(aGenre.getDeletedAt().toString())));

        Mockito.verify(getGenreByIdUseCase).execute(Mockito.eq(expectedId));
    }

    @Test
    public void getGenreWithInvalIdIdTest() throws Exception {
        // Arrange
        final String expectedErrorMessage = "Genre with ID 123 was not found";
        final String expectedId = "123";

        Mockito.when(getGenreByIdUseCase.execute(Mockito.any())).thenThrow(new GenreNotFoundException("Genre with ID " + expectedId + " was not found"));

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.get("/genres/{id}", expectedId)
                .with(ApiTest.GENRES_JWT)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final ResultActions response = this.mvc.perform(aRequest);

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(getGenreByIdUseCase).execute(Mockito.eq(expectedId));
    }

    /* UPDATE GENRE TESTS */

    @Test
    public void updateGenreTest() throws Exception {
        // Arrange
        final String expectedName = "Ação";
        final List<String> expectedCategories = List.of("123", "456");
        final boolean expectedIsActive = true;

        final Genre aGenre = Genre.newGenre(expectedName, expectedIsActive);
        final String expectedId = aGenre.getId();

        final UpdateGenreRequest aCommand = new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        Mockito.when(updateGenreUseCase.execute(Mockito.any())).thenReturn(aGenre);

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .with(ApiTest.GENRES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aCommand));

        final ResultActions response = this.mvc.perform(aRequest).andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(updateGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void updateGenreWithInvalidNameTest() throws Exception {
        // Arrange
        final List<String> expectedCategories = List.of("123", "456");
        final boolean expectedIsActive = true;
        final String expectedErrorMessage = "'name' should not be null";

        final Genre aGenre = Genre.newGenre("Ação", expectedIsActive);
        final String expectedId = aGenre.getId();

        final UpdateGenreRequest aCommand = new UpdateGenreRequest(null, expectedCategories, expectedIsActive);

        Mockito.when(updateGenreUseCase.execute(Mockito.any())).thenThrow(new Exception("'name' should not be null"));

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.put("/genres/{id}", expectedId)
                .with(ApiTest.GENRES_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aCommand));

        final ResultActions response = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateGenreUseCase).execute(Mockito.argThat(cmd ->
                Objects.equals(null, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    /* DELETE GENRE TESTS */

    @Test
    public void deleteGenreTest() throws Exception {
        // Arrange
        final String expectedId = "123";

        Mockito.when(deleteGenreUseCase.execute(Mockito.any())).thenReturn("Genre " + expectedId + " deleted");

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.delete("/genres/{id}", expectedId)
                .with(ApiTest.GENRES_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final ResultActions result = this.mvc.perform(aRequest);

        // Assert
        result.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteGenreUseCase).execute(Mockito.eq(expectedId));
    }

    /* GET ALL GENRES TESTS */

    @Test
    public void getAllGenresTest() throws Exception {
        // Arrange
        final Genre aGenre = Genre.newGenre("Ação", false);

        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "ac";
        final String expectedSort = "name";
        final String expectedDirection = "asc";
        final int expectedItemsCount = 1;
        final int expectedTotal = 1;

        final List<Genre> expectedItems = List.of(aGenre);

        Mockito.when(getAllGenreUseCase.execute(Mockito.any())).thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.get("/genres")
                .with(ApiTest.GENRES_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON);

        final ResultActions response = this.mvc.perform(aRequest);

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perPage", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aGenre.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aGenre.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(aGenre.isActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", Matchers.equalTo(aGenre.getDeletedAt().toString())));

        Mockito.verify(getAllGenreUseCase).execute(Mockito.argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}