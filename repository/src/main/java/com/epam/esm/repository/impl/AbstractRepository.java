package com.epam.esm.repository.impl;

import com.epam.esm.model.Identifiable;
import com.epam.esm.repository.CrudRepository;
import com.epam.esm.repository.exception.NotFoundException;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
@Slf4j
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
        if (t == null){
            log.warn("Entity not found, id:{}",id);
            throw new NotFoundException(this.getClass()+": entity not found, id:"+id+". ");
        }
        checkIsDeleted(t);
        return Optional.ofNullable(t);
    }


    @Override
    public Optional<T> update(T t) {
        Optional<T> optional = get((E) t.getId());
        if(!optional.isPresent()){
                throw new NotFoundException("Repository: entity not found exception.");
        }
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
            log.warn("Entity not found exception:" + t.toString());
            throw new NotFoundException(this.getClass()+": isDeleted :entity not found, id:"+t.getId());
        }
    }

}
