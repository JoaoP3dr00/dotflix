package com.dotflix.infrastructure.category.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCategoryRequest(
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active
) {
}