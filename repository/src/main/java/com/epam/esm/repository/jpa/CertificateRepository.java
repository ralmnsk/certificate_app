package com.epam.esm.repository.jpa;

import com.epam.esm.model.Certificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    @Query("select o.certificates from Order o where  o.id = :orderId")
    Page<Certificate> getAllByOrderId(@Param("orderId") Long orderId, Pageable pageable);

    @Query("select b from Certificate b join b.tags u where u.id = :tagId")
    Optional<Certificate> getCertIdByTagId(@Param("tagId") int tagId);
}
