package com.dotflix.infrastructure;

import com.dotflix.application.UseCase;
import com.dotflix.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args){
        System.out.println("Hello World!");
        SpringApplication.run(WebServerConfig.class, args);
    }
}