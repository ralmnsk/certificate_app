package com.epam.esm.repository.crud;

import com.epam.esm.model.ListWrapper;
import com.epam.esm.model.User;
import com.epam.esm.model.filter.AbstractFilter;
import com.epam.esm.model.filter.UserFilter;
import com.epam.esm.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository<T,E,F extends AbstractFilter> extends CrudRepository<T,E> {
    T findByLogin(String login);

    ListWrapper<T,F> getAll(F filter);

    Optional<T> getUserByOrderId(E orderId);

}
