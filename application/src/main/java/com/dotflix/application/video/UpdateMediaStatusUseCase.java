package com.dotflix.application.video;

import com.dotflix.application.UseCase;
import com.dotflix.application.video.dto.UpdateMediaStatusDTO;
import com.dotflix.application.video.exceptions.VideoNotFoundException;
import com.dotflix.domain.video.*;
import java.util.Objects;

public class UpdateMediaStatusUseCase extends UseCase<UpdateMediaStatusDTO, String> {
    private final VideoGateway videoGateway;

    public UpdateMediaStatusUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public String execute(final UpdateMediaStatusDTO aCmd) throws Exception {
        final String anId = aCmd.videoId();
        final String aResourceId = aCmd.resourceId();
        final String folder = aCmd.folder();
        final String filename = aCmd.filename();

        final Video aVideo = this.videoGateway.findById(anId).orElseThrow(() -> new VideoNotFoundException("Genre with ID " + anId + " was not found"));

        final String encodedPath = "%s/%s".formatted(folder, filename);

        if (matches(aResourceId, aVideo.getVideo().orElse(null))) {
            updateVideo(VideoMediaType.VIDEO, aCmd.status(), aVideo, encodedPath);
            return "ok";
        } else if (matches(aResourceId, aVideo.getTrailer().orElse(null))) {
            updateVideo(VideoMediaType.TRAILER, aCmd.status(), aVideo, encodedPath);
            return "ok";
        }
        return "";
    }

    private void updateVideo(final VideoMediaType aType, final MediaStatus aStatus, final Video aVideo, final String encodedPath) {
        switch (aStatus) {
            case PENDING -> {
            }
            case PROCESSING -> aVideo.processing(aType);
            case COMPLETED -> aVideo.completed(aType, encodedPath);
        }

        this.videoGateway.update(aVideo);
    }

    private boolean matches(final String anId, final AudioVideoMedia aMedia) {
        if (aMedia == null) {
            return false;
        }

        return aMedia.id().equals(anId);
    }
}
