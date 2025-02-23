package com.dotflix.application.genre.dto;

import java.util.List;

public record CreateGenreDTO(String name, boolean isActive, List<String> categories) {
//    public static CreateGenreDTO with(final String aName, final Boolean isActive, final List<String> categories) {
//        return new CreateGenreDTO(aName, isActive != null ? isActive : true, categories);
//    }
}