package com.dotflix.domain.video;

import com.dotflix.domain.events.DomainEvent;
import com.dotflix.domain.events.DomainEventPublisher;

import java.time.Instant;
import java.time.Year;
import java.util.*;

public class Video {
    public String id;
    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;
    private boolean opened;
    private boolean published;
    private Instant createdAt;
    private Instant updatedAt;
    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;
    private AudioVideoMedia trailer;
    private AudioVideoMedia video;
    private Set<String> categories;
    private Set<String> genres;
    private Set<String> castMembers;
    private final List<DomainEvent> domainEvents;

    protected Video(
            final String anId,
            final String aTitle,
            final String aDescription,
            final Year aLaunchYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final ImageMedia aBanner,
            final ImageMedia aThumb,
            final ImageMedia aThumbHalf,
            final AudioVideoMedia aTrailer,
            final AudioVideoMedia aVideo,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members,
            final List<DomainEvent> domainEvents
    ) throws Exception {
        this.id = anId;
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchYear;
        this.duration = aDuration;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.rating = aRating;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        this.banner = aBanner;
        this.thumbnail = aThumb;
        this.thumbnailHalf = aThumbHalf;
        this.trailer = aTrailer;
        this.video = aVideo;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = members;
        this.domainEvents = new ArrayList<>(domainEvents == null ? Collections.emptyList() : domainEvents);
        validate();
    }

    public void validate() throws Exception {
        // Check title
        if (title == null) {
            throw new Exception("'title' should not be null");
        }

        if (title.isBlank()) {
            throw new Exception("'title' should not be empty");
        }

        final int length = title.trim().length();
        if (length > 255) {
            throw new Exception("'title' must be between 1 and 255 characters");
        }

        // Check description
        if (description == null) {
            throw new Exception("'description' should not be null");
        }

        if (description.isBlank()) {
            throw new Exception("'description' should not be empty");
        }

        final int descLength = description.trim().length();
        if (descLength > 4_000) {
            throw new Exception("'description' must be between 1 and 4000 characters");
        }

        // Check launchedAt
        if (getLaunchedAt() == null) {
            throw new Exception("'launchedAt' should not be null");
        }

        // Check rating
        if (getRating() == null) {
            throw new Exception("'rating' should not be null");
        }
    }

    public Video update(
            final String aTitle,
            final String aDescription,
            final Year aLaunchYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members
    ) {
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchYear;
        this.duration = aDuration;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.rating = aRating;
        this.setCategories(categories);
        this.setGenres(genres);
        this.setCastMembers(members);
        this.updatedAt = Instant.now();
        return this;
    }

    public Video updateBannerMedia(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video updateThumbnailMedia(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video updateThumbnailHalfMedia(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = Instant.now();
        return this;
    }

    public Video updateTrailerMedia(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = Instant.now();
        onAudioVideoMediaUpdated(trailer);
        return this;
    }

    public Video updateVideoMedia(final AudioVideoMedia video) {
        this.video = video;
        this.updatedAt = Instant.now();
        onAudioVideoMediaUpdated(video);
        return this;
    }

    public String getId() { return this.id; }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public double getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean getOpened() {
        return opened;
    }

    public boolean getPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Set<String> getCategories() {
        return categories != null ? Collections.unmodifiableSet(categories) : Collections.emptySet();
    }

    public Set<String> getGenres() {
        return genres != null ? Collections.unmodifiableSet(genres) : Collections.emptySet();
    }

    public Set<String> getCastMembers() {
        return castMembers != null ? Collections.unmodifiableSet(castMembers) : Collections.emptySet();
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(video);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }

    private void setCategories(final Set<String> categories) {
        this.categories = categories != null ? new HashSet<>(categories) : Collections.emptySet();
    }

    private void setGenres(final Set<String> genres) {
        this.genres = genres != null ? new HashSet<>(genres) : Collections.emptySet();
    }

    private void setCastMembers(final Set<String> castMembers) {
        this.castMembers = castMembers != null ? new HashSet<>(castMembers) : Collections.emptySet();
    }

    public static Video newVideo(
            final String aTitle,
            final String aDescription,
            final Year aLaunchYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members
    ) throws Exception {
        Random r = new Random();
        final int id = r.nextInt(1000);
        final var now = Instant.now();

        return new Video(
                Integer.toString(id),
                aTitle,
                aDescription,
                aLaunchYear,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                now,
                now,
                null,
                null,
                null,
                null,
                null,
                categories,
                genres,
                members,
                null
        );
    }

    public static Video with(final Video aVideo) throws Exception {
        return new Video(
                aVideo.getId(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt(),
                aVideo.getDuration(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getRating(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null),
                aVideo.getTrailer().orElse(null),
                aVideo.getVideo().orElse(null),
                new HashSet<>(aVideo.getCategories()),
                new HashSet<>(aVideo.getGenres()),
                new HashSet<>(aVideo.getCastMembers()),
                aVideo.getDomainEvents()
        );
    }

    public static Video with(
            final String anId,
            final String aTitle,
            final String aDescription,
            final Year aLaunchYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final ImageMedia aBanner,
            final ImageMedia aThumb,
            final ImageMedia aThumbHalf,
            final AudioVideoMedia aTrailer,
            final AudioVideoMedia aVideo,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members
    )  throws Exception {
        return new Video(
                anId,
                aTitle,
                aDescription,
                aLaunchYear,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                aCreationDate,
                aUpdateDate,
                aBanner,
                aThumb,
                aThumbHalf,
                aTrailer,
                aVideo,
                categories,
                genres,
                members,
                null
        );
    }

    public Video processing(final VideoMediaType aType) {
        if (VideoMediaType.VIDEO == aType) {
            getVideo()
                    .ifPresent(media -> updateVideoMedia(media.processing()));
        } else if (VideoMediaType.TRAILER == aType) {
            getTrailer()
                    .ifPresent(media -> updateTrailerMedia(media.processing()));
        }

        return this;
    }

    public Video completed(final VideoMediaType aType, final String encodedPath) {
        if (VideoMediaType.VIDEO == aType) {
            getVideo()
                    .ifPresent(media -> updateVideoMedia(media.completed(encodedPath)));
        } else if (VideoMediaType.TRAILER == aType) {
            getTrailer()
                    .ifPresent(media -> updateTrailerMedia(media.completed(encodedPath)));
        }

        return this;
    }

    private void onAudioVideoMediaUpdated(final AudioVideoMedia media) {
        if (media != null && media.isPendingEncode()) {
            this.registerEvent(new VideoMediaCreated(getId(), media.rawLocation()));
        }
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void publishDomainEvents(final DomainEventPublisher publisher) {
        if (publisher == null) {
            return;
        }

        getDomainEvents()
                .forEach(publisher::publishEvent);

        this.domainEvents.clear();
    }

    public void registerEvent(final DomainEvent event) {
        if (event == null) {
            return;
        }

        this.domainEvents.add(event);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Video video1)) return false;
        return Double.compare(getDuration(), video1.getDuration()) == 0 && getOpened() == video1.getOpened() && getPublished() == video1.getPublished() && Objects.equals(getId(), video1.getId()) && Objects.equals(getTitle(), video1.getTitle()) && Objects.equals(getDescription(), video1.getDescription()) && Objects.equals(getLaunchedAt(), video1.getLaunchedAt()) && getRating() == video1.getRating() && Objects.equals(getCreatedAt(), video1.getCreatedAt()) && Objects.equals(getUpdatedAt(), video1.getUpdatedAt()) && Objects.equals(getBanner(), video1.getBanner()) && Objects.equals(getThumbnail(), video1.getThumbnail()) && Objects.equals(getThumbnailHalf(), video1.getThumbnailHalf()) && Objects.equals(getTrailer(), video1.getTrailer()) && Objects.equals(getVideo(), video1.getVideo()) && Objects.equals(getCategories(), video1.getCategories()) && Objects.equals(getGenres(), video1.getGenres()) && Objects.equals(getCastMembers(), video1.getCastMembers()) && Objects.equals(getDomainEvents(), video1.getDomainEvents());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getLaunchedAt(), getDuration(), getRating(), getOpened(), getPublished(), getCreatedAt(), getUpdatedAt(), getBanner(), getThumbnail(), getThumbnailHalf(), getTrailer(), getVideo(), getCategories(), getGenres(), getCastMembers(), getDomainEvents());
    }
}
