package com.dotflix.application;

import com.dotflix.domain.category.Category;

/**
 * Receive and return
 * @param <IN>
 * @param <OUT>
 */
public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN in);
}