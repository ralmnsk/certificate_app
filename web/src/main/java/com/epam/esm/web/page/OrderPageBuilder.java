package com.epam.esm.web.page;

import com.epam.esm.service.dto.CustomPageDto;
import com.epam.esm.service.dto.ListWrapperDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.dto.filter.OrderFilterDto;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.assembler.OrderAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class OrderPageBuilder extends AbstractPageBuilder<OrderDto, OrderService<OrderDto, Long, OrderFilterDto>, OrderAssembler,OrderFilterDto> {
    private final String EMPTY = "";

    public OrderPageBuilder(OrderService<OrderDto, Long,OrderFilterDto> service, OrderAssembler assembler) {
        super(new HashSet<>(Arrays.asList("orders.id")), service, assembler);
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
}