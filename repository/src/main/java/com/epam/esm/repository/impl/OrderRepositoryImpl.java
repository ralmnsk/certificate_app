package com.epam.esm.repository.impl;

import com.epam.esm.model.Order;
import com.epam.esm.model.filter.OrderFilter;
import com.epam.esm.model.wrapper.OrderListWrapper;
import com.epam.esm.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends AbstractRepository<Order, Long> implements OrderRepository {
    private final static String PERCENT = "%";
    private final static String SELECT = "select";
    private final static String COUNT = "count";
    private final static String USER_SURNAME = "userSurname";
    private final static String USER_NAME = "userName";
    private final static String USER_ID = "userId";
    private final static String SELECT_SQL = "select distinct orders.id,orders.completed,orders.created,orders.deleted,orders.description,orders.total_cost  ";
    private final static String COUNT_SQL = "select distinct count(*) from(select distinct orders.id,orders.completed,orders.created,orders.deleted,orders.description,orders.total_cost ";
    private final static String FROM = "from orders orders join users users on orders.user_id = users.id where users.surname like :userSurname and users.name like :userName and users.deleted = false and orders.deleted = false ";
    private final static String AND_USERS = "and users.id = :userId";
    private final static String APPEND_C = ") c";
    private QueryBuilder<OrderFilter> builder;

    public OrderRepositoryImpl(QueryBuilder<OrderFilter> builder) {
        super(Order.class);
        this.builder = builder;
    }


    @Override
    public OrderListWrapper getAll(OrderFilter filter) {
        List<Order> orders = getList(filter);
        filter = setCountResult(filter);

        OrderListWrapper listWrapper = new OrderListWrapper();
        listWrapper.setList(orders);
        listWrapper.setFilter(filter);

        return listWrapper;

    }

    private List<Order> getList(OrderFilter filter) {
        String ql = assembleQlString(filter, SELECT).toString();
        Query query = getEntityManager().createNativeQuery(ql, Order.class);
        querySetParameters(filter, query);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Order> orders = query.getResultList();

        return orders;
    }

    private OrderFilter setCountResult(OrderFilter filter) {
        Query queryTotal = getEntityManager().createNativeQuery
                (assembleQlString(filter, COUNT).toString());
        querySetParameters(filter, queryTotal);

        long countResult = Long.valueOf(queryTotal.getSingleResult().toString());
        int i = (int) countResult;
        int pageSize = filter.getSize();
        filter = builder.updateFilter(filter, pageSize, countResult);

        return filter;
    }

    private void querySetParameters(OrderFilter filter, Query query) {
        query.setParameter(USER_SURNAME, PERCENT + filter.getUserSurname() + PERCENT);
        query.setParameter(USER_NAME, PERCENT + filter.getUserName() + PERCENT);
        if (filter.getUserId() != null && filter.getUserId() > 0) {
            query.setParameter(USER_ID, filter.getUserId());
        }
    }

    private StringBuilder assembleQlString(OrderFilter filter, String selecting) {
        String select = SELECT_SQL;

        if (selecting.equals(COUNT)) {
            select = COUNT_SQL;
        }
        StringBuilder ql = new StringBuilder(select).append(FROM);
        if (filter.getUserId() != null && filter.getUserId() > 0) {
            ql.append(AND_USERS);
        }

        ql = builder.addSortToQueryString(filter, selecting, ql);

        if (selecting.equals(COUNT)) {
            ql.append(APPEND_C);
        }

        return ql;
    }


}
