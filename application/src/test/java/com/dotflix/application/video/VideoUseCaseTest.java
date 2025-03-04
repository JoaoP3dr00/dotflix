package com.dotflix.application.video;

import com.dotflix.application.UseCaseTest;
import com.dotflix.application.video.dto.CreateVideoDTO;
import com.dotflix.domain.castmember.CastMember;
import com.dotflix.domain.castmember.CastMemberGateway;
import com.dotflix.domain.castmember.CastMemberType;
import com.dotflix.domain.category.Category;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.genre.Genre;
import com.dotflix.domain.genre.GenreGateway;
import com.dotflix.domain.utils.IdUtils;
import com.dotflix.domain.video.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * TESTS CHECK IN THIS CLASS
 * CREATE TESTS:
 *      - CREATE -> OK
 *      - CREATE WITH NULL TITLE -> OK
 *      - CREATE WITH EMPTY TITLE
 *      - CREATE WITH NULL RATING TEST -> OK
 *      - CREATE WITH NULL LAUNCH YEAR
 *      - CREATE AND SOME CATEGORIES DOES NOT EXISTS -> OK
 *      - CREATE AND SOME GENRES DOES NOT EXISTS -> OK
 *      - CREATE AND SOME CASTMEMBERS DOES NOT EXISTS -> OK
 *      - CREATE AND THROWS EXCEPTION -> OK
 *
 */
