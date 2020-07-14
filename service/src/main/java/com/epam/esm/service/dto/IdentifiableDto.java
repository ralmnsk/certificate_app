package com.epam.esm.service.dto;

import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

public abstract class IdentifiableDto<T> extends RepresentationModel<IdentifiableDto<T>> implements Serializable {
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
