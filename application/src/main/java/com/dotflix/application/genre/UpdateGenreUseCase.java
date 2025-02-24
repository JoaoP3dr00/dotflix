package com.dotflix.application.genre;

import com.dotflix.application.UseCase;
import com.dotflix.application.genre.dto.UpdateGenreDTO;
import com.dotflix.application.genre.excpetions.GenreNotFoundException;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.genre.Genre;
import com.dotflix.domain.genre.GenreGateway;
import java.util.List;
import java.util.Objects;

public class UpdateGenreUseCase extends UseCase<UpdateGenreDTO, Genre> {
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public UpdateGenreUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Genre execute(final UpdateGenreDTO updateGenreDTO) throws Exception {
        final String id = updateGenreDTO.id();
        final String name = updateGenreDTO.name();
        final boolean isActive = updateGenreDTO.isActive();
        final var categories = Objects.requireNonNull(updateGenreDTO.categories(), "Categories ids can not be null or empty");

        final Genre genre = this.genreGateway.findById(id).orElseThrow(() -> new GenreNotFoundException("O gênero " + id + " não foi encontrado."));

        final List<String> ids = Objects.requireNonNull(categoryGateway.existsByIds(categories), "Some categories could not be found");

        genre.update(name, isActive, ids);

        return this.genreGateway.update(genre);
    }
}