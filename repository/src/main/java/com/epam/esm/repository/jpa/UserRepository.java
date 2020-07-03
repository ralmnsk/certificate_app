package com.epam.esm.repository.jpa;

import com.epam.esm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select b from User b join b.orders u where u.id = :orderId")
    Optional<User> getUserIdByOrderId(@Param("orderId") Long orderId);
}
