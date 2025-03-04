package com.dotflix.application.video;

import com.dotflix.application.UseCase;
import com.dotflix.application.video.exceptions.VideoNotFoundException;
import com.dotflix.domain.video.Video;
import com.dotflix.domain.video.VideoGateway;
import java.util.Objects;

public class GetVideoByIdUseCase extends UseCase<String, Video> {
    private final VideoGateway videoGateway;

    public GetVideoByIdUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Video execute(final String anIn) throws Exception {
        final String aVideoId = anIn;

        return this.videoGateway.findById(aVideoId).orElseThrow(() -> new VideoNotFoundException("Genre with ID " + aVideoId + " was not found"));
    }
}
