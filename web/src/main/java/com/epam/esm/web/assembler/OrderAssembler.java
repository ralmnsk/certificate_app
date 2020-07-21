package com.epam.esm.web.assembler;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.filter.AbstractFilterDto;
import com.epam.esm.dto.filter.OrderFilterDto;
import com.epam.esm.dto.wrapper.ListWrapperDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.controller.UserController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements Assembler<Long, OrderDto, OrderFilterDto> {
    private OrderService orderService;

    public OrderAssembler(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderDto assemble(Long orderId, OrderDto orderDto) {
        Link linkSelfOrder = linkTo(methodOn(OrderController.class).get(orderDto.getId(),
                new Principal() {
                    @Override
                    public String getName() {
                        return null;
                    }
                }
        )).withSelfRel();
        orderDto.add(linkSelfOrder);
        Link linkCertificates = linkTo(methodOn(OrderController.class)
                .getAllCertificatesByOrderId(null, 0, 5, Arrays.asList(""), orderId, null))
                .withRel("certificates");
        orderDto.add(linkCertificates);

        return orderDto;
    }


    public CollectionModel<OrderDto> toCollectionModel(OrderFilterDto filter) {
        ListWrapperDto<OrderDto, OrderFilterDto> wrapper = orderService.getAll(filter);
        List<OrderDto> orders = wrapper.getList();
        filter = wrapper.getFilterDto();
        orders.forEach(o -> {
            Link selfLink = linkTo(methodOn(OrderController.class).get(o.getId(),
                    new Principal() {
                        @Override
                        public String getName() {
                            return null;
                        }
                    }
            )).withSelfRel();
            o.add(selfLink);
        });

        CollectionModel<OrderDto> collectionModel = CollectionModel.of(orders);
        if (filter.getUserId() != null && filter.getUserId() > 0) {
            Link link = linkTo(methodOn(UserController.class)
                    .getAllOrdersByUserId(filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getPage(), filter.getSize(), filter.getSortParams(),
                            filter.getUserId(),
                            new Principal() {
                                @Override
                                public String getName() {
                                    return null;
                                }
                            }
                    )).withRel("user id:" + filter.getUserId() + " orders");
            collectionModel.add(link);
            addNextPrevious(collectionModel, filter);
        } else {
            Link link = linkTo(methodOn(OrderController.class)
                    .getAll(filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getCertificateName(),
                            filter.getPage(),
                            filter.getSize(),
                            filter.getSortParams()
                            , null
                    )).withRel("orders");
            collectionModel.add(link);
        }

        return collectionModel;
    }

    private void addNextPrevious(CollectionModel<OrderDto> collectionModel, AbstractFilterDto filter) {
        int page = filter.getPage();

        if (page > 0 && page <= filter.getTotalPages()) {
            Link link = linkTo(methodOn(UserController.class)
                    .getAllOrdersByUserId(filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getPage() - 1,
                            filter.getSize(),
                            filter.getSortParams(),
                            filter.getUserId(),
                            new Principal() {
                                @Override
                                public String getName() {
                                    return null;
                                }
                            }
                    )).withRel("user id:" + filter.getUserId() + " orders previous page");
            collectionModel.add(link);
        }

        if (page >= 0 && page < filter.getTotalPages()) {
            Link link = linkTo(methodOn(UserController.class)
                    .getAllOrdersByUserId(filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getPage() + 1,
                            filter.getSize(),
                            filter.getSortParams(),
                            filter.getUserId(),
                            new Principal() {
                                @Override
                                public String getName() {
                                    return null;
                                }
                            }
                    )).withRel("user id:" + filter.getUserId() + " orders next page");
            collectionModel.add(link);
        }
    }
}
