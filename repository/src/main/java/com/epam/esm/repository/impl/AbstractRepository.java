package com.epam.esm.repository.impl;

import com.epam.esm.model.Identifiable;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.exception.NotFoundException;

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
        checkIsDeleted(t);
        return Optional.ofNullable(t);
    }

    @Override
    public Optional<T> get(E id) {
        T t = entityManager.find(entity, id);
        checkIsDeleted(t);
        return Optional.ofNullable(t);
    }


    @Override
    public Optional<T> update(T t) {
        T tBase = get((E) t.getId()).orElseThrow(() -> new NotFoundException("Repository: entity not found exception."));
        entityManager.merge(t);
        entityManager.flush();
        entityManager.refresh(t);

        return Optional.ofNullable(t);
    }

    @Override
    public boolean delete(E id) {
        T t = entityManager.find(entity, id);
        if (t != null) {
            t.setDeleted(true);
            return true;
        }
        return false;
    }

    private void checkIsDeleted(T t) {
        if (t.isDeleted()) {
            throw new NotFoundException("Repository: entity not found");
        }
    }

}
