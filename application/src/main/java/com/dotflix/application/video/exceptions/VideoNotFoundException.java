package com.dotflix.application.video.exceptions;

public class VideoNotFoundException extends Exception {
    public VideoNotFoundException(final String msg){
        super(msg);
        System.out.println(getMessage());
    }
}