package com.dotflix.domain.video;

import com.dotflix.domain.events.DomainEvent;
import java.time.Instant;

public record VideoMediaCreated(String resourceId, String filePath, Instant occurredOn) implements DomainEvent {
    public VideoMediaCreated(final String resourceId, final String filePath) {
        this(resourceId, filePath, Instant.now());
    }
}
