package com.dotflix.application.category.exceptions;

public class CategoryNotFoundException extends Exception {
    public CategoryNotFoundException(final String msg){
        super(msg);
        System.out.println(getMessage());
    }
}
