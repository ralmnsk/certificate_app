package com.epam.esm.model;

/**
 * The type Identifiable.
 *
 * @param <T> the type parameter
 */
public abstract class Identifiable<T> {
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
