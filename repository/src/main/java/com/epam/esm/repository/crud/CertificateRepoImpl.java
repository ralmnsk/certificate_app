package com.epam.esm.repository.crud;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Filter;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

import static com.epam.esm.repository.crud.Constants.*;

@Repository
@Getter
public class CertificateRepoImpl extends AbstractRepo<Certificate, Long> implements CertificateCrudRepository {
    private final String EMPTY = "";

    public CertificateRepoImpl() {
        super(Certificate.class);
    }

//    @Override
//    public Page<Certificate> getAllByOrderId(Long orderId, Pageable pageable) {
//        Query query = getEntityManager().createNativeQuery("select * from certificate join order_certificate oc on certificate.id = oc.certificate_id join " +
//                "orders o on oc.order_id = o.id where o.id = :orderId", Certificate.class);
//        query.setParameter("orderId", orderId);
//        int pageNumber = pageable.getPageNumber();
//        int pageSize = pageable.getPageSize();
//        query.setFirstResult((pageNumber) * pageSize);
//        query.setMaxResults(pageSize);
//        List<Certificate> certificates = query.getResultList();
//
//        Query queryTotal = getEntityManager().createNativeQuery("select count(*) from certificate join order_certificate oc on certificate.id = oc.certificate_id join " +
//                "orders o on oc.order_id = o.id where o.id = :orderId");
//        queryTotal.setParameter("orderId", orderId);
//        BigInteger countResult = (BigInteger) queryTotal.getSingleResult();
//        int i = countResult.intValue();
//
//        return new PageImpl<Certificate>(certificates, pageable, i);
//    }

//    @Override
//    public Instant getCreationById(Long certId) {
//        Query query = getEntityManager().createNativeQuery("select creation from certificate where id = :certId");
//        query.setParameter("certId", certId);
//        Timestamp timestamp = (Timestamp) query.getSingleResult();
//        Instant instant = timestamp.toInstant();
//        return instant;
//    }
//
//    @Override
//    public Instant getModificationById(Long certId) {
//        Query query = getEntityManager().createNativeQuery("select modification from certificate where id = :certId");
//        query.setParameter("certId", certId);
//        Timestamp timestamp = (Timestamp) query.getSingleResult();
//        if (timestamp != null) {
//            return timestamp.toInstant();
//        }
//        return null;
//    }
//
//    @Override
//    public void removeFromTagRelationByCertificateId(Long cId) {
//        Query query = getEntityManager().createNativeQuery("delete from cert_tag where certificate_id = :certId");
//        query.setParameter("certId", cId);
//        query.executeUpdate();
//    }
//
//    @Override
//    public void removeFromOrderRelationByCertificateId(Long cId) {
//        Query query = getEntityManager().createNativeQuery("delete from order_certificate where certificate_id = :certId");
//        query.setParameter("certId", cId);
//        query.executeUpdate();
//    }

    @Override
    public List<Certificate> getAll(Filter filter) {

        String ql = assembleQlString(filter, SELECT);
        Query query = getEntityManager().createNativeQuery(ql, Certificate.class);
        setParameters(filter, query);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Certificate> certificates = query.getResultList();

        Query queryTotal = getEntityManager().createNativeQuery
                (assembleQlString(filter, COUNT));
        setParameters(filter, queryTotal);

        long countResult = Long.valueOf(queryTotal.getSingleResult().toString());

        updateFilter(filter, pageSize, countResult);

        return certificates;
    }

    private void setParameters(Filter filter, Query query) {
        query.setParameter("certificateName", PERCENT_START + filter.getCertificateName() + PERCENT_END);
//        query.setParameter("duration", PERCENT_START + filter.getDuration() + PERCENT_END);
//        query.setParameter("modification", PERCENT_START + filter.getModification() + PERCENT_END);
//        query.setParameter("creation", PERCENT_START + filter.getCreation() + PERCENT_END);
        query.setParameter("description", PERCENT_START + filter.getDescription() + PERCENT_END);
        query.setParameter("tagName", PERCENT_START + filter.getTagName() + PERCENT_END);
        query.setParameter("surname", PERCENT_START + filter.getUserSurname() + PERCENT_END);
    }


    private String assembleQlString(Filter filter, String selecting) {
        String select = "select distinct certificate.id,certificate.creation,certificate.deleted,certificate.description,certificate.duration," +
                "       certificate.modification,certificate.name,certificate.price ";
        String count = "select count(*) from (";

        String ql = select + " from certificate " +
                "    join cert_tag ct on certificate.id = ct.certificate_id " +
                "    join tag on ct.tag_id = tag.id " +
                "    join order_certificate oc on certificate.id = oc.certificate_id " +
                "    join orders on oc.order_id = orders.id " +
                "    join users on orders.user_id = users.id " +

                "where certificate.name like :certificateName " +   //+ filter.getCertificateName() +
//                "  and certificate.duration >= :duration " +        //+ filter.getDuration() +
//                "  and certificate.modification >= :modification " +                //'" + filter.getModification() + "'" +
//                "  and certificate.creation >= :creation " +                     //'" + filter.getCreation() + "'" +
                "  and certificate.description like :description " + //'%" + filter.getDescription() + "%'" +
                "  and tag.name like :tagName " + //'%" + filter.getTagName() + "%'" +
                "and users.surname like :surname ";  //'%" + filter.getUserSurname() + "%'";

        ql = addSortToQueryString(filter, selecting, ql);
        if (selecting.equals(COUNT)) {
            ql = count + ql + ") c";

        }

        return ql;
    }
}
