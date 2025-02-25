package com.dotflix.infrastructure.genre.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record GenreGetAllResponse(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
}