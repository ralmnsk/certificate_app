package com.epam.esm.repository.crud;

import com.epam.esm.model.ListWrapper;
import com.epam.esm.model.Order;
import com.epam.esm.model.filter.AbstractFilter;
import com.epam.esm.model.filter.OrderFilter;
import com.epam.esm.repository.CrudRepository;

public interface OrderRepository<T,E, F extends AbstractFilter> extends CrudRepository<T,E> {
    ListWrapper<T,F> getAll(F filter);
}
