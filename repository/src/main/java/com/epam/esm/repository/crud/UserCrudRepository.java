package com.epam.esm.repository.crud;

import com.epam.esm.model.User;
import com.epam.esm.repository.CrudRepository;

import java.util.Optional;

public interface UserCrudRepository extends CrudRepository<User, Long> {

//    Optional<User> getUserIdByOrderId(Long orderId);

}