public class VideoUseCaseTest extends UseCaseTest {
    @InjectMocks
    private CreateVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Mock
    private GenreGateway genreGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway, categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway);
    }

    /* CREATE TESTS */

    @Test
    public void createVideoTest() throws Exception {
        // Arrange
        final Category aula = Category.newCategory("Aulas", "Some description", true);
        final Genre genre = Genre.newGenre("Technology", true);
        final CastMember castmember = CastMember.newMember("Joao", CastMemberType.ACTOR);

        final String expectedTitle = "Filme";
        final String expectedDescription = "Um filme bom!!!";
        final Year expectedLaunchYear = Year.of(11);
        final double expectedDuration = 333.3;
        final boolean expectedOpened = true;
        final boolean expectedPublished = false;
        final Rating expectedRating = Rating.ER;
        final Set<String> expectedCategories = Set.of(aula.getId());
        final var expectedGenres = Set.of(genre.getId());
        final Set<String> expectedMembers = Set.of(castmember.getId());

        final Resource expectedVideo = resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = resource(VideoMediaType.BANNER);
        final Resource expectedThumb = resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = resource(VideoMediaType.THUMBNAIL_HALF);

        final CreateVideoDTO aCommand = new CreateVideoDTO(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedMembers,
                expectedVideo,
                expectedTrailer,
                expectedBanner,
                expectedThumb,
                expectedThumbHalf
        );

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        Mockito.when(castMemberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(videoGateway.create(Mockito.any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

        // Act
        final var actualResult = useCase.execute(aCommand);

        // Assert
        Assertions.assertNotNull(actualResult);
        Assertions.assertNotNull(actualResult.getId());

        Mockito.verify(videoGateway).create(Mockito.argThat(actualVideo ->
                Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear.getValue(), actualVideo.getLaunchedAt().getValue())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
        ));
    }

    @Test
    public void createVideoWithNullTitleTest() {
        // Arrange
        final var expectedErrorMessage = "'title' should not be null";

        final String expectedDescription = "Um filme bom!!!";
        final Year expectedLaunchYear = Year.of(11);
        final double expectedDuration = 333.3;
        final boolean expectedOpened = true;
        final boolean expectedPublished = false;
        final Rating expectedRating = Rating.ER;
        final Set<String> expectedCategories = Set.of("123");
        final Set<String> expectedGenres = Set.of("123");
        final Set<String> expectedMembers = Set.of("123");

        final CreateVideoDTO aCommand = new CreateVideoDTO(
                null,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedMembers,
                null,
                null,
                null,
                null,
                null
        );

        // Act
        final var actualException = Assertions.assertThrows(Exception.class, () -> useCase.execute(aCommand));

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeAudioVideo(Mockito.any(),Mockito. any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(videoGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void createVideoWithNullRatingTest() throws Exception {
        // Arrange
        final String expectedErrorMessage = "'rating' should not be null";

        final Category aula = Category.newCategory("Aulas", "Some description", true);
        final Genre genre = Genre.newGenre("Technology", true);
        final CastMember castmember = CastMember.newMember("Joao", CastMemberType.ACTOR);

        final String expectedTitle = "Filme";
        final String expectedDescription = "Um filme bom!!!";
        final Year expectedLaunchYear = Year.of(11);
        final double expectedDuration = 333.3;
        final boolean expectedOpened = true;
        final boolean expectedPublished = false;
        final Set<String> expectedCategories = Set.of(aula.getId());
        final Set<String> expectedGenres = Set.of(genre.getId());
        final Set<String> expectedMembers = Set.of(castmember.getId());

        final CreateVideoDTO aCommand = new CreateVideoDTO(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                null,
                expectedCategories,
                expectedGenres,
                expectedMembers,
                null,
                null,
                null,
                null,
                null
        );

        // Act
        final var actualException = Assertions.assertThrows(Exception.class, () -> useCase.execute(aCommand));

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(castMemberGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(),Mockito. any());
        Mockito.verify(genreGateway, Mockito.times(0)).existsByIds(Mockito.any());
        Mockito.verify(videoGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void createVideoAndSomeCategoriesDoesNotExistsTest() throws Exception {
        // Arrange
        final Category aula = Category.newCategory("Aulas", "Some description", true);
        final Genre genre = Genre.newGenre("Technology", true);
        final CastMember castmember = CastMember.newMember("Joao", CastMemberType.ACTOR);

        final String expectedErrorMessage = "Some categories could not be found: [%s]".formatted(aula.getId());

        final String expectedTitle = "Filme";
        final String expectedDescription = "Um filme bom!!!";
        final Year expectedLaunchYear = Year.of(2022);
        final double expectedDuration = 333.3;
        final boolean expectedOpened = true;
        final boolean expectedPublished = false;
        final Rating expectedRating = Rating.ER;
        final Set<String> expectedCategories = Set.of(aula.getId());
        final Set<String> expectedGenres = Set.of(genre.getId());
        final Set<String> expectedMembers = Set.of(castmember.getId());

        final CreateVideoDTO aCommand = new CreateVideoDTO(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear.getValue(),
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating.getName(),
                expectedCategories,
                expectedGenres,
                expectedMembers,
                null,
                null,
                null,
                null,
                null
        );

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>());

        Mockito.when(castMemberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        // Act
        final var actualException = Assertions.assertThrows(Exception.class, () -> useCase.execute(aCommand));

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedCategories));
        Mockito.verify(castMemberGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedMembers));
        Mockito.verify(genreGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedGenres));
        Mockito.verify(mediaResourceGateway, Mockito.times(0)).storeImage(Mockito.any(),Mockito. any());
        Mockito.verify(videoGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void createVideoThrowsExceptionTest() throws Exception {
        // Arrange
        final String expectedErrorMessage = "An error on create video was observed [videoId:";
        final Category aula = Category.newCategory("Aulas", "Some description", true);
        final Category aula2 = Category.newCategory("Aulas2", "Some description2", true);

        final Genre genre = Genre.newGenre("Technology", true);
        final Genre genre2 = Genre.newGenre("Business", true);

        final CastMember castmember = CastMember.newMember("Joao", CastMemberType.ACTOR);
        final CastMember castmember2 = CastMember.newMember("Pedro", CastMemberType.ACTOR);

        final String expectedTitle = "Filme";
        final String expectedDescription = "Um filme bom!!!";
        final Year expectedLaunchYear = Year.of(11);
        final double expectedDuration = 333.3;
        final boolean expectedOpened = true;
        final boolean expectedPublished = false;
        final Rating expectedRating = Rating.ER;
        final Set<String> expectedCategories = Set.of(aula.getId(), aula2.getId());
        final Set<String> expectedGenres = Set.of(genre.getId(), genre2.getId());
        final Set<String> expectedMembers = Set.of(castmember.getId(), castmember2.getId());

        final Resource expectedVideo = resource(VideoMediaType.VIDEO);
        final Resource expectedTrailer = resource(VideoMediaType.TRAILER);
        final Resource expectedBanner = resource(VideoMediaType.BANNER);
        final Resource expectedThumb = resource(VideoMediaType.THUMBNAIL);
        final Resource expectedThumbHalf = resource(VideoMediaType.THUMBNAIL_HALF);

        final CreateVideoDTO aCommand = new CreateVideoDTO(expectedTitle, expectedDescription, expectedLaunchYear.getValue(), expectedDuration, expectedOpened, expectedPublished, expectedRating.getName(), expectedCategories, expectedGenres, expectedMembers, expectedVideo, expectedTrailer, expectedBanner, expectedThumb, expectedThumbHalf);

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedCategories));

        Mockito.when(castMemberGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedMembers));

        Mockito.when(genreGateway.existsByIds(Mockito.any()))
                .thenReturn(new ArrayList<>(expectedGenres));

        mockImageMedia();
        mockAudioVideoMedia();

        Mockito.when(videoGateway.create(Mockito.any()))
                .thenThrow(new RuntimeException("Internal Server Error"));

        // Act
        final var actualResult = Assertions.assertThrows(InternalError.class, () -> useCase.execute(aCommand));

        // Assert
        Assertions.assertNotNull(actualResult);
        Assertions.assertTrue(actualResult.getMessage().startsWith(expectedErrorMessage));

        Mockito.verify(mediaResourceGateway).clearResources(Mockito.any());
    }

    private void mockImageMedia() {
        Mockito.when(mediaResourceGateway.storeImage(Mockito.any(),Mockito.any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            final var resource = videoResource.resource();
            return ImageMedia.with(resource.checksum(), resource.name(), "/img");
        });
    }

    private void mockAudioVideoMedia() {
        Mockito.when(mediaResourceGateway.storeAudioVideo(Mockito.any(),Mockito.any())).thenAnswer(t -> {
            final var videoResource = t.getArgument(1, VideoResource.class);
            final var resource = videoResource.resource();
            return AudioVideoMedia.with(
                    resource.checksum(),
                    resource.name(),
                    "/img"
            );
        });
    }

    public Resource resource(final VideoMediaType type){
        String contentType;

        if(type == VideoMediaType.VIDEO || type == VideoMediaType.TRAILER)
            contentType = "video/mp4";
        else
            contentType = "image/jpg";

        final String checksum = IdUtils.uuid();
        final byte[] content = "Conteudo".getBytes();

        return Resource.with(content, checksum, contentType, type.name().toLowerCase());
    }

    /* DELETE TESTS */
}
