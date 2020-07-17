package com.epam.esm.model;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class Identifiable<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private T id;

    public T getId() {
        return id;
    }

    public void setId(T id) {
        this.id = id;
    }
}
