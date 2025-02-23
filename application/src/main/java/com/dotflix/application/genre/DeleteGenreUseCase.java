package com.dotflix.application.genre;

import com.dotflix.application.UseCase;
import com.dotflix.domain.genre.GenreGateway;

import java.util.Objects;

public class DeleteGenreUseCase extends UseCase<String, String> {
    private final GenreGateway genreGateway;

    public DeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public String execute(final String id) {
        this.genreGateway.deleteById(id);

        return "Genre " + id + " deleted";
    }
}