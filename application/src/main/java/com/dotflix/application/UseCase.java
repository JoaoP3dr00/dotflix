package com.dotflix.application;

import com.dotflix.domain.category.Category;

import java.lang.reflect.Modifier;

/**
 * Receive and return
 * @param <IN>
 * @param <OUT>
 */
public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN in);
}