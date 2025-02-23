package com.dotflix.domain.genre;

import java.time.Instant;
import java.util.*;

public class Genre {
    private final String id;
    private String name;
    private boolean active;
    private List<String> categories;    // Lista de id's de categorias, para não alterar diretamente categorias caso aconteça, evitando conflitos
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(final String id, final String aName, final boolean isActive, final List<String> categories, final Instant aCreatedAt, final Instant aUpdatedAt, final Instant aDeletedAt) throws Exception {
        this.id = id;
        this.name = Objects.requireNonNull(aName, "'name' should not be null");
        this.categories = categories;
        this.active = isActive;
        this.createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdatedAt, "'updatedAt' should not be null");
        this.deletedAt = aDeletedAt;
        validate();
    }

    /**
     * Factory method to create new genre
     * @param aName
     * @param isActive
     * @return
     */
    public static Genre newGenre(final String aName, final boolean isActive) throws Exception {
        Random r = new Random();
        final int id = r.nextInt(1000);

        final var deletedAt = isActive ? null : Instant.now();

        return new Genre(Integer.toString(id), aName, isActive, new ArrayList<>(), Instant.now(), Instant.now(), deletedAt);
    }

    /**
     * Construtor que retorna uma nova instância de genre com os mesmos atributos
     * @param anId
     * @param aName
     * @param isActive
     * @param categories
     * @param aCreatedAt
     * @param aUpdatedAt
     * @param aDeletedAt
     * @return
     */
    public static Genre with(final String anId, final String aName, final boolean isActive, final List<String> categories, final Instant aCreatedAt, final Instant aUpdatedAt, final Instant aDeletedAt) throws Exception {
        return new Genre(anId, aName, isActive, categories, aCreatedAt, aUpdatedAt, aDeletedAt);
    }

    public void validate() throws Exception {
        final String name = this.getName();

        if(name == null){
            throw new Exception("'name' should not be null");
        }

        if(name.isBlank()){
            throw new Exception("'name' should not be empty");
        }

        if(name.trim().length() > 255 || name.trim().length() < 3){
            throw new Exception("'name' should have between 3 and 255 characters");
        }
    }

    public Genre update(final String aName, final boolean isActive, final List<String> categories) throws Exception {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }

        this.name = aName;
        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
        this.updatedAt = Instant.now();
        validate();

        return this;
    }

    public Genre deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }
        this.active = false;
        this.updatedAt = Instant.now();

        return this;
    }

    public Genre activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public String getId(){
        return this.id;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public List<String> getCategories() {
        return Collections.unmodifiableList(categories);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public Genre addCategory(final String id) {
        if (id == null) {
            return this;
        }
        this.categories.add(id);
        this.updatedAt = Instant.now();
        return this;
    }

    public Genre addCategories(final List<String> categories) {
        if (categories == null || categories.isEmpty()) {
            return this;
        }
        this.categories.addAll(categories);
        this.updatedAt = Instant.now();
        return this;
    }

    public Genre removeCategory(final String id) {
        if (id == null) {
            return this;
        }
        this.categories.remove(id);
        this.updatedAt = Instant.now();
        return this;
    }
}