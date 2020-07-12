package com.epam.esm.service.order;


import com.epam.esm.service.CrudService;
import com.epam.esm.service.dto.IdDto;

import java.util.List;

public interface OrderService<T, E> extends CrudService<T, E> {
    void addOrderToUser(Long userId, List<IdDto> list);

    void deleteOrderFromUser(Long userId, List<IdDto> list);

}
