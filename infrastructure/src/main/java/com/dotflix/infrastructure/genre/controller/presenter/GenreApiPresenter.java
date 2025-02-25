package com.dotflix.infrastructure.genre.controller.presenter;

import com.dotflix.domain.genre.Genre;
import com.dotflix.infrastructure.genre.controller.dto.GenreGetAllResponse;
import com.dotflix.infrastructure.genre.controller.dto.GenreResponse;

public interface GenreApiPresenter {
    static GenreResponse present(final Genre output) {
        return new GenreResponse(
                output.getId(),
                output.getName(),
                output.getCategories(),
                output.isActive(),
                output.getCreatedAt(),
                output.getUpdatedAt(),
                output.getDeletedAt()
        );
    }

    static GenreGetAllResponse presentGetAll(final Genre output) {
        return new GenreGetAllResponse(
                output.getId(),
                output.getName(),
                output.isActive(),
                output.getCreatedAt(),
                output.getDeletedAt()
        );
    }
}