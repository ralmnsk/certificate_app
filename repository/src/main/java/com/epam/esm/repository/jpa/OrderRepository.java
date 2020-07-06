package com.epam.esm.repository.jpa;

import com.epam.esm.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select * from orders where user_id = :userId", nativeQuery = true)
    Page<Order> getAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select b from Order b join b.certificates u where u.id = :certId")
    Optional<Order> getOrderByCertificateId(@Param("certId") Long certId);

    @Query(value = "select created from orders where id = :orderId", nativeQuery = true)
    Instant getCreatedByOrderId(@Param("orderId") Long orderId);

    @Modifying
    @Query(value = "delete from order_certificate where order_id = :orderId", nativeQuery = true)
    void removeFromCertificateRelationByOrderId(@Param("orderId") Long orderId);

    @Modifying
    @Query(value = "update orders set user_id = :userId where id = :orderId", nativeQuery = true)
    void addOrderToUser(@Param("userId") Long userId, @Param("orderId") Long orderId);

}
