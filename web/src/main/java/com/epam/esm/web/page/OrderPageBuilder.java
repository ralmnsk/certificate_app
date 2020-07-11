package com.epam.esm.web.page;

import com.epam.esm.service.dto.CustomPageDto;
import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.order.OrderService;
import com.epam.esm.web.assembler.OrderAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class OrderPageBuilder extends AbstractPageBuilder<OrderDto,OrderService<OrderDto,Long>,OrderAssembler> {
    private final String EMPTY = "";
//    private OrderService<OrderDto, Long> orderService;
//    private OrderAssembler orderAssembler;

    public OrderPageBuilder(OrderService<OrderDto, Long> service, OrderAssembler assembler, OrderService<OrderDto, Long> orderService, OrderAssembler orderAssembler) {
        super(new HashSet<>(Arrays.asList("users.surname","users.name","certificate.name")), service, assembler);
    }


    //    public CustomPageDto<OrderDto> build(FilterDto filterDto) {
//        CustomPageDto<OrderDto> page = new CustomPageDto<>();
//        filterDto = validateFilter(filterDto);
//
//        filterDto = setSort(filterDto);
//        orderService.getAll(filterDto);
//        CollectionModel<OrderDto> collectionModel = getCollectionModel(filterDto);
//
//        filterDto = orderService.getFilterDto();
//        page.setSize(filterDto.getSize());
//        page.setTotalElements(filterDto.getTotalElements());
//        page.setPage(filterDto.getPage());
//        page.setElements(collectionModel);
//        page.setTotalPage(filterDto.getTotalPages());
//
//        return page;
//    }
//
//    private CollectionModel<OrderDto> getCollectionModel(FilterDto filterDto) {
//        return orderAssembler.toCollectionModel(filterDto);
//    }


}