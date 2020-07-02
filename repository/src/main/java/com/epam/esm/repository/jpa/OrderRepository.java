package com.epam.esm.repository.jpa;

import com.epam.esm.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "select * from orders where user_id = :userId",nativeQuery = true)
    Page<Order> getAllByUserId(@Param("userId")Long userId, Pageable pageable);
}
