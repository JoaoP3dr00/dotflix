package com.dotflix.application.video.exceptions;

public class ResourceNotFoundException extends Exception {
    public ResourceNotFoundException(final String msg){
        super(msg);
        System.out.println(getMessage());
    }
}