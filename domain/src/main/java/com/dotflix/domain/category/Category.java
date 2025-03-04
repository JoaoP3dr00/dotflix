package com.dotflix.domain.category;

import java.util.Objects;
import java.util.Random;
import java.time.Instant;

public class Category implements Cloneable {
    private final String id;
    private String name;
    private String description;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final String id, final String name, final String description, final boolean isActive, final Instant createdAt, final Instant updatedAt, final Instant deletedAt) throws Exception {
        this.id = Objects.requireNonNull(id, "'id' should not be null");
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = Objects.requireNonNull(createdAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "'updatedAt' should not be null");
        this.deletedAt = deletedAt;
        validate();
    }

    /**
     * Factory Method to create new category
     * @param name
     * @param description
     * @param active
     * @return
     */
    public static Category newCategory(final String name, final String description, final boolean active) {
        Random r = new Random();
        final int id = r.nextInt(1000);

        final var deletedAt = active ? null : Instant.now();

        try {
            return new Category(Integer.toString(id), name, description, active, Instant.now(), Instant.now(), deletedAt);
        } catch (Exception e){
            System.out.println("Error: " + e);
            return null;
        }
    }

    /**
     * Construtor que retorna uma nova instância de category com os mesmos atributos do objeto de origem
     * @param id
     * @param name
     * @param description
     * @param isActive
     * @param createdAt
     * @param updatedAt
     * @param deletedAt
     * @return
     */
    public static Category with(final String id, final String name, final String description, final boolean isActive, final Instant createdAt, final Instant updatedAt, final Instant deletedAt)throws Exception{
        return new Category(id, name, description, isActive, createdAt, updatedAt, deletedAt);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean getIsActive() {
        return isActive;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsActive(boolean active) {
        this.isActive = active;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
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

    public Category deactivate(){
        if(getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }

        this.isActive = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category activate(){
        this.deletedAt = null;
        this.isActive = true;
        this.updatedAt = Instant.now();

        return this;
    }

    public Category update(final String name, final String description, final boolean isActive) {
        setName(name);
        setDescription(description);
        if(isActive)
            activate();
        else
            deactivate();

        this.updatedAt = Instant.now();

        try {
            validate();
        } catch (Exception e){
            System.out.println("Error: " + e);
            return null;
        }

        return this;
    }


    @Override
    public Category clone() {
        try {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Category category)) return false;
        return isActive == category.isActive && Objects.equals(getId(), category.getId()) && Objects.equals(getName(), category.getName()) && Objects.equals(getDescription(), category.getDescription()) && Objects.equals(getCreatedAt(), category.getCreatedAt()) && Objects.equals(getUpdatedAt(), category.getUpdatedAt()) && Objects.equals(getDeletedAt(), category.getDeletedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), isActive, getCreatedAt(), getUpdatedAt(), getDeletedAt());
    }
}