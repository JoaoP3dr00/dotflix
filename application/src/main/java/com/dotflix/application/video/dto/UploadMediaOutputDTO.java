package com.dotflix.application.video.dto;

import com.dotflix.domain.video.Video;
import com.dotflix.domain.video.VideoMediaType;

public record UploadMediaOutputDTO(String videoId, VideoMediaType mediaType) {
    public static UploadMediaOutputDTO with(final Video aVideo, final VideoMediaType aType) {
        return new UploadMediaOutputDTO(aVideo.getId(), aType);
    }
}