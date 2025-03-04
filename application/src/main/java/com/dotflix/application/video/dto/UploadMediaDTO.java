package com.dotflix.application.video.dto;

import com.dotflix.domain.video.VideoResource;

public record UploadMediaDTO(String videoId, VideoResource videoResource) {
    public static UploadMediaDTO with(final String anId, final VideoResource aResource) {
        return new UploadMediaDTO(anId, aResource);
    }
}
