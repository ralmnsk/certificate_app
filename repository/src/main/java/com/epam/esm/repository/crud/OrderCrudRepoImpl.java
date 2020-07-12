package com.epam.esm.repository.crud;

import com.epam.esm.model.Filter;
import com.epam.esm.model.Order;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

import static com.epam.esm.repository.crud.Constants.*;

@Repository
@Getter
public class OrderCrudRepoImpl extends AbstractRepo<Order, Long> implements OrderCrudRepository {
    private QueryBuilder builder;

    public OrderCrudRepoImpl(QueryBuilder builder) {
        super(Order.class);
        this.builder = builder;
    }

//    @Override
//    public Page<Order> getAllByUserId(Long userId, Pageable pageable) {
//        Query query = getEntityManager().createNativeQuery("select o.id,o.completed,o.created,o.deleted,o.description,o.total_cost from orders o where o.user_id = :userId", Order.class);
//        query.setParameter("userId", userId);
//        int pageNumber = pageable.getPageNumber();
//        int pageSize = pageable.getPageSize();
//        query.setFirstResult((pageNumber) * pageSize);
//        query.setMaxResults(pageSize);
//        List<Order> orders = query.getResultList();
//
//        Query queryTotal = getEntityManager().createNativeQuery
//                ("select count(o.id) from orders o where o.id = :userId");
//        queryTotal.setParameter("userId", userId);
//        BigInteger countResult = (BigInteger) queryTotal.getSingleResult();
//        int i = countResult.intValue();
//
//        return new PageImpl<Order>(orders, pageable, i);
//    }
//
//
//    @Override
//    public void removeFromCertificateRelationByOrderId(Long orderId) {
//        Query query = getEntityManager().createNativeQuery("delete from order_certificate where order_id = :orderId");
//        query.setParameter("orderId", orderId);
//        query.executeUpdate();
//    }
//
//    @Override
//    public void addOrderToUser(Long userId, Long orderId) {
//        Query query = getEntityManager().createNativeQuery("update orders set user_id = :userId where id = :orderId");
//        query.setParameter("orderId", orderId);
//        query.setParameter("userId", userId);
//        query.executeUpdate();
//    }

    @Override
    public List<Order> getAll(Filter filter) {
        String ql = assembleQlString(filter, "select");
        Query query = getEntityManager().createNativeQuery(ql, Order.class);
        querySetParameters(filter, query);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Order> orders = query.getResultList();

        Query queryTotal = getEntityManager().createNativeQuery
                (assembleQlString(filter, "count"));
        querySetParameters(filter, queryTotal);

        long countResult = Long.valueOf(queryTotal.getSingleResult().toString());
        int i = (int) countResult;

        filter = builder.updateFilter(filter, pageSize, countResult);
        setFilter(filter);

        return orders;
    }

    private void querySetParameters(Filter filter, Query query) {
        query.setParameter("userSurname", PERCENT_START + filter.getUserSurname() + PERCENT_END);
        query.setParameter("userName", PERCENT_START + filter.getUserName() + PERCENT_END);
        if (filter.getUserId() != null && filter.getUserId() > 0) {
            query.setParameter("userId", filter.getUserId());
        }
    }

    private String assembleQlString(Filter filter, String selecting) {
        String select = "select distinct orders.id,orders.completed,orders.created,orders.deleted,orders.description,orders.total_cost  ";
        String count = "select distinct count(*) from(select distinct orders.id,orders.completed,orders.created,orders.deleted,orders.description,orders.total_cost ";
        if (selecting.equals(COUNT)) {
            select = count;
        }
        String ql = select + "from orders orders join users users on orders.user_id = users.id " +
                "where users.surname like :userSurname and users.name like :userName ";
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
