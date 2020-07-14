package com.epam.esm.service.order;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.ListWrapperDto;
import com.epam.esm.service.dto.filter.AbstractFilterDto;

import java.util.Set;

public interface OrderService<T, E, F extends AbstractFilterDto> extends CrudService<T, E> {
    void addOrderToUser(Long userId, Set<Long> list);

    void deleteOrderFromUser(Long userId, Set<Long> list);

    ListWrapperDto<T, F> getAll(F filterDto);

}
