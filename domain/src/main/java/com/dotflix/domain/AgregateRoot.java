package com.dotflix.domain;

public class AgregateRoot<ID extends Identifier> extends Entity<ID>{
    protected AgregateRoot(ID id){
        super(id);
    }
}
