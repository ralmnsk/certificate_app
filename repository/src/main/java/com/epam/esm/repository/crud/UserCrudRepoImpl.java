package com.epam.esm.repository.crud;

import com.epam.esm.model.Filter;
import com.epam.esm.model.User;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

import static com.epam.esm.repository.crud.Constants.*;

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
        String ql = assembleQlString(filter, SELECT);
        Query query = getEntityManager().createNativeQuery(ql, User.class);
        query.setParameter("name", PERCENT_START + filter.getUserName() + PERCENT_END);
        query.setParameter("surname", PERCENT_START + filter.getUserSurname() + PERCENT_END);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<User> users = query.getResultList();

        Query queryCount = getEntityManager().createNativeQuery(assembleQlString(filter, COUNT));
        queryCount.setParameter("name", PERCENT_START + filter.getUserName() + PERCENT_END);
        queryCount.setParameter("surname", PERCENT_START + filter.getUserSurname() + PERCENT_END);
        long countResult = Long.valueOf(queryCount.getSingleResult().toString());

        updateFilter(filter, pageSize, countResult);

        return users;
    }

    private String assembleQlString(Filter filter, String selecting) {
        String select = "select distinct * ";
        String count = "select count(*) from (";
        String ql = select + " from users where surname like :surname and name like :name ";

        ql = addSortToQueryString(filter, selecting, ql);
        if (selecting.equals(COUNT)) {
            ql = count + ql + ") c";

        }

        return ql;
    }
}
