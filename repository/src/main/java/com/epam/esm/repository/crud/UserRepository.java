package com.epam.esm.repository.crud;

import com.epam.esm.model.User;
import com.epam.esm.model.filter.UserFilter;
import com.epam.esm.model.wrapper.UserListWrapper;
import com.epam.esm.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByLogin(String login);

    UserListWrapper getAll(UserFilter filter);

    Optional<User> getUserByOrderId(Long orderId);

}
