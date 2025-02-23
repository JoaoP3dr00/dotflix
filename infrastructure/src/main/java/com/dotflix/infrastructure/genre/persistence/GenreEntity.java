package com.dotflix.infrastructure.genre.persistence;

import com.dotflix.domain.genre.Genre;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "Genre")
@Table(name = "genres")
public class GenreEntity {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "genre", cascade = CascadeType.ALL, fetch = FetchType.EAGER , orphanRemoval = true)
    private Set<GenreCategoryEntity> categories;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(9)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(9)")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "TIMESTAMP(9)")
    private Instant deletedAt;

    public GenreEntity() {
    }

    private GenreEntity(final String anId, final String aName, final boolean isActive, final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        this.id = anId;
        this.name = aName;
        this.active = isActive;
        this.categories = new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    private void addCategory(final String categoryId) {
        this.categories.add(GenreCategoryEntity.from(this, categoryId));
    }

    public static GenreEntity fromDomain(final Genre aGenre) {
        final var anEntity = new GenreEntity(aGenre.getId(), aGenre.getName(), aGenre.isActive(), aGenre.getCreatedAt(), aGenre.getUpdatedAt(), aGenre.getDeletedAt());

        aGenre.getCategories().forEach(anEntity::addCategory);

        return anEntity;
    }

    public Genre toDomain() {
        try {
            return Genre.with(
                    getId(),
                    getName(),
                    isActive(),
                    getCategoryIDs(),
                    getCreatedAt(),
                    getUpdatedAt(),
                    getDeletedAt()
            );
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    private void removeCategory(final String categoryId) {
        this.categories.remove(GenreCategoryEntity.from(this, categoryId));
    }

    public String getId() {
        return id;
    }

    public GenreEntity setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public GenreEntity setName(String name) {
        this.name = name;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public GenreEntity setActive(boolean active) {
        this.active = active;
        return this;
    }

    public List<String> getCategoryIDs() {
        return getCategories().stream()
                .map(it -> it.getId().getCategoryId())
                .toList();
    }
    public Set<GenreCategoryEntity> getCategories() {
        return categories;
    }

    public GenreEntity setCategories(Set<GenreCategoryEntity> categories) {
        this.categories = categories;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public GenreEntity setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public GenreEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public GenreEntity setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
        return this;
    }
}