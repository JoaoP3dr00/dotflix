package com.dotflix.application.video;

import com.dotflix.application.UseCase;
import com.dotflix.application.video.dto.UploadMediaDTO;
import com.dotflix.application.video.dto.UploadMediaOutputDTO;
import com.dotflix.application.video.exceptions.VideoNotFoundException;
import com.dotflix.domain.video.*;
import java.util.Objects;

public class UploadMediaUseCase extends UseCase<UploadMediaDTO, UploadMediaOutputDTO> {
    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public UploadMediaUseCase(final MediaResourceGateway mediaResourceGateway, final VideoGateway videoGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public UploadMediaOutputDTO execute(final UploadMediaDTO aCmd) throws Exception {
        final String anId = aCmd.videoId();
        final VideoResource aResource = aCmd.videoResource();

        final var aVideo = this.videoGateway.findById(anId).orElseThrow(() -> new VideoNotFoundException("Genre with ID " + anId + " was not found"));

        switch (aResource.type()) {
            case VIDEO -> aVideo.updateVideoMedia(mediaResourceGateway.storeAudioVideo(anId, aResource));
            case TRAILER -> aVideo.updateTrailerMedia(mediaResourceGateway.storeAudioVideo(anId, aResource));
            case BANNER -> aVideo.updateBannerMedia(mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL -> aVideo.updateThumbnailMedia(mediaResourceGateway.storeImage(anId, aResource));
            case THUMBNAIL_HALF -> aVideo.updateThumbnailHalfMedia(mediaResourceGateway.storeImage(anId, aResource));
        }

        return UploadMediaOutputDTO.with(videoGateway.update(aVideo), aResource.type());
    }
}
