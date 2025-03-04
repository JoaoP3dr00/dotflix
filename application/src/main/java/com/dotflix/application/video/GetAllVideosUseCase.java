package com.dotflix.application.video;

import com.dotflix.application.UseCase;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.video.VideoGateway;
import com.dotflix.domain.video.VideoPreview;
import com.dotflix.domain.video.VideoSearchQuery;

import java.util.Objects;

public class GetAllVideosUseCase extends UseCase<VideoSearchQuery, Pagination<VideoPreview>> {
    private final VideoGateway videoGateway;

    public GetAllVideosUseCase(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoPreview> execute(final VideoSearchQuery aQuery) {
        return this.videoGateway.findAll(aQuery);
    }
}
