package com.dotflix.infrastructure.genre.persistence;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "genres_categories")
public class GenreCategoryEntity {
    @EmbeddedId
    private GenreCategoryID id;

    @ManyToOne
    @MapsId("genreId")
    private GenreEntity genre;

    public GenreCategoryEntity() {
    }

    private GenreCategoryEntity(final GenreEntity aGenre, final String aCategoryId) {
        this.id = GenreCategoryID.from(aGenre.getId(), aCategoryId);
        this.genre = aGenre;
    }

    public static GenreCategoryEntity from(final GenreEntity aGenre, final String aCategoryId) {
        return new GenreCategoryEntity(aGenre, aCategoryId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreCategoryEntity that = (GenreCategoryEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public GenreCategoryID getId() {
        return id;
    }

    public GenreCategoryEntity setId(GenreCategoryID id) {
        this.id = id;
        return this;
    }

    public GenreEntity getGenre() {
        return genre;
    }

    public GenreCategoryEntity setGenre(GenreEntity genre) {
        this.genre = genre;
        return this;
    }
}