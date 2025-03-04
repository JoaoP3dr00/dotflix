package com.dotflix.application.video;

import com.dotflix.domain.video.MediaResourceGateway;
import com.dotflix.domain.video.VideoGateway;

import java.util.Objects;

public class DeleteVideoUseCase {
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DeleteVideoUseCase(final VideoGateway videoGateway, final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    public void execute(final String anIn) {
        this.videoGateway.deleteById(anIn);
        this.mediaResourceGateway.clearResources(anIn);
    }
}
