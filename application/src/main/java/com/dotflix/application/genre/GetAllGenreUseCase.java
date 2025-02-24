package com.dotflix.application.genre;

import com.dotflix.application.UseCase;
import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.genre.Genre;
import com.dotflix.domain.genre.GenreGateway;
import java.util.Objects;

public class GetAllGenreUseCase extends UseCase<SearchQuery, Pagination<Genre>> {
    private final GenreGateway genreGateway;

    public GetAllGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<Genre> execute(final SearchQuery aQuery) {
        return this.genreGateway.findAll(aQuery);
    }
}
