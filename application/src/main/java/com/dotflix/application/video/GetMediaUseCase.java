package com.dotflix.application.video;

import com.dotflix.application.UseCase;
import com.dotflix.application.video.dto.GetMediaDTO;
import com.dotflix.application.video.exceptions.MediaNotFoundException;
import com.dotflix.application.video.exceptions.ResourceNotFoundException;
import com.dotflix.domain.video.MediaResourceGateway;
import com.dotflix.domain.video.Resource;
import com.dotflix.domain.video.VideoMediaType;
import java.util.Objects;

public class GetMediaUseCase extends UseCase<GetMediaDTO, Resource> {
    private final MediaResourceGateway mediaResourceGateway;

    public GetMediaUseCase(final MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public Resource execute(final GetMediaDTO aCmd) throws Exception {
        final String anId = aCmd.videoId();
        final VideoMediaType aType = VideoMediaType.of(aCmd.mediaType()).orElseThrow(() -> new MediaNotFoundException("Media type" + aCmd.mediaType() + " doesn't exists"));

        return this.mediaResourceGateway.getResource(anId, aType).orElseThrow(() -> new ResourceNotFoundException("A resource with ID " + aCmd.videoId() + " and type " + aCmd.mediaType() + " was not found"));
    }
}
