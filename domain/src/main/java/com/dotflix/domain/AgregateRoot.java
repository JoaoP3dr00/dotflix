package com.dotflix.domain;

public abstract class AgregateRoot<ID extends Identifier> extends Entity<ID>{
    protected AgregateRoot(ID id){
        super(id);
    }
}
