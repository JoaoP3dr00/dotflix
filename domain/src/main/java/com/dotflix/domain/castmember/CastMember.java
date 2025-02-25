package com.dotflix.domain.castmember;

import java.time.Instant;
import java.util.Objects;
import java.util.Random;

public class CastMember {
    private final String id;
    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;

    protected CastMember(final String id, final String name, final CastMemberType type, final Instant createdAt, final Instant updatedAt) throws Exception{
        this.id = Objects.requireNonNull(id, "'id' should not be null");
        this.name = name;
        this.type = type;
        this.createdAt = Objects.requireNonNull(createdAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "'updatedAt' should not be null");
        validate();
    }

    public static CastMember newMember(final String name, final CastMemberType type) throws Exception {
        Random r = new Random();
        final int id = r.nextInt(1000);
        final Instant now = Instant.now();

        return new CastMember(Integer.toString(id), name, type, now, now);
    }

    public static CastMember with(final String id, final String name, final CastMemberType type, final Instant createdAt, final Instant updatedAt) throws Exception {
        return new CastMember(id, name, type, createdAt, updatedAt);
    }

    public CastMember update(final String name, final CastMemberType type) throws Exception {
        setName(name);
        setType(type);
        setUpdatedAt(Instant.now());
        validate();
        return this;
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

        if(type == null) {
            throw new Exception("'type' should not be null");
        }
    }

    public String getId(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public CastMemberType getType() {
        return this.type;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(CastMemberType type) {
        this.type = type;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}