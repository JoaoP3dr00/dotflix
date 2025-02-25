package com.dotflix.application.castmember.exceptions;

public class CastMemberNotFoundException extends Exception {
    public CastMemberNotFoundException(final String msg){
        super(msg);
        System.out.println(getMessage());
    }
}
