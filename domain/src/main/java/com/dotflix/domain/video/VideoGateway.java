package com.dotflix.domain.video;

import com.dotflix.domain.Pagination;
import java.util.Optional;

public interface VideoGateway {

    Video create(Video aVideo);

    void deleteById(String anId);

    Optional<Video> findById(String anId);

    Video update(Video aVideo);

    Pagination<VideoPreview> findAll(VideoSearchQuery aQuery);

}
