package com.dotflix.infrastructure.genre.persistence;

import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;
import com.dotflix.domain.genre.Genre;
import com.dotflix.domain.genre.GenreGateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class GenrePostgresqlGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    public GenrePostgresqlGateway(final GenreRepository genreRepository) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
    }

    @Override
    public Genre create(final Genre aGenre) {
        return this.genreRepository.save(GenreEntity.fromDomain(aGenre)).toDomain();
    }

    @Override
    public void deleteById(final String id) {
        if (this.genreRepository.existsById(id)) {
            this.genreRepository.deleteById(id);
        }
    }

    @Override
    public Optional<Genre> findById(final String id) {
        return this.genreRepository.findById(id)
                .map(GenreEntity::toDomain);
    }

    @Override
    public Genre update(final Genre aGenre) {
        return this.genreRepository.save(GenreEntity.fromDomain(aGenre)).toDomain();
    }

    @Override
    public List<String> existsByIds(final Iterable<String> genreIDS) {
        final var ids = StreamSupport.stream(genreIDS.spliterator(), false).toList();
        return this.genreRepository.existsByIds(ids).stream().toList();
    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final Specification<GenreEntity> specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map((final String str) -> {
                    final String convertLike = "%" + str + "%";

                    final Specification<GenreEntity> nameLike = (root, query, cb) ->
                            cb.like(
                              cb.upper(
                                      root.get("name")
                              ),
                              convertLike.toUpperCase()
                            );

                    return nameLike;
                })
                .orElse(null);

        final Page<GenreEntity> pageResult = this.genreRepository.findAll(Specification.where(specifications), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(GenreEntity::toDomain).toList()
        );
    }
}
