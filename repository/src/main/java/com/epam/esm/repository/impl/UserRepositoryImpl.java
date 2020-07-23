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
    private final static String PERCENT = "%";
    private final static String SELECT = "select";
    private final static String COUNT = "count";
    private final static String SELECT_SQL = "select * from users where users.login = :login and users.deleted = false ";
    private final static String LOGIN = "login";
    private final static String NAME = "name";
    private final static String SURNAME = "surname";
    private final static String SELECT_BY_ORDER_ID = "select users.id,users.surname,users.name,users.login, users.password, users.deleted, users.role from users join orders o on users.id = o.user_id where o.id = :orderId";
    private final static String ORDER_ID = "orderId";
    private final static String SELECT_DISTINCT = "select distinct * ";
    private final static String SELECT_COUNT = "select count(*) from (";
    private final static String FROM_USERS = " from users where surname like :surname and name like :name and users.deleted = false ";
    private final static String APPEND_C = ") c";
    private QueryBuilder<UserFilter> builder;

    public UserRepositoryImpl(QueryBuilder<UserFilter> builder) {
        super(User.class);
        this.builder = builder;
    }

    @Override
    public User findByLogin(String login) {
        Query query = getEntityManager().createNativeQuery(SELECT_SQL, User.class);
        query.setParameter(LOGIN, login);
        User user = (User) query.getSingleResult();

        return user;
    }

    @Override
    public UserListWrapper getAll(UserFilter filter) {
        String ql = assembleQlString(filter, SELECT).toString();
        Query query = getEntityManager().createNativeQuery(ql, User.class);
        query.setParameter(NAME, PERCENT + filter.getUserName() + PERCENT);
        query.setParameter(SURNAME, PERCENT + filter.getUserSurname() + PERCENT);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<User> users = query.getResultList();
//        users = users.stream().filter(user -> !user.getDeleted()).collect(toList());

        Query queryCount = getEntityManager().createNativeQuery(assembleQlString(filter, COUNT).toString());
        queryCount.setParameter(NAME, PERCENT + filter.getUserName() + PERCENT);
        queryCount.setParameter(SURNAME, PERCENT + filter.getUserSurname() + PERCENT);
        long countResult = Long.valueOf(queryCount.getSingleResult().toString());

        filter = builder.updateFilter(filter, pageSize, countResult);
        UserListWrapper listWrapper = new UserListWrapper();
        listWrapper.setList(users);
        listWrapper.setFilter(filter);

        return listWrapper;
    }

    @Override
    public Optional<User> getUserByOrderId(Long orderId) {
        Query query = getEntityManager().createNativeQuery(SELECT_BY_ORDER_ID, User.class);
        query.setParameter(ORDER_ID, orderId);
        User user = (User) query.getSingleResult();
//        if (user.getDeleted()) {
//            return Optional.empty();
//        }

        return Optional.ofNullable(user);
    }

    private StringBuilder assembleQlString(UserFilter filter, String selecting) {
        String select = SELECT_DISTINCT;
        String count = SELECT_COUNT;
        StringBuilder ql = new StringBuilder(select).append(FROM_USERS);

        ql = builder.addSortToQueryString(filter, selecting, ql);
        if (selecting.equals(COUNT)) {
            ql = new StringBuilder(count).append(ql).append(APPEND_C);

        }

        return ql;
    }
}
