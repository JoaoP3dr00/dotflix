package com.dotflix.domain.category;

import com.dotflix.domain.AgregateRoot;
import com.dotflix.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.UUID;

public class Category extends AgregateRoot<CategoryID> {
    private String name;
    private String description;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID id, final String name, final String description, final boolean isActive, final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        super(id);
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    /**
     * Factory Method that
     * @param name
     * @param description
     * @param active
     * @return
     */
    public static Category newCategory(final String name, final String description, final boolean active){
        final CategoryID id = CategoryID.unique();
        final var deletedAt = active ? null : Instant.now();
        return new Category(id, name, description, active, Instant.now(), Instant.now(), deletedAt);
    }

    public CategoryID getId() {
        return super.getId();
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

    @Override
    public void validate(final ValidationHandler handler){
        new CategoryValidator(this, handler).validate();
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

    public Category update(final String name, final String description, final boolean isActive){
        setName(name);
        setDescription(description);
        if(isActive)
            activate();
        else
            deactivate();

        this.updatedAt = Instant.now();
        return this;
    }
}