package com.epam.esm.service.dto;

import java.io.Serializable;

/**
 * The type Dto.
 * An abstract class DTO to extend.
 *
 * @param <T> the type parameter
 */
public abstract class Dto<T> implements Serializable {
    private T id;

    /**
     * Gets id.
     *
     * @return the id
     */
    public T getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(T id) {
        this.id = id;
    }
}
