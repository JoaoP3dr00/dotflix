package com.dotflix.infrastructure.category;

import com.dotflix.domain.category.Category;
import com.dotflix.domain.lixo.CategoryID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Table(name = "category")
@Entity
public class CategoryEntity {
    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 4000, nullable = true)
    private String description;

    @Column(name = "active", nullable = false)
    private boolean isActive;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(9)") // Campo de data com precisão de 6 casas após os segundos
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(9)")
    private Instant updatedAt;

    @Column(name = "deleted_at", nullable = true, columnDefinition = "TIMESTAMP(9)")
    private Instant deletedAt;

    public CategoryEntity(){}

    private CategoryEntity(final String id, final String name, final String description, final boolean isActive, final Instant createdAt, final Instant updatedAt, final    Instant deletedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static CategoryEntity fromDomain(final Category category){
        return new CategoryEntity(category.getId(),
                category.getName(), category.getDescription(),
                category.getIsActive(), category.getCreatedAt(),
                category.getUpdatedAt(), category.getDeletedAt()
        );
    }

    public Category toDomain(){
        return Category.with(getId(),getName(),getDescription(),getIsActive(),getCreatedAt(),getUpdatedAt(),getDeletedAt());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
