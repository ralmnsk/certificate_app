package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

public abstract class IdentifiableDto<T> extends RepresentationModel<IdentifiableDto<T>> implements Serializable {
    private static final long serialVersionUID = 1L;
    private T id;
    @JsonIgnore
    private boolean deleted;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
