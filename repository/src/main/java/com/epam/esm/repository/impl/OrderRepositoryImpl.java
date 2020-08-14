package com.epam.esm.repository.impl;

import com.epam.esm.model.Order;
import com.epam.esm.model.filter.OrderFilter;
import com.epam.esm.model.wrapper.OrderListWrapper;
import com.epam.esm.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderRepositoryImpl extends AbstractRepository<Order, Long> implements OrderRepository {
    private static final String PERCENT = "%";
    private static final String SELECT = "select";
    private static final String COUNT = "count";
    private static final String USER_SURNAME = "userSurname";
    private static final String USER_NAME = "userName";
    private static final String USER_ID = "userId";
    private static final String SELECT_SQL = "select distinct orders.id,orders.completed,orders.created,orders.deleted,orders.description,orders.total_cost  ";
    private static final String COUNT_SQL = "select distinct count(*) from(select distinct orders.id,orders.completed,orders.created,orders.deleted,orders.description,orders.total_cost ";
    private static final String FROM = "from orders orders join users users on orders.user_id = users.id where users.surname like :userSurname and users.name like :userName and users.deleted = false and orders.deleted = false ";
    private static final String AND_USERS = "and users.id = :userId";
    private static final String APPEND_C = ") c";
    private static final String ORDERS_BY_CERTIFICATE_ID = "select orders.id,orders.completed,orders.created,orders.deleted,orders.description,orders.total_cost,orders.user_id " +
            "from orders join order_certificate oc on orders.id = oc.order_id join users u on orders.user_id = u.id " +
            "join certificate c on oc.certificate_id = c.id where orders.deleted = false and u.deleted = false and c.deleted = false and c.id = :certificateId";
    private static final String ORDERS_BY_USER_ID = "select orders.id,orders.completed,orders.created,orders.deleted, orders.description,orders.total_cost " +
            "from orders " +
            "    join users u on orders.user_id = u.id " +
            "where u.deleted=false and orders.deleted=false and u.id = :userId";
    private QueryBuilder<OrderFilter> builder;

    public OrderRepositoryImpl(QueryBuilder<OrderFilter> builder) {
        super(Order.class);
        this.builder = builder;
    }

    @Override
    public Order getFirstByUserId(Long userId) {

        Query query = getEntityManager().createNativeQuery(ORDERS_BY_USER_ID, Order.class);
        query.setParameter(USER_ID, userId);
        List<Order> orders = query.getResultList();
        Optional<Order> order = orders.stream().filter(o -> o.getCertificates().isEmpty()).findFirst();
        return order.isPresent() ? order.get() : null;
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

    @Override
    public List<Order> getOrdersByCertificateId(Long certificateId) {
        Query query = getEntityManager().createNativeQuery(ORDERS_BY_CERTIFICATE_ID, Order.class);
        query.setParameter("certificateId", certificateId);
        return query.getResultList();
    }

    private List<Order> getList(OrderFilter filter) {
        String ql = assembleQlString(filter, SELECT).toString();
        Query query = getEntityManager().createNativeQuery(ql, Order.class);
        querySetParameters(filter, query);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);

        return query.getResultList();
    }

    private OrderFilter setCountResult(OrderFilter filter) {
        Query queryTotal = getEntityManager().createNativeQuery
                (assembleQlString(filter, COUNT).toString());
        querySetParameters(filter, queryTotal);

        long countResult = Long.parseLong(queryTotal.getSingleResult().toString());
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
