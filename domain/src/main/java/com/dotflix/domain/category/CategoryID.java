package com.dotflix.domain.category;

import com.dotflix.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {
    private final String value;

    private CategoryID(final String id){
        Objects.requireNonNull(id);
        this.value = id;
    }

    public static CategoryID unique(){
        return new CategoryID(UUID.randomUUID().toString().toLowerCase());
    }

    /**
     * Helper method for conversion
     * @param id
     * @return
     */
    public static CategoryID from(final String id){
        return new CategoryID(id);
    }

    public static CategoryID from(final UUID id){
        return new CategoryID(id.toString().toLowerCase());
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryID that = (CategoryID) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
