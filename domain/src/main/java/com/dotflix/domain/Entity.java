package com.dotflix.domain;

import com.dotflix.domain.validation.ValidationHandler;

import java.util.Objects;

/**
 * com.dotflix.domain.Entity class that only allow value object identifiers from any type of entity
 * @param <ID>
 */
public abstract class Entity<ID extends Identifier> {
    protected final ID id;


    protected Entity(final ID id){
        Objects.requireNonNull(id, "'id' should not be null");
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    public abstract void validate(ValidationHandler handler);

    @Override
    public boolean equals(final Object o) {
        if(this == o) return true;  // Check 'o' object reference comparing to 'this' object reference
        if (o == null || getClass() != o.getClass()) return false;  // Check if 'o' is null and classes equality
        final Entity<?> entity = (Entity<?>) o;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
