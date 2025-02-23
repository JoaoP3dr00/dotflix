package com.dotflix.application.genre;

import com.dotflix.application.UseCase;
import com.dotflix.application.genre.dto.CreateGenreDTO;
import com.dotflix.domain.category.CategoryGateway;
import com.dotflix.domain.genre.Genre;
import com.dotflix.domain.genre.GenreGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateGenreUseCase extends UseCase<CreateGenreDTO, Genre> {
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public CreateGenreUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Genre execute(final CreateGenreDTO createGenreDTO) throws Exception {
        final String aName = createGenreDTO.name();
        final boolean isActive = createGenreDTO.isActive();
        final List<String> categoriesIds = Objects.requireNonNull(createGenreDTO.categories(), "Categories ids can not be null or empty");

        final Genre genre = Genre.newGenre(aName, isActive);

        final List<String> foundedIds = categoryGateway.existsByIds(categoriesIds);

        if(foundedIds.size() != categoriesIds.size()) {
            final List<String> missingIds = new ArrayList<>(categoriesIds);
            missingIds.removeAll(foundedIds);

            throw new Exception("Some categories could not be found: " + missingIds);
        } else {
            genre.addCategories(foundedIds);

            return this.genreGateway.create(genre);
        }
    }
}
