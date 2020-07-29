package com.epam.esm.repository;

import com.epam.esm.model.User;
import com.epam.esm.model.filter.UserFilter;
import com.epam.esm.model.wrapper.UserListWrapper;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);

    UserListWrapper getAll(UserFilter filter);

    Optional<User> getUserByOrderId(Long orderId);

}
