package com.epam.esm.repository.impl;

import com.epam.esm.model.Identifiable;
import com.epam.esm.repository.CrudRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public abstract class AbstractRepository<T extends Identifiable, E> implements CrudRepository<T, E> {


    @PersistenceContext
    private EntityManager entityManager;
    private Class<T> entity;

    public AbstractRepository(Class<T> entity) {
        this.entity = entity;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    @Override
    public Optional<T> save(T t) {
        entityManager.persist(t);
        entityManager.refresh(t);
        return Optional.ofNullable(entityManager.find(entity, t.getId()));
    }

    @Override
    public Optional<T> get(E id) {
        return Optional.ofNullable(entityManager.find(entity, id));
    }


    @Override
    public Optional<T> update(T t) {
        entityManager.merge(t);
        entityManager.flush();
        entityManager.refresh(t);
        return Optional.ofNullable(entityManager.find(entity, t.getId()));
    }

    @Override
    public boolean delete(E id) {
        T t = entityManager.find(entity, id);
        if (t != null) {
            entityManager.remove(t);
            return true;
        }
        return false;
    }


}
