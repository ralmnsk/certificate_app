package com.epam.esm.repository.impl;

import com.epam.esm.model.Order;
import com.epam.esm.model.filter.OrderFilter;
import com.epam.esm.model.wrapper.OrderListWrapper;
import com.epam.esm.repository.OrderRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.repository.impl.Constants.*;

@Repository
public class OrderRepositoryImpl extends AbstractRepository<Order, Long> implements OrderRepository {
    private QueryBuilder<OrderFilter> builder;

    public OrderRepositoryImpl(QueryBuilder<OrderFilter> builder) {
        super(Order.class);
        this.builder = builder;
    }


    @Override
    public OrderListWrapper getAll(OrderFilter filter) {
        String ql = assembleQlString(filter, "select");
        Query query = getEntityManager().createNativeQuery(ql, Order.class);
        querySetParameters(filter, query);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Order> orders = query.getResultList();
        orders = orders.stream().filter(order -> !order.isDeleted()).collect(Collectors.toList());

        Query queryTotal = getEntityManager().createNativeQuery
                (assembleQlString(filter, "count"));
        querySetParameters(filter, queryTotal);

        long countResult = Long.valueOf(queryTotal.getSingleResult().toString());
        int i = (int) countResult;

        filter = builder.updateFilter(filter, pageSize, countResult);
        OrderListWrapper listWrapper = new OrderListWrapper();
        listWrapper.setList(orders);
        listWrapper.setFilter(filter);

        return listWrapper;

    }

    private void querySetParameters(OrderFilter filter, Query query) {
        query.setParameter("userSurname", PERCENT_START + filter.getUserSurname() + PERCENT_END);
        query.setParameter("userName", PERCENT_START + filter.getUserName() + PERCENT_END);
        if (filter.getUserId() != null && filter.getUserId() > 0) {
            query.setParameter("userId", filter.getUserId());
        }
    }

    private String assembleQlString(OrderFilter filter, String selecting) {
        String select = "select distinct orders.id,orders.completed,orders.created,orders.deleted,orders.description,orders.total_cost  ";
        String count = "select distinct count(*) from(select distinct orders.id,orders.completed,orders.created,orders.deleted,orders.description,orders.total_cost ";
        if (selecting.equals(COUNT)) {
            select = count;
        }
        String ql = select + "from orders orders join users users on orders.user_id = users.id " +
                "where users.surname like :userSurname and users.name like :userName and users.deleted = false and orders.deleted = false ";
        if (filter.getUserId() != null && filter.getUserId() > 0) {
            ql = ql + "and users.id = :userId";
        }

        ql = builder.addSortToQueryString(filter, selecting, ql);

        if (selecting.equals(COUNT)) {
            ql = ql + ") c";
        }

        return ql;
    }


}
