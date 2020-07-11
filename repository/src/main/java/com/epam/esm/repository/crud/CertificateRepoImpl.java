package com.epam.esm.repository.crud;

import com.epam.esm.model.Certificate;
import com.epam.esm.model.Filter;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

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

        String ql = assembleQlString(filter, "select");
        Query query = getEntityManager().createNativeQuery(ql, Certificate.class);
        int pageNumber = filter.getPage();
        int pageSize = filter.getSize();
        query.setFirstResult((pageNumber) * pageSize);
        query.setMaxResults(pageSize);
        List<Certificate> certificates = query.getResultList();

        Query queryTotal = getEntityManager().createNativeQuery
                (assembleQlString(filter, "count"));
        long countResult = Long.valueOf(queryTotal.getSingleResult().toString());

        updateFilter(filter, pageSize, countResult);

        return certificates;
    }



    private String assembleQlString(Filter filter, String selecting) {
        String select = "select certificate.id,certificate.creation,certificate.deleted,certificate.description,certificate.duration," +
                "       certificate.modification,certificate.name,certificate.price ";
        String count = "select count(*) ";
        if (selecting.equals("count")) {
            select = count;

        }
        String ql = select + " from certificate " +
                "    join cert_tag ct on certificate.id = ct.certificate_id " +
                "    join tag on ct.tag_id = tag.id " +
                "    join order_certificate oc on certificate.id = oc.certificate_id " +
                "    join orders on oc.order_id = orders.id " +
                "    join users on orders.user_id = users.id " +

                "where certificate.name like '%" + filter.getCertificateName() + "%'" +
                "  and certificate.duration >= " + filter.getDuration() +
                "  and certificate.modification >= '" + filter.getModification() + "'" +
                "  and certificate.creation >= '" + filter.getCreation() + "'" +
                "  and certificate.description like '%" + filter.getDescription() + "%'" +
                "  and tag.name like '%" + filter.getTagName() + "%'" +
                "and users.surname like '%" + filter.getUserSurname() + "%'";

        ql = addSortToQueryString(filter, selecting, ql);

        return ql;
    }
}
