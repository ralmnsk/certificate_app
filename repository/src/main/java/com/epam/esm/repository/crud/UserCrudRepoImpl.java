package com.epam.esm.repository.crud;

import com.epam.esm.model.Filter;
import com.epam.esm.model.User;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
@Getter
public class UserCrudRepoImpl extends AbstractRepo<User, Long> implements UserCrudRepository {
    public UserCrudRepoImpl() {
        super(User.class);
    }

//    @Override
//    public Optional<User> getUserIdByOrderId(Long orderId) {
//
//        Query query = getEntityManager().createQuery("select b from User b join b.orders u where u.id = :orderId", User.class);
//        query.setParameter("orderId", orderId);
//        User user = (User) query.getSingleResult();
//
//        return Optional.ofNullable(user);
//    }

    @Override
    public List<User> getAll(Filter filter) {
        String ql = assembleQlString(filter, "select");
        Query query = getEntityManager().createNativeQuery(ql, User.class);
        query.setParameter("name", '%' + filter.getUserName() + '%');
        query.setParameter("surname", '%' + filter.getUserSurname() + '%');
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<User> users = query.getResultList();

        Query queryCount = getEntityManager().createNativeQuery(assembleQlString(filter, "count"));
        queryCount.setParameter("name", '%' + filter.getUserName() + '%');
        queryCount.setParameter("surname", '%' + filter.getUserSurname() + '%');
        long countResult = Long.valueOf(queryCount.getSingleResult().toString());

        updateFilter(filter, pageSize, countResult);

        return users;
    }

    private String assembleQlString(Filter filter, String selecting) {
        String select = "select * ";
        String count = "select count(*) ";
        if (selecting.equals("count")) {
            select = count;

        }
        String ql = select + " from users where surname like :surname and name like :name ";

        ql = addSortToQueryString(filter, selecting, ql);

        return ql;
    }
}
