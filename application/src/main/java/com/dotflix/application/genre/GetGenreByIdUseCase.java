package com.dotflix.application.genre;

import com.dotflix.application.UseCase;
import com.dotflix.application.genre.excpetions.GenreNotFoundException;
import com.dotflix.domain.genre.Genre;
import com.dotflix.domain.genre.GenreGateway;
import java.util.Objects;

public class GetGenreByIdUseCase extends UseCase<String, Genre> {
    private final GenreGateway genreGateway;

    public GetGenreByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Genre execute(final String id) throws GenreNotFoundException {
        return this.genreGateway.findById(id).orElseThrow(() -> new GenreNotFoundException("Genre with ID " + id + " was not found"));
    }
}
