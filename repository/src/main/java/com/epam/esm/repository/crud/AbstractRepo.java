package com.epam.esm.repository.crud;

import com.epam.esm.model.Filter;
import com.epam.esm.model.Identifiable;
import com.epam.esm.repository.CrudRepository;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

import static com.epam.esm.repository.crud.Constants.COUNT;

@Getter
@Setter
abstract class AbstractRepo<T extends Identifiable, E> implements CrudRepository<T, E> {
    private Filter filter;

    @PersistenceContext
    private EntityManager entityManager;
    private Class<T> entity;

    public AbstractRepo(Class<T> entity) {
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

    public void updateFilter(Filter filter, int pageSize, long countResult) {
        filter.setTotalPages((countResult / pageSize));
        if (countResult % pageSize != 0) {
            filter.setTotalPages(((int) countResult / pageSize) + 1);
        }
        filter.setTotalElements(countResult);
        setFilter(filter);
    }

    public String addSortToQueryString(Filter filter, String selecting, String ql) {
        if (!selecting.equals(COUNT) &&filter.getSort() != null && !filter.getSort().getOrders().isEmpty()) {
            String str = filter
                    .getSort()
                    .getOrders()
                    .stream()
                    .map(o -> o.getParameter() + " " + o.getDirection()).reduce("", (a, b) -> " " + a + " " + b + ",");
            str = str.substring(0, str.length() - 1);
            ql = ql + " order by" + str ;
        }
        return ql;
    }

}
