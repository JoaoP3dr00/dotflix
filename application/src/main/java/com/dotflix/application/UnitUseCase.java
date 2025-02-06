package com.dotflix.application;

/**
 * Only receive
 * @param <OUT>
 */
public abstract class UnitUseCase<OUT> {
    public abstract OUT execute();
}
