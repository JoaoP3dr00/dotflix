package com.dotflix.application.video.exceptions;

public class MediaNotFoundException extends Exception {
    public MediaNotFoundException(final String msg){
        super(msg);
        System.out.println(getMessage());
    }
}