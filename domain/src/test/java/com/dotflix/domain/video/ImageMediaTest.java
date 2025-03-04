package com.dotflix.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImageMediaTest {

    @Test
    public void createNewImageTest() {
        // Arrange
        final var expectedChecksum = "abc";
        final var expectedName = "Banner.png";
        final var expectedLocation = "/images/ac";

        // Act
        final var actualImage =
                ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        // Assert
        Assertions.assertNotNull(actualImage);
        Assertions.assertEquals(expectedChecksum, actualImage.checksum());
        Assertions.assertEquals(expectedName, actualImage.name());
        Assertions.assertEquals(expectedLocation, actualImage.location());
    }

    @Test
    public void compareTwoImagesWithEqualsTest() {
        // Arrange
        final var expectedChecksum = "abc";
        final var expectedLocation = "/images/ac";

        final var img1 =
                ImageMedia.with(expectedChecksum, "Random", expectedLocation);

        final var img2 =
                ImageMedia.with(expectedChecksum, "Simple", expectedLocation);

        // Assert
        Assertions.assertEquals(img1, img2);
        Assertions.assertNotSame(img1, img2);
    }

    @Test
    public void withTest() {
        Assertions.assertThrows(
                NullPointerException.class,
                () -> ImageMedia.with(null, "Random", "/images")
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> ImageMedia.with("abc", null, "/images")
        );

        Assertions.assertThrows(
                NullPointerException.class,
                () -> ImageMedia.with("abc", "Random", null)
        );
    }
}