package com.dotflix.domain.castmember;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest {
    @Test
    public void createNewCastMemberTest() {
        // Arrange
        final String expectedName = "Vin Diesel";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        // Act
        try {
            final CastMember actualMember = CastMember.newMember(expectedName, expectedType);

            // Assert
            Assertions.assertNotNull(actualMember);
            Assertions.assertNotNull(actualMember.getId());
            Assertions.assertEquals(expectedName, actualMember.getName());
            Assertions.assertEquals(expectedType, actualMember.getType());
            Assertions.assertNotNull(actualMember.getCreatedAt());
            Assertions.assertNotNull(actualMember.getUpdatedAt());
            Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
        } catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    @Test
    public void createNewCastMemberWithInvalidNullNameTest() {
        // Arrange
        final CastMemberType expectedType = CastMemberType.ACTOR;
        final String expectedErrorMessage = "'name' should not be null";

        // Act
        final Exception actualException = Assertions.assertThrows(
                Exception.class,
                () -> CastMember.newMember(null, expectedType)
        );

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void createNewCastMemberWithInvalidEmptyNameTest() {
        // Arrange
        final String expectedName = " ";
        final CastMemberType expectedType = CastMemberType.ACTOR;
        final String expectedErrorMessage = "'name' should not be empty";

        // Act
        final Exception actualException = Assertions.assertThrows(
                Exception.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void createNewCastMemberWithInvalidLongNameTest() {
        // Arrange
        final String expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """;
        final CastMemberType expectedType = CastMemberType.ACTOR;
        final String expectedErrorMessage = "'name' should have between 3 and 255 characters";

        // Act
        final Exception actualException = Assertions.assertThrows(
                Exception.class,
                () -> CastMember.newMember(expectedName, expectedType)
        );

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void createNewCastMemberWithInvalidTypeTest() {
        // Arrange
        final String expectedName = "Vin Diesel";
        final String expectedErrorMessage = "'type' should not be null";

        // Act
        final Exception actualException = Assertions.assertThrows(
                Exception.class,
                () -> CastMember.newMember(expectedName, null)
        );

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    /* UPDATE CASTMEMBER TESTS */

    @Test
    public void updateCastMemberTest() {
        // Arrange
        final String expectedName = "Vin Diesel";
        final CastMemberType expectedType = CastMemberType.ACTOR;

        try {
            final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

            Assertions.assertNotNull(actualMember);
            Assertions.assertNotNull(actualMember.getId());

            final var actualID = actualMember.getId();
            final var actualCreatedAt = actualMember.getCreatedAt();
            final var actualUpdatedAt = actualMember.getUpdatedAt();

            // Act
            actualMember.update(expectedName, expectedType);

            // Assert
            Assertions.assertEquals(actualID, actualMember.getId());
            Assertions.assertEquals(expectedName, actualMember.getName());
            Assertions.assertEquals(expectedType, actualMember.getType());
            Assertions.assertEquals(actualCreatedAt, actualMember.getCreatedAt());
            Assertions.assertTrue(actualUpdatedAt.isBefore(actualMember.getUpdatedAt()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void updateCastMemberWithInvalidNullNameTest() {
        // Arrange
        final CastMemberType expectedType = CastMemberType.ACTOR;
        final String expectedErrorMessage = "'name' should not be null";

        try {
            final CastMember actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

            Assertions.assertNotNull(actualMember);
            Assertions.assertNotNull(actualMember.getId());

            // Act
            final var actualException = Assertions.assertThrows(
                    Exception.class,
                    () -> actualMember.update(null, expectedType)
            );

            // Assert
            Assertions.assertNotNull(actualException);
            Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void updateCastMemberWithInvalidTypeTest() {
        // Arrange
        final String expectedName = "Vin Diesel";
        final String expectedErrorMessage = "'type' should not be null";

        try {
            final CastMember actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

            Assertions.assertNotNull(actualMember);
            Assertions.assertNotNull(actualMember.getId());

            // Act
            final var actualException = Assertions.assertThrows(
                    Exception.class,
                    () -> actualMember.update(expectedName, null)
            );

            // Assert
            Assertions.assertNotNull(actualException);
            Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}