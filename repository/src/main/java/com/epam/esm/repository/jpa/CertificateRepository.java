package com.epam.esm.repository.jpa;

import com.epam.esm.model.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    @Query("select o.certificates from Order o where  o.id = :orderId")
    Page<Certificate> getAllByOrderId(@Param("orderId") Long orderId, Pageable pageable);

    @Query("select b from Certificate b join b.tags u where u.id = :tagId")
    Optional<Certificate> getCertIdByTagId(@Param("tagId") int tagId);

    @Query(value = "select creation from certificate where id = :certId", nativeQuery = true)
    Instant getCreationById(@Param("certId") Long certId);

    @Query(value = "select modification from certificate where id = :certId", nativeQuery = true)
    Instant getModificationById(@Param("certId") Long certId);

    @Modifying
    @Query(value = "delete from cert_tag where certificate_id = :cId", nativeQuery = true)
    void removeFromTagRelationByCertificateId(@Param("cId") Long cId);

    @Modifying
    @Query(value = "delete from order_certificate where certificate_id = :cId", nativeQuery = true)
    void removeFromOrderRelationByCertificateId(@Param("cId") Long cId);

//    @Modifying
//    @Query(value = "insert into order_certificate ( order_id, certificate_id) values (:orderId, :certificateId)", nativeQuery = true)
//    void addCertificateToOrder(@Param("orderId") Long orderId, @Param("certificateId") Long certificateId);
}
