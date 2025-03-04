package com.dotflix.domain.video;

import java.util.Optional;

public interface MediaResourceGateway {

    AudioVideoMedia storeAudioVideo(String anId, VideoResource aResource);

    ImageMedia storeImage(String anId, VideoResource aResource);

    Optional<Resource> getResource(String anId, VideoMediaType type);

    void clearResources(String anId);
}
