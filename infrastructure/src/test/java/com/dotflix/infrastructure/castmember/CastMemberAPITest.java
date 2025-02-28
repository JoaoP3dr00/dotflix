package com.dotflix.infrastructure.castmember;

import com.dotflix.application.castmember.*;
import com.dotflix.application.castmember.exceptions.CastMemberNotFoundException;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.castmember.CastMember;
import com.dotflix.domain.castmember.CastMemberType;
import com.dotflix.infrastructure.ApiTest;
import com.dotflix.infrastructure.ControllerTest;
import com.dotflix.infrastructure.castmember.controller.CastMemberAPI;
import com.dotflix.infrastructure.castmember.controller.dto.CreateCastMemberRequest;
import com.dotflix.infrastructure.castmember.controller.dto.UpdateCastMemberRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
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

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private CreateCastMemberUseCase createCastMemberUseCase;

    @MockitoBean
    private DeleteCastMemberUseCase deleteCastMemberUseCase;

    @MockitoBean
    private GetCastMemberByIdUseCase getCastMemberByIdUseCase;

    @MockitoBean
    private GetAllCastMemberUseCase getAllCastMemberUseCase;

    @MockitoBean
    private UpdateCastMemberUseCase updateCastMemberUseCase;

    /* CREATE CASTMEMBERS TEST */
    @Test
    public void createCastMemberTest() throws Exception {
        // Arrange
        final String expectedName = "Joao";
        final CastMemberType expectedType = CastMemberType.ACTOR;
        final CastMember castMember = CastMember.newMember(expectedName, expectedType);
        final String expectedId = castMember.getId();

        final CreateCastMemberRequest aCommand = new CreateCastMemberRequest(expectedName, expectedType);

        Mockito.when(createCastMemberUseCase.execute(Mockito.any())).thenReturn(castMember);

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final ResultActions response = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/cast_members/" + expectedId))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(createCastMemberUseCase).execute(Mockito.argThat(actualCmd ->
                Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
        ));
    }

    @Test
    public void createCastMemberWithInvalidNullNameTest() throws Exception {
        // Arrange
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final String expectedErrorMessage = "'name' should not be null";

        final CreateCastMemberRequest aCommand = new CreateCastMemberRequest(null, expectedType);

        Mockito.when(createCastMemberUseCase.execute(Mockito.any())).thenThrow(new Exception("'name' should not be null"));

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.post("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final ResultActions response = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(createCastMemberUseCase).execute(Mockito.argThat(actualCmd ->
                Objects.equals(null, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
        ));
    }

    /* GET CASTMEMBER TESTS */

    @Test
    public void getCastMemberByIdTest() throws Exception {
        // Arrange
        final String expectedName = "Joao";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final CastMember aMember = CastMember.newMember(expectedName, expectedType);
        final String expectedId = aMember.getId();

        Mockito.when(getCastMemberByIdUseCase.execute(Mockito.any()))
                .thenReturn(aMember);

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.get("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final ResultActions response = this.mvc.perform(aRequest);

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type", Matchers.equalTo(expectedType.name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(aMember.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(aMember.getUpdatedAt().toString())));

        Mockito.verify(getCastMemberByIdUseCase).execute(Mockito.eq(expectedId));
    }

    /* UPDATE CASTMEMBER TESTS */

    @Test
    public void updateCastMemberTest() throws Exception {
        // Arrange
        final String expectedName = "Joao";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final CastMember aMember = CastMember.newMember(expectedName, expectedType);
        final String expectedId = aMember.getId();

        final UpdateCastMemberRequest aCommand =
                new UpdateCastMemberRequest(expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any()))
                .thenReturn(aMember);

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final ResultActions response = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(actualCmd ->
                Objects.equals(expectedId, actualCmd.id())
                        && Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
        ));
    }

    @Test
    public void updateCastMemberWithInvalidNameTest() throws Exception {
        // Arrange
        final CastMember aMember = CastMember.newMember("Vin Di", CastMemberType.DIRECTOR);
        final String expectedId = aMember.getId();

        final CastMemberType expectedType = CastMemberType.ACTOR;

        final String expectedErrorMessage = "'name' should not be null";

        final UpdateCastMemberRequest aCommand = new UpdateCastMemberRequest(null, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any())).thenThrow(new Exception("'name' should not be null"));

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final ResultActions response = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(actualCmd ->
                Objects.equals(expectedId, actualCmd.id())
                        && Objects.equals(null, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
        ));
    }

    @Test
    public void updateCastMemberWithInvalidIdTest() throws Exception {
        // Arrange
        final String expectedId = "123";

        final String expectedName = "Joao";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final String expectedErrorMessage = "CastMember with ID 123 was not found";

        final UpdateCastMemberRequest aCommand =
                new UpdateCastMemberRequest(expectedName, expectedType);

        Mockito.when(updateCastMemberUseCase.execute(Mockito.any()))
                .thenThrow(new CastMemberNotFoundException("CastMember with ID " + expectedId + " was not found"));

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.put("/cast_members/{id}", expectedId)
                .with(ApiTest.CAST_MEMBERS_JWT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final ResultActions response = this.mvc.perform(aRequest)
                .andDo(MockMvcResultHandlers.print());

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue()))
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateCastMemberUseCase).execute(Mockito.argThat(actualCmd ->
                Objects.equals(expectedId, actualCmd.id())
                        && Objects.equals(expectedName, actualCmd.name())
                        && Objects.equals(expectedType, actualCmd.type())
        ));
    }

    /* DELETE CASTMEMEBR TESTS */

    @Test
    public void deleteCastMemberTest() throws Exception {
        // Arrange
        final String expectedId = "123";

        Mockito.when(deleteCastMemberUseCase.execute(Mockito.any())).thenReturn("CastMember " + expectedId + " deleted");

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.delete("/cast_members/{id}", expectedId).with(ApiTest.CAST_MEMBERS_JWT);

        final ResultActions response = this.mvc.perform(aRequest);

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(deleteCastMemberUseCase).execute(Mockito.eq(expectedId));
    }

    /* GET ALL CASTMEMBERS TESTS */

    @Test
    public void getAllCastMembersWithParamsTest() throws Exception {
        // Arrange
        final CastMember aMember = CastMember.newMember("Pedro", CastMemberType.DIRECTOR);

        final int expectedPage = 1;
        final int expectedPerPage = 20;
        final String expectedTerms = "Alg";
        final String expectedSort = "type";
        final String expectedDirection = "desc";

        final int expectedItemsCount = 1;
        final int expectedTotal = 1;

        final List<CastMember> expectedItems = List.of(aMember);

        Mockito.when(getAllCastMemberUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.get("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("search", expectedTerms)
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .accept(MediaType.APPLICATION_JSON);

        final ResultActions response = this.mvc.perform(aRequest);

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perPage", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aMember.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aMember.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].type", Matchers.equalTo(aMember.getType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aMember.getCreatedAt().toString())));

        Mockito.verify(getAllCastMemberUseCase).execute(Mockito.argThat(aQuery ->
                Objects.equals(expectedPage, aQuery.page())
                        && Objects.equals(expectedPerPage, aQuery.perPage())
                        && Objects.equals(expectedTerms, aQuery.terms())
                        && Objects.equals(expectedSort, aQuery.sort())
                        && Objects.equals(expectedDirection, aQuery.direction())
        ));
    }

    @Test
    public void getAllCastMembersWithoutParamsTest() throws Exception {
        // Arrange
        final CastMember aMember = CastMember.newMember("Pedro", CastMemberType.DIRECTOR);

        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "";
        final String expectedSort = "name";
        final String expectedDirection = "asc";

        final int expectedItemsCount = 1;
        final int expectedTotal = 1;

        final List<CastMember> expectedItems = List.of(aMember);

        Mockito.when(getAllCastMemberUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // Act
        final MockHttpServletRequestBuilder aRequest = MockMvcRequestBuilders.get("/cast_members")
                .with(ApiTest.CAST_MEMBERS_JWT)
                .accept(MediaType.APPLICATION_JSON);

        final ResultActions response = this.mvc.perform(aRequest);

        // Assert
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPage", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.perPage", Matchers.equalTo(expectedPerPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aMember.getId())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aMember.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].type", Matchers.equalTo(aMember.getType().name())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aMember.getCreatedAt().toString())));

        Mockito.verify(getAllCastMemberUseCase).execute(Mockito.argThat(aQuery ->
                Objects.equals(expectedPage, aQuery.page())
                        && Objects.equals(expectedPerPage, aQuery.perPage())
                        && Objects.equals(expectedTerms, aQuery.terms())
                        && Objects.equals(expectedSort, aQuery.sort())
                        && Objects.equals(expectedDirection, aQuery.direction())
        ));
    }
}