package com.dotflix.domain.genre;

import com.dotflix.domain.Pagination;
import com.dotflix.domain.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface GenreGateway {

    Genre create(Genre aGenre);

    void deleteById(String id);

    Optional<Genre> findById(String id);

    Genre update(Genre aGenre);

    Pagination<Genre> findAll(SearchQuery aQuery);

    List<String> existsByIds(Iterable<String> ids);
}