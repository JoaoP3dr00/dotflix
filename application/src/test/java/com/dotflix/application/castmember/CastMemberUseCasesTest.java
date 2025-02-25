package com.dotflix.application.castmember;

import com.dotflix.application.UseCaseTest;
import com.dotflix.application.castmember.dto.CreateCastMemberDTO;
import com.dotflix.application.castmember.dto.UpdateCastMemberDTO;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.castmember.CastMember;
import com.dotflix.domain.castmember.CastMemberGateway;
import com.dotflix.domain.castmember.CastMemberType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CastMemberUseCasesTest extends UseCaseTest {
    @InjectMocks
    private CreateCastMemberUseCase createCastMemberUseCase;

    @InjectMocks
    private DeleteCastMemberUseCase deleteCastMemberUseCase;

    @InjectMocks
    private GetCastMemberByIdUseCase getCastMemberByIdUseCase;

    @InjectMocks
    private GetAllCastMemberUseCase getAllCastMemberUseCase;

    @InjectMocks
    private UpdateCastMemberUseCase updateCastMemberUseCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    /* CREATE CASTMEMBER TESTS */

    @Test
    public void createCastMemberTest() throws Exception {
        // Arrange
        final String expectedName = "Estuarine Neilson";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final CreateCastMemberDTO createCastMemberDTO = new CreateCastMemberDTO(expectedName, expectedType);

        Mockito.when(castMemberGateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Act
        final CastMember castMember = createCastMemberUseCase.execute(createCastMemberDTO);

        // Assert
        Assertions.assertNotNull(castMember);
        Assertions.assertNotNull(castMember.getId());

        Mockito.verify(castMemberGateway).create(Mockito.argThat(member ->
                Objects.nonNull(member.getId())
                        && Objects.equals(expectedName, member.getName())
                        && Objects.equals(expectedType, member.getType())
                        && Objects.nonNull(member.getCreatedAt())
                        && Objects.nonNull(member.getUpdatedAt())
        ));
    }

    @Test
    public void createCastMemberWithInvalidNullNameTest() {
        // Arrange
        final String expectedName = "Estuarine Neilson";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final String expectedErrorMessage = "'name' should not be null";

        final CreateCastMemberDTO createCastMemberDTO = new CreateCastMemberDTO(null, expectedType);

        // Act
        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
            createCastMemberUseCase.execute(createCastMemberDTO);
        });

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void createCastMemberWithInvalidTypeTest() {
        // Arrange
        final String expectedName = "Estuarine Neilson";

        final String expectedErrorMessage = "'type' should not be null";

        final CreateCastMemberDTO createCastMemberDTO = new CreateCastMemberDTO(expectedName, null);

        // Act
        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
            createCastMemberUseCase.execute(createCastMemberDTO);
        });

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway, Mockito.times(0)).create(Mockito.any());
    }

    /* DELETE CASTMEMBER TESTS */

    @Test
    public void deleteCastMemberTest() throws Exception {
        // Arrange
        final CastMember castMember = CastMember.newMember("Estuarine Neilson", CastMemberType.ACTOR);

        final String expectedId = castMember.getId();

        Mockito.doNothing().when(castMemberGateway).deleteById(Mockito.any());

        // Act
        Assertions.assertDoesNotThrow(() -> deleteCastMemberUseCase.execute(expectedId));

        // Assert
        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void deleteCastMemberAndGatewayThrowsException() throws Exception {
        // Arrange
        final CastMember castMember = CastMember.newMember("Estuarine Neilson", CastMemberType.ACTOR);

        final String expectedId = castMember.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error")).when(castMemberGateway).deleteById(Mockito.any());

        // Act
        Assertions.assertThrows(IllegalStateException.class, () -> deleteCastMemberUseCase.execute(expectedId));

        // Assert
        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));
    }

    /* GET CASTMEMBER TESTS */
    @Test
    public void getCastMemberTest() throws Exception {
        // Arrange
        final String expectedName = "Estuarine Neilson";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final CastMember castMember = CastMember.newMember(expectedName, expectedType);

        final String expectedId = castMember.getId();

        Mockito.when(castMemberGateway.findById(Mockito.any())).thenReturn(Optional.of(castMember));

        // Act
        final CastMember actualOutput = getCastMemberByIdUseCase.execute(expectedId);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.getId());
        Assertions.assertEquals(expectedName, actualOutput.getName());
        Assertions.assertEquals(expectedType, actualOutput.getType());
        Assertions.assertEquals(castMember.getCreatedAt(), actualOutput.getCreatedAt());
        Assertions.assertEquals(castMember.getUpdatedAt(), actualOutput.getUpdatedAt());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
    }

    @Test
    public void getAllCastMemberTest() throws Exception {
        // Arrange
        final List<CastMember> members = List.of(CastMember.newMember("Estuarine Neilson", CastMemberType.ACTOR), CastMember.newMember("Estuprilson Negrilson", CastMemberType.ACTOR));

        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "Algo";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";
        final int expectedTotal = 2;

        final List<CastMember> expectedItems = members.stream().toList();

        final Pagination<CastMember> expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotal, members);

        Mockito.when(castMemberGateway.findAll(Mockito.any())).thenReturn(expectedPagination);

        final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final Pagination<CastMember> actualOutput = getAllCastMemberUseCase.execute(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(castMemberGateway).findAll(Mockito.eq(aQuery));
    }

    /* UPDATE CASTMEMBER TESTS */

    @Test
    public void updateCastMemberTest() throws Exception {
        // Arrange
        final CastMember aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final String expectedId = aMember.getId();
        final String expectedName = "Estuarine Neilson";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final UpdateCastMemberDTO updateCastMemberDTO = new UpdateCastMemberDTO(expectedId, expectedName, expectedType);

        Mockito.when(castMemberGateway.findById(Mockito.any())).thenReturn(Optional.of(CastMember.with(aMember)));

        Mockito.when(castMemberGateway.update(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Act
        final CastMember actualOutput = updateCastMemberUseCase.execute(updateCastMemberDTO);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId, actualOutput.getId());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));

        Mockito.verify(castMemberGateway).update(Mockito.argThat(aUpdatedMember ->
                Objects.equals(expectedId, aUpdatedMember.getId())
                        && Objects.equals(expectedName, aUpdatedMember.getName())
                        && Objects.equals(expectedType, aUpdatedMember.getType())
                        && Objects.equals(aMember.getCreatedAt(), aUpdatedMember.getCreatedAt())
                        && aMember.getUpdatedAt().isBefore(aUpdatedMember.getUpdatedAt())
        ));
    }

    @Test
    public void updateCastMemberWithInvalidNameTest() throws Exception{
        // Arrange
        final CastMember aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final String expectedId = aMember.getId();
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final String expectedErrorMessage = "'name' should not be null";

        final UpdateCastMemberDTO updateCastMemberDTO = new UpdateCastMemberDTO(expectedId, null, expectedType);

        Mockito.when(castMemberGateway.findById(Mockito.any())).thenReturn(Optional.of(aMember));

        // Act
        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
            updateCastMemberUseCase.execute(updateCastMemberDTO);
        });

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
        Mockito.verify(castMemberGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void updateCastMemberWithInvalidTypeTest() throws Exception{
        // Arrange
        final CastMember aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final String expectedId = aMember.getId();
        final String expectedName = "Estuarine Neilson";

        final String expectedErrorMessage = "'type' should not be null";

        final UpdateCastMemberDTO updateCastMemberDTO = new UpdateCastMemberDTO(expectedId, expectedName, null);

        Mockito.when(castMemberGateway.findById(Mockito.any())).thenReturn(Optional.of(aMember));

        // Act
        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
            updateCastMemberUseCase.execute(updateCastMemberDTO);
        });

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
        Mockito.verify(castMemberGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void updateCastMemberWithInvalidIdTest() throws Exception{
        // Arrange
        final CastMember aMember = CastMember.newMember("vin diesel", CastMemberType.DIRECTOR);

        final String expectedId = "123";
        final String expectedName = "Estuarine Neilson";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final String expectedErrorMessage = "CastMember with ID 123 was not found";

        final UpdateCastMemberDTO updateCastMemberDTO = new UpdateCastMemberDTO(expectedId, expectedName, expectedType);

        Mockito.when(castMemberGateway.findById(Mockito.any())).thenReturn(Optional.empty());

        // Act
        final Exception actualException = Assertions.assertThrows(Exception.class, () -> {
                updateCastMemberUseCase.execute(updateCastMemberDTO);
            }
        );

        // Assert
        Assertions.assertNotNull(actualException);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(castMemberGateway).findById(Mockito.eq(expectedId));
        Mockito.verify(castMemberGateway, Mockito.times(0)).update(Mockito.any());
    }
}
