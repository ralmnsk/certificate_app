package com.epam.esm.repository.crud;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.User;
import com.epam.esm.model.filter.AbstractFilter;
import com.epam.esm.model.filter.TagFilter;
import org.springframework.stereotype.Component;

import javax.persistence.Query;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Filter;

import static com.epam.esm.repository.crud.Constants.*;

@Component
public class QueryBuilder<F extends AbstractFilter> {

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

    private F filter;

    public String assembleQlString(AbstractFilter abstractFilter, Class clazz, String selecting) {//for class
        getEntityNameSet(abstractFilter);
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

        ql = addObjectId(abstractFilter, ql);

        ql = addSortToQueryString(abstractFilter, selecting, ql);

        if (selecting.equals(COUNT)) {
            ql = SELECT_COUNT + ql + SELECT_COUNT_END;

        }

        return ql;
    }

    private Set<String> getEntityNameSet(AbstractFilter abstractFilter) {
        entityNameSet = new HashSet<>();
        if (abstractFilter.getTagId() != null && abstractFilter.getTagId() > 0) {
            entityNameSet.add(TAG_TABLE);
        }
        if (abstractFilter.getCertificateId() != null && abstractFilter.getCertificateId() > 0) {
            entityNameSet.add(CERTIFICATE_TABLE);
        }
        if (abstractFilter.getOrderId() != null && abstractFilter.getOrderId() > 0) {
            entityNameSet.add(ORDER_TABLE);
        }
        if (abstractFilter.getUserId() != null && abstractFilter.getUserId() > 0) {
            entityNameSet.add(USER_TABLE);
        }
        if (abstractFilter.getCertificateName() != null && !abstractFilter.getCertificateName().isEmpty()) {
            entityNameSet.add(CERTIFICATE_TABLE);
        }
        if (abstractFilter.getTagName() != null && !abstractFilter.getTagName().isEmpty()) {
            entityNameSet.add(TAG_TABLE);
        }
        if (abstractFilter.getUserSurname() != null && !abstractFilter.getUserSurname().isEmpty()) {
            entityNameSet.add(USER_TABLE);
        }
        if (abstractFilter.getUserName() != null && !abstractFilter.getUserName().isEmpty()) {
            entityNameSet.add(USER_TABLE);
        }


        return this.entityNameSet;
    }

    private String addObjectId(AbstractFilter abstractFilter, String ql) {
        if (abstractFilter.getTagId() != null && abstractFilter.getTagId() > 0) {
            ql = ql + AND + TAG_ID;
        }
        if (abstractFilter.getCertificateId() != null && abstractFilter.getCertificateId() > 0) {
            ql = ql + AND + CERTIFICATE_ID;
        }
        if (abstractFilter.getOrderId() != null && abstractFilter.getOrderId() > 0) {
            ql = ql + AND + ORDER_ID;
        }
        if (abstractFilter.getUserId() != null && abstractFilter.getUserId() > 0) {
            ql = ql + AND + USER_ID;
        }
        return ql;
    }


    public F updateFilter(F filter, int pageSize, long countResult) {
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


    public String addSortToQueryString(AbstractFilter abstractFilter, String selecting, String ql) {
        if (!selecting.equals(COUNT) && abstractFilter.getFilterSort() != null && !abstractFilter.getFilterSort().getFilterOrders().isEmpty()) {
            String str = abstractFilter
                    .getFilterSort()
                    .getFilterOrders()
                    .stream()
                    .map(o -> o.getParameter() + " " + o.getFilterDirection()).reduce("", (a, b) -> " " + a + " " + b + ",");
            str = str.substring(0, str.length() - 1);
            ql = ql + ORDER_BY + str;
        }
        return ql;
    }

    public void setParameters(AbstractFilter abstractFilter, Query query) {
        query.setParameter("certificateName", PERCENT_START + abstractFilter.getCertificateName() + PERCENT_END);
//        if (entityNameSet.contains(CERTIFICATE_TABLE)) {
//        }
        if (entityNameSet.contains(TAG_TABLE)) {
            query.setParameter("tagName", PERCENT_START + abstractFilter.getTagName() + PERCENT_END);
        }
        if (entityNameSet.contains(USER_TABLE)) {
            query.setParameter("surname", PERCENT_START + abstractFilter.getUserSurname() + PERCENT_END);
            query.setParameter("userName", PERCENT_START + abstractFilter.getUserName() + PERCENT_END);
        }

        if (abstractFilter.getTagId() != null && abstractFilter.getTagId() > 0) {
            query.setParameter("tagId", abstractFilter.getTagId());
        }

        if (abstractFilter.getCertificateId() != null && abstractFilter.getCertificateId() > 0) {
            query.setParameter("certificateId", abstractFilter.getCertificateId());
        }


        if (abstractFilter.getOrderId() != null && abstractFilter.getOrderId() > 0) {
            query.setParameter("orderId", abstractFilter.getOrderId());
        }

        if (abstractFilter.getUserId() != null && abstractFilter.getUserId() > 0) {
            query.setParameter("userId", abstractFilter.getUserId());
        }
    }

}
