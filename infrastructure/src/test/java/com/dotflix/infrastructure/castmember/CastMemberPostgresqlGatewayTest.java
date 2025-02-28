package com.dotflix.infrastructure.castmember;

import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.castmember.CastMember;
import com.dotflix.domain.castmember.CastMemberType;
import com.dotflix.infrastructure.PostgresqlGatewayTest;
import com.dotflix.infrastructure.castmember.persistence.CastMemberEntity;
import com.dotflix.infrastructure.castmember.persistence.CastMemberPostgresqlGateway;
import com.dotflix.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@PostgresqlGatewayTest
public class CastMemberPostgresqlGatewayTest {
    @Autowired
    private CastMemberPostgresqlGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testDependencies() {
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(castMemberRepository);
    }

    private void mockMembers() throws Exception {
        castMemberRepository.saveAllAndFlush(List.of(
                CastMemberEntity.fromDomain(CastMember.newMember("Kit Harington", CastMemberType.ACTOR)),
                CastMemberEntity.fromDomain(CastMember.newMember("Vin Diesel", CastMemberType.ACTOR)),
                CastMemberEntity.fromDomain(CastMember.newMember("Quentin Tarantino", CastMemberType.DIRECTOR)),
                CastMemberEntity.fromDomain(CastMember.newMember("Jason Momoa", CastMemberType.ACTOR)),
                CastMemberEntity.fromDomain(CastMember.newMember("Martin Scorsese", CastMemberType.DIRECTOR))
        ));
    }

    /* CREATE TESTS */

    @Test
    public void createCastMemberTest() throws Exception {
        // Arrange
        final String expectedName = "Joao";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final CastMember aMember = CastMember.newMember(expectedName, expectedType);
        final String expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        // Act
        final CastMember actualMember = castMemberGateway.create(CastMember.with(aMember));

        // Assert
        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());

        final CastMemberEntity persistedMember = castMemberRepository.findById(expectedId).get();

        Assertions.assertEquals(expectedId, persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), persistedMember.getUpdatedAt());
    }

    /* UPDATE TESTS */

    @Test
    public void updateCastMemberTest() throws Exception {
        // Arrange
        final String expectedName = "Joao";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final CastMember aMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);
        final String expectedId = aMember.getId();

        final CastMemberEntity currentMember = castMemberRepository.saveAndFlush(CastMemberEntity.fromDomain(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertEquals("vind", currentMember.getName());
        Assertions.assertEquals(CastMemberType.DIRECTOR, currentMember.getType());

        // Act
        final CastMember actualMember = castMemberGateway.update(CastMember.with(aMember).update(expectedName, expectedType));

        // Assert
        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertTrue(aMember.getUpdatedAt().isBefore(actualMember.getUpdatedAt()));

        final CastMemberEntity persistedMember = castMemberRepository.findById(expectedId).get();

        Assertions.assertEquals(expectedId, persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
        Assertions.assertTrue(aMember.getUpdatedAt().isBefore(persistedMember.getUpdatedAt()));
    }

    /* EXISTS BY ID TESTS */

    @Test
    public void existsByIdWithTwoCastMemberTest() throws Exception {
        // Arrange
        final CastMember aMember = CastMember.newMember("Vin", CastMemberType.DIRECTOR);

        final var expectedItems = 1;
        final String expectedId = aMember.getId();

        Assertions.assertEquals(0, castMemberRepository.count());

        castMemberRepository.saveAndFlush(CastMemberEntity.fromDomain(aMember));

        // Act
        final var actualMember = castMemberGateway.existsByIds(List.of("123", expectedId));

        // Assert
        Assertions.assertEquals(expectedItems, actualMember.size());
        Assertions.assertEquals(expectedId, actualMember.get(0));
    }

    /* DELETE CASTMEMBER TESTS */

    @Test
    public void deleteCastMemberTest() throws Exception {
        // Arrange
        final CastMember aMember = CastMember.newMember("Pedro", CastMemberType.DIRECTOR);

        castMemberRepository.saveAndFlush(CastMemberEntity.fromDomain(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // Act
        castMemberGateway.deleteById(aMember.getId());

        // Assert
        Assertions.assertEquals(0, castMemberRepository.count());
    }

    @Test
    public void deleteCastMemberWithInvalidIdTest() throws Exception {
        // Arrange
        final CastMember aMember = CastMember.newMember("Pedro", CastMemberType.DIRECTOR);

        castMemberRepository.saveAndFlush(CastMemberEntity.fromDomain(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // Act
        castMemberGateway.deleteById("123");

        // Assert
        Assertions.assertEquals(1, castMemberRepository.count());
    }

    /* GET CASTMEMBER TESTS */

    @Test
    public void getCastMemberTest() throws Exception {
        // Arrange
        final String expectedName = "Joao";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        final CastMember aMember = CastMember.newMember(expectedName, expectedType);
        final String expectedId = aMember.getId();

        castMemberRepository.saveAndFlush(CastMemberEntity.fromDomain(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // Act
        final CastMember actualMember = castMemberGateway.findById(expectedId).get();

        // Assert
        Assertions.assertEquals(expectedId, actualMember.getId());
        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
        Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());
    }

    @ParameterizedTest
    @CsvSource({
            "vin,0,10,1,1,Vin Diesel",
            "taran,0,10,1,1,Quentin Tarantino",
            "jas,0,10,1,1,Jason Momoa",
            "har,0,10,1,1,Kit Harington",
            "MAR,0,10,1,1,Martin Scorsese",
    })
    public void getAllCastMemberWithValidTermsTest(final String expectedTerms, final int expectedPage, final int expectedPerPage, final int expectedItemsCount, final long expectedTotal, final String expectedName) throws Exception{
        // Arrange
        mockMembers();

        final String expectedSort = "name";
        final String expectedDirection = "asc";

        final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final Pagination<CastMember> actualPage = castMemberGateway.findAll(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Jason Momoa",
            "name,desc,0,10,5,5,Vin Diesel",
            "createdAt,asc,0,10,5,5,Kit Harington",
            "createdAt,desc,0,10,5,5,Martin Scorsese",
    })
    public void getAllCastMembersWithValidSortAndDirectionTest(final String expectedSort, final String expectedDirection, final int expectedPage, final int expectedPerPage, final int expectedItemsCount, final long expectedTotal, final String expectedName) throws Exception {
        // Arrange
        mockMembers();

        final String expectedTerms = "";

        final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final Pagination<CastMember> actualPage = castMemberGateway.findAll(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Jason Momoa;Kit Harington",
            "1,2,2,5,Martin Scorsese;Quentin Tarantino",
            "2,2,1,5,Vin Diesel",
    })
    public void getAllCastMembersWithValidPaginationTest(final int expectedPage, final int expectedPerPage, final int expectedItemsCount, final long expectedTotal, final String expectedNames) throws Exception {
        // Arrange
        mockMembers();

        final String expectedTerms = "";
        final String expectedSort = "name";
        final String expectedDirection = "asc";

        final SearchQuery aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final Pagination<CastMember> actualPage = castMemberGateway.findAll(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final String expectedName : expectedNames.split(";")) {
            Assertions.assertEquals(expectedName, actualPage.items().get(index).getName());
            index++;
        }
    }
}