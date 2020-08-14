package com.epam.esm.web.page;

import com.epam.esm.dto.CustomPageDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.filter.OrderFilterDto;
import com.epam.esm.dto.wrapper.ListWrapperDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.web.assembler.OrderAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class OrderPageBuilder extends AbstractPageBuilder<OrderDto, OrderService, OrderAssembler, OrderFilterDto> {
    private static final String EMPTY = "";
    private static final String ORDERS_ID = "orders.id";

    public OrderPageBuilder(OrderService service, OrderAssembler assembler) {
        super(new HashSet<>(Arrays.asList(ORDERS_ID)), service, assembler);
    }

    public CustomPageDto<OrderDto> build(OrderFilterDto filterDto) {
        CustomPageDto<OrderDto> page = new CustomPageDto<>();
        filterDto = validateFilter(filterDto);

        filterDto = setSort(filterDto);

        ListWrapperDto<OrderDto, OrderFilterDto> listWrapperDto = getService().getAll(filterDto);
        CollectionModel<OrderDto> collectionModel = getCollectionModel(filterDto);

        filterDto = listWrapperDto.getFilterDto();
        page.setSize(filterDto.getSize());
        page.setTotalElements(filterDto.getTotalElements());
        page.setPage(filterDto.getPage());
        page.setElements(collectionModel);
        page.setTotalPage(filterDto.getTotalPages());

        return page;
    }

    private OrderFilterDto validateFilter(OrderFilterDto filterDto) {

        if (filterDto.getCertificateName() == null) {
            filterDto.setCertificateName(EMPTY);
        }
        if (filterDto.getUserSurname() == null) {
            filterDto.setUserSurname(EMPTY);
        }

        if (filterDto.getUserSurname() == null) {
            filterDto.setUserSurname(EMPTY);
        }
        if (filterDto.getUserName() == null) {
            filterDto.setUserName(EMPTY);
        }
        filterDto = validateAbstractFilter(filterDto);
        return filterDto;
    }
}