package com.dotflix.application.genre.excpetions;

public class GenreNotFoundException extends Exception {
    public GenreNotFoundException(final String msg){
        super(msg);
        System.out.println(getMessage());
    }
}
