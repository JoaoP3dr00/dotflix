package com.dotflix.infrastructure;

import com.dotflix.application.UseCase;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello World!");
        System.out.println(new UseCase().execute());
    }
}