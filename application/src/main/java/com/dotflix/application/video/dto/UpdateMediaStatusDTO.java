package com.dotflix.application.video.dto;

import com.dotflix.domain.video.MediaStatus;

public record UpdateMediaStatusDTO(
        MediaStatus status,
        String videoId,
        String resourceId,
        String folder,
        String filename
) {
    public static UpdateMediaStatusDTO with(
            final MediaStatus status,
            final String videoId,
            final String resourceId,
            final String folder,
            final String filename
    ) {
        return new UpdateMediaStatusDTO(status, videoId, resourceId, folder, filename);
    }
}
