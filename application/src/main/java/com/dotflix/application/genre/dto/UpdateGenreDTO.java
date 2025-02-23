package com.dotflix.application.genre.dto;

import java.util.List;

public record UpdateGenreDTO(String id, String name, boolean isActive, List<String> categories) {
}
