package com.epam.esm.repository.impl;

import com.epam.esm.model.User;
import com.epam.esm.model.filter.UserFilter;
import com.epam.esm.model.wrapper.UserListWrapper;
import com.epam.esm.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Repository
public class UserRepositoryImpl extends AbstractRepository<User, Long> implements UserRepository {
    private final String PERCENT = "%";
    private final String SELECT = "select";
    private final String COUNT = "count";

    private QueryBuilder<UserFilter> builder;

    public UserRepositoryImpl(QueryBuilder<UserFilter> builder) {
        super(User.class);
        this.builder = builder;
    }

    @Override
    public User findByLogin(String login) {
        Query query = getEntityManager().createNativeQuery("select * from users where users.login = :login", User.class);
        query.setParameter("login", login);
        User user = (User) query.getSingleResult();
        if (user != null && user.getDeleted()) {
            return null;
        }

        return user;
    }

    @Override
    public UserListWrapper getAll(UserFilter filter) {
        String ql = assembleQlString(filter, SELECT);
        Query query = getEntityManager().createNativeQuery(ql, User.class);
        query.setParameter("name", PERCENT + filter.getUserName() + PERCENT);
        query.setParameter("surname", PERCENT + filter.getUserSurname() + PERCENT);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<User> users = query.getResultList();
        users = users.stream().filter(user -> !user.getDeleted()).collect(toList());

        Query queryCount = getEntityManager().createNativeQuery(assembleQlString(filter, COUNT));
        queryCount.setParameter("name", PERCENT + filter.getUserName() + PERCENT);
        queryCount.setParameter("surname", PERCENT + filter.getUserSurname() + PERCENT);
        long countResult = Long.valueOf(queryCount.getSingleResult().toString());

        filter = builder.updateFilter(filter, pageSize, countResult);
        UserListWrapper listWrapper = new UserListWrapper();
        listWrapper.setList(users);
        listWrapper.setFilter(filter);

        return listWrapper;
    }

    @Override
    public Optional<User> getUserByOrderId(Long orderId) {
        Query query = getEntityManager().createNativeQuery("select users.id,users.surname,users.name,users.login, users.password, users.deleted, users.role from users join orders o on users.id = o.user_id " +
                "where o.id = :orderId", User.class);
        query.setParameter("orderId", orderId);
        User user = (User) query.getSingleResult();
        if (user.getDeleted()) {
            return Optional.empty();
        }

        return Optional.ofNullable(user);
    }

    private String assembleQlString(UserFilter filter, String selecting) {
        String select = "select distinct * ";
        String count = "select count(*) from (";
        String ql = select + " from users where surname like :surname and name like :name and users.deleted = false ";

        ql = builder.addSortToQueryString(filter, selecting, ql);
        if (selecting.equals(COUNT)) {
            ql = count + ql + ") c";

        }

        return ql;
    }
}
