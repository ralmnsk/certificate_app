package com.epam.esm.service.dto;

import com.epam.esm.service.view.Profile;
import com.fasterxml.jackson.annotation.JsonView;

public abstract class Dto<T> {
    @JsonView(Profile.PublicView.class)
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
