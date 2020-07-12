package com.epam.esm.repository.crud;

import com.epam.esm.model.*;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;

import static com.epam.esm.repository.crud.Constants.*;

@Component
public class QueryBuilder {

    private final String SELECT = " select distinct ";
    private final String TAG = " tag.id, tag.deleted, tag.name ";
    private final String CERTIFICATE = " certificate.id,certificate.creation,certificate.deleted,certificate.description,certificate.duration, certificate.modification,certificate.name,certificate.price ";
    private final String ORDER = " orders.id,orders.completed,orders.created,orders.deleted,orders.description,orders.total_cost ";
    private final String USER = " users.id, users.deleted, users.login, users.name, users.password, users.role, users.surname ";

    private final String TAG_TABLE = " tag ";
    private final String CERTIFICATE_TABLE = " certificate ";
    private final String ORDER_TABLE = " orders ";
    private final String USER_TABLE = " users ";

    private final String FROM = " from ";
    private final String JOIN = " join ";
    private final String TAG_JOIN_CERTIFICATE = " join cert_tag ct on certificate.id = ct.certificate_id join tag on ct.tag_id = tag.id ";
    private final String CERTIFICATE_JOIN_ORDER = " join order_certificate oc on certificate.id = oc.certificate_id join orders on oc.order_id = orders.id ";
    private final String ORDER_JOIN_USER = " join users on orders.user_id = users.id ";

    private final String WHERE = " where ";
    private final String AND = " and ";

    private final String TAG_NAME = " tag.name like :tagName ";

    private final String CERTIFICATE_NAME = " certificate.name like :certificateName ";
//    private final String CERTIFICATE_PRICE = " certificate.price >= :certificatePrice ";

//    private final String ORDER_TOTAL_COST = " orders.total_cost >= :totalCost ";

    private final String USER_SURNAME = " users.surname like :surname ";
    private final String USER_NAME = " users.name like :userName ";

    private final String TAG_ID = " tag.id = :tagId ";
    private final String CERTIFICATE_ID = " certificate.id = :certificateId ";
    private final String ORDER_ID = " orders.id = :orderId ";
    private final String USER_ID = " users.id = :userId ";

    private final String ORDER_BY = " order by  ";
    private final String SELECT_COUNT = "select count(*) from (";
    private final String SELECT_COUNT_END = " ) c ";
    private final String EMPTY = "";

    private HashSet<String> entityNameSet;

    private Filter filter;

    public String assembleQlString(Filter filter, Class clazz, String selecting) {//for class
        getEntityNameSet(filter);
        String tag_cert = entityNameSet.contains(TAG_TABLE) ? TAG_JOIN_CERTIFICATE : EMPTY;
        String cert_order = entityNameSet.contains(ORDER_TABLE) ? CERTIFICATE_JOIN_ORDER : EMPTY;
        String order_user = entityNameSet.contains(USER_TABLE) ? ORDER_JOIN_USER : EMPTY;

        String tag_name = entityNameSet.contains(TAG_TABLE) ? TAG_NAME + AND : EMPTY;
        String user_name = entityNameSet.contains(USER_TABLE) ? AND + USER_SURNAME + AND + USER_NAME : EMPTY;

        String ql = SELECT + addObjectColumns(clazz) + FROM + addObject(clazz) +
                tag_cert +
                cert_order +
                order_user +
                WHERE +
                tag_name +
                CERTIFICATE_NAME +
                user_name;

        ql = addObjectId(filter, ql);

        ql = addSortToQueryString(filter, selecting, ql);

        if (selecting.equals(COUNT)) {
            ql = SELECT_COUNT + ql + SELECT_COUNT_END;

        }

        return ql;
    }

