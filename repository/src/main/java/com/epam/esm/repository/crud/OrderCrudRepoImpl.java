package com.epam.esm.repository.crud;

import com.epam.esm.model.Filter;
import com.epam.esm.model.Order;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

import static com.epam.esm.repository.crud.Constants.PERCENT_END;
import static com.epam.esm.repository.crud.Constants.PERCENT_START;

@Repository
@Getter
public class OrderCrudRepoImpl extends AbstractRepo<Order, Long> implements OrderCrudRepository {

    public OrderCrudRepoImpl() {
        super(Order.class);
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

        updateFilter(filter, pageSize, countResult);

        return orders;
    }

    private void querySetParameters(Filter filter, Query query) {
        query.setParameter("userSurname", PERCENT_START + filter.getUserSurname() + PERCENT_END);
        query.setParameter("userName", PERCENT_START + filter.getUserName() + PERCENT_END);
        query.setParameter("certificateName", PERCENT_START + filter.getCertificateName() + PERCENT_END);
    }

    private String assembleQlString(Filter filter, String selecting) {
        String select = "select distinct o.id,o.completed,o.created,o.deleted,o.description,o.total_cost,certificate.name ";
        String count = "select distinct count(*) ";
        if (selecting.equals("count")) {
            select = count;

        }
        String ql = select + "from orders o join order_certificate oc on o.id = oc.order_id join certificate certificate on oc.certificate_id = certificate.id join users u on o.user_id = u.id " +
                "where u.surname like :userSurname and u.name like :userName and certificate.name like :certificateName";

        ql = addSortToQueryString(filter, selecting, ql);

        return ql;
    }


}
