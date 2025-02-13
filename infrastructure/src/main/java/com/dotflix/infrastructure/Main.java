package com.dotflix.infrastructure;

import com.dotflix.application.category.*;
import com.dotflix.infrastructure.configuration.WebServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication
public class Main {
    public static void main(String[] args){
        System.out.println("Hello World!");
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "");
        SpringApplication.run(WebServerConfig.class, args);
    }

//    @Bean
//    @DependsOnDatabaseInitialization
//    ApplicationRunner runner(@Autowired CreateCategoryUseCase createCategoryUseCase, @Autowired DeleteCategoryUseCase deleteCategoryUseCase, @Autowired UpdateCategoryUseCase updateCategoryUseCase, @Autowired GetCategoryByIdUseCase getCategoryByIdUseCase, @Autowired GetAllCategoriesUseCase getAllCategoriesUseCase){
//        return args -> {};
//    }
}