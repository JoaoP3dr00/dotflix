package com.dotflix.application.video.dto;

public record GetMediaDTO(String videoId, String mediaType) {
    public static GetMediaDTO with(final String anId, final String aType) {
        return new GetMediaDTO(anId, aType);
    }
}