    private Set<String> getEntityNameSet(Filter filter) {
        entityNameSet = new HashSet<>();
        if (filter.getTagId() != null && filter.getTagId() > 0) {
            entityNameSet.add(TAG_TABLE);
        }
        if (filter.getCertificateId() != null && filter.getCertificateId() > 0) {
            entityNameSet.add(CERTIFICATE_TABLE);
        }
        if (filter.getOrderId() != null && filter.getOrderId() > 0) {
            entityNameSet.add(ORDER_TABLE);
        }
        if (filter.getUserId() != null && filter.getUserId() > 0) {
            entityNameSet.add(USER_TABLE);
        }
        if (filter.getCertificateName() != null && !filter.getCertificateName().isEmpty()) {
            entityNameSet.add(CERTIFICATE_TABLE);
        }
        if (filter.getTagName() != null && !filter.getTagName().isEmpty()) {
            entityNameSet.add(TAG_TABLE);
        }
        if (filter.getUserSurname() != null && !filter.getUserSurname().isEmpty()) {
            entityNameSet.add(USER_TABLE);
        }
        if (filter.getUserName() != null && !filter.getUserName().isEmpty()) {
            entityNameSet.add(USER_TABLE);
        }


        return this.entityNameSet;
    }

    private String addObjectId(Filter filter, String ql) {
        if (filter.getTagId() != null && filter.getTagId() > 0) {
            ql = ql + AND + TAG_ID;
        }
        if (filter.getCertificateId() != null && filter.getCertificateId() > 0) {
            ql = ql + AND + CERTIFICATE_ID;
        }
        if (filter.getOrderId() != null && filter.getOrderId() > 0) {
            ql = ql + AND + ORDER_ID;
        }
        if (filter.getUserId() != null && filter.getUserId() > 0) {
            ql = ql + AND + USER_ID;
        }
        return ql;
    }


    public Filter updateFilter(Filter filter, int pageSize, long countResult) {
        filter.setTotalPages((countResult / pageSize));
        if (countResult % pageSize != 0) {
            filter.setTotalPages(((int) countResult / pageSize) + 1);
        }
        filter.setTotalElements(countResult);
        return filter;
    }

    private String addObjectColumns(Class clazz) {
        if (clazz.equals(Tag.class)) {
            return TAG;
        }
        if (clazz.equals(Certificate.class)) {
            return CERTIFICATE;
        }
        if (clazz.equals(Order.class)) {
            return ORDER;
        }
        if (clazz.equals(User.class)) {
            return USER;
        }
        return TAG;
    }

    private String addObject(Class clazz) {
        if (clazz.equals(Tag.class)) {
            return TAG_TABLE;
        }
        if (clazz.equals(Certificate.class)) {
            return CERTIFICATE_TABLE;
        }
        if (clazz.equals(Order.class)) {
            return ORDER_TABLE;
        }
        if (clazz.equals(User.class)) {
            return USER_TABLE;
        }
        return TAG_TABLE;
    }


    public String addSortToQueryString(Filter filter, String selecting, String ql) {
        if (!selecting.equals(COUNT) && filter.getSort() != null && !filter.getSort().getOrders().isEmpty()) {
            String str = filter
                    .getSort()
                    .getOrders()
                    .stream()
                    .map(o -> o.getParameter() + " " + o.getDirection()).reduce("", (a, b) -> " " + a + " " + b + ",");
            str = str.substring(0, str.length() - 1);
            ql = ql + ORDER_BY + str;
        }
        return ql;
    }

    public void setParameters(Filter filter, Query query) {
        query.setParameter("certificateName", PERCENT_START + filter.getCertificateName() + PERCENT_END);
//        if (entityNameSet.contains(CERTIFICATE_TABLE)) {
//        }
        if (entityNameSet.contains(TAG_TABLE)) {
            query.setParameter("tagName", PERCENT_START + filter.getTagName() + PERCENT_END);
        }
        if (entityNameSet.contains(USER_TABLE)) {
            query.setParameter("surname", PERCENT_START + filter.getUserSurname() + PERCENT_END);
            query.setParameter("userName", PERCENT_START + filter.getUserName() + PERCENT_END);
        }

        if (filter.getTagId() != null && filter.getTagId() > 0) {
            query.setParameter("tagId", filter.getTagId());
        }

        if (filter.getCertificateId() != null && filter.getCertificateId() > 0) {
            query.setParameter("certificateId", filter.getCertificateId());
        }


        if (filter.getOrderId() != null && filter.getOrderId() > 0) {
            query.setParameter("orderId", filter.getOrderId());
        }

        if (filter.getUserId() != null && filter.getUserId() > 0) {
            query.setParameter("userId", filter.getUserId());
        }
    }

}
