package com.epam.esm.service.dto;

import com.epam.esm.service.view.Profile;
import com.fasterxml.jackson.annotation.JsonView;

import java.io.Serializable;

/**
 * The type Dto.
 *
 * @param <T> the type parameter
 */
public abstract class Dto<T> implements Serializable {
    @JsonView(Profile.PublicView.class)
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
