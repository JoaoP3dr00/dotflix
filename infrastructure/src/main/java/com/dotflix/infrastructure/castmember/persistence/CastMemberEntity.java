package com.dotflix.infrastructure.castmember.persistence;

import com.dotflix.domain.castmember.CastMember;
import com.dotflix.domain.castmember.CastMemberType;
import jakarta.persistence.*;

import java.time.Instant;

@Entity(name = "CastMember")
@Table(name = "cast_members")
public class CastMemberEntity {
    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CastMemberType type;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(9)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(9)")
    private Instant updatedAt;

    public CastMemberEntity() {
    }

    public CastMemberEntity(final String id, final String name, final CastMemberType type, final Instant createdAt, final Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CastMemberEntity fromDomain(final CastMember aMember) {
        return new CastMemberEntity(
                aMember.getId(),
                aMember.getName(),
                aMember.getType(),
                aMember.getCreatedAt(),
                aMember.getUpdatedAt()
        );
    }

    public CastMember toDomain() {
        try {
            return CastMember.with(
                    getId(),
                    getName(),
                    getType(),
                    getCreatedAt(),
                    getUpdatedAt()
            );
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public String getId() {
        return id;
    }

    public CastMemberEntity setId(final String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CastMemberEntity setName(final String name) {
        this.name = name;
        return this;
    }

    public CastMemberType getType() {
        return type;
    }

    public CastMemberEntity setType(final CastMemberType type) {
        this.type = type;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public CastMemberEntity setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public CastMemberEntity setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}