package com.dotflix.application.video;

import com.dotflix.application.UseCase;
import com.dotflix.application.video.dto.CreateVideoDTO;
import com.dotflix.domain.castmember.CastMemberGateway;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.genre.GenreGateway;
import com.dotflix.domain.video.*;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

public class CreateVideoUseCase extends UseCase<CreateVideoDTO, Video> {
    private final CategoryGateway categoryGateway;
    private final CastMemberGateway castMemberGateway;
    private final GenreGateway genreGateway;
    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public CreateVideoUseCase(final CategoryGateway categoryGateway, final CastMemberGateway castMemberGateway, final GenreGateway genreGateway, final MediaResourceGateway mediaResourceGateway, final VideoGateway videoGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Video execute(final CreateVideoDTO aCommand) throws Exception {
        final Rating aRating = Rating.of(aCommand.rating()).orElse(null);
        final Year aLaunchYear = aCommand.launchedAt() != null ? Year.of(aCommand.launchedAt()) : null;
        final Set<String> categories = aCommand.categories();
        final Set<String> genres = aCommand.genres();
        final Set<String> members = aCommand.members();

        final Video aVideo = Video.newVideo(
                aCommand.title(),
                aCommand.description(),
                aLaunchYear,
                aCommand.duration(),
                aCommand.opened(),
                aCommand.published(),
                aRating,
                categories,
                genres,
                members
        );

        validate("cast members", members, castMemberGateway::existsByIds);
        validate("genres", genres, genreGateway::existsByIds);
        validate("categories", categories, categoryGateway::existsByIds);

        return create(aCommand, aVideo);
    }

    private Video create(final CreateVideoDTO aCommand, final Video aVideo) {
        final String anId = aVideo.getId();

        try {
            final AudioVideoMedia aVideoMedia = aCommand.getVideo()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(VideoMediaType.VIDEO, it)))
                    .orElse(null);

            final AudioVideoMedia aTrailerMedia = aCommand.getTrailer()
                    .map(it -> this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(VideoMediaType.TRAILER, it)))
                    .orElse(null);

            final ImageMedia aBannerMedia = aCommand.getBanner()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(VideoMediaType.BANNER, it)))
                    .orElse(null);

            final ImageMedia aThumbnailMedia = aCommand.getThumbnail()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(VideoMediaType.THUMBNAIL, it)))
                    .orElse(null);

            final ImageMedia aThumbHalfMedia = aCommand.getThumbnailHalf()
                    .map(it -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(VideoMediaType.THUMBNAIL_HALF, it)))
                    .orElse(null);

            return this.videoGateway.create(
                    aVideo
                        .updateVideoMedia(aVideoMedia)
                        .updateTrailerMedia(aTrailerMedia)
                        .updateBannerMedia(aBannerMedia)
                        .updateThumbnailMedia(aThumbnailMedia)
                        .updateThumbnailHalfMedia(aThumbHalfMedia)
            );
        } catch (final Throwable t) {
            this.mediaResourceGateway.clearResources(anId);
            throw new InternalError("An error on create video was observed [videoId:%s]".formatted(anId));
        }
    }

    private <T> void validate(final String aggregate, final Set<T> ids, final Function<Iterable<T>, List<T>> existsByIds) throws Exception {
        if (ids == null || ids.isEmpty()) {
            throw new Exception(aggregate + " ids is empty or null");
        }

        final List<T> retrievedIds = existsByIds.apply(ids);

        if (ids.size() != retrievedIds.size()) {
            final ArrayList<T> missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            throw new Exception("Some %s could not be found: %s".formatted(aggregate, missingIds));
        }
    }
}
