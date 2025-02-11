package com.dotflix.application;

/**
 * Receive and return
 * @param <IN>
 * @param <OUT>
 */
public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN in) throws Exception;
}