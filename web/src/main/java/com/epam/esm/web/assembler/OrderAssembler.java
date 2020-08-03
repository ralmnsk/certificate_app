package com.epam.esm.web.assembler;

import com.epam.esm.dto.OrderDto;
import com.epam.esm.dto.filter.OrderFilterDto;
import com.epam.esm.dto.wrapper.ListWrapperDto;
import com.epam.esm.service.OrderService;
import com.epam.esm.web.controller.OrderController;
import com.epam.esm.web.controller.UserController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OrderAssembler implements Assembler<Long, OrderDto, OrderFilterDto> {
    private OrderService orderService;

    public OrderAssembler(OrderService orderService) {
        this.orderService = orderService;
    }

    public OrderDto assemble(Long orderId, OrderDto orderDto, Authentication authentication) {
        Link linkSelf = linkTo(methodOn(OrderController.class).get(orderDto.getId(),
                authentication
        )).withSelfRel();
        Link linkCreate = linkTo(methodOn(OrderController.class).create(orderDto, authentication)).withRel("post_create_order");
        Link linkUpdate = linkTo(methodOn(OrderController.class).update(orderDto, orderId, authentication)).withRel("put_update_order");
        Link linkDelete = linkTo(OrderController.class).slash(orderDto.getId()).withRel("delete_order");
        Link linkAddCertificates = linkTo(methodOn(OrderController.class)
                .addCertificateToOrder(orderId, new HashSet<Long>(), authentication)).withRel("put_add_certificates_to_order");
        Link linkDeleteCertificates = linkTo(methodOn(OrderController.class)
                .deleteCertificateFromOrder(orderId, new HashSet<Long>(), authentication)).withRel("put_remove_certificates_from_order");
        orderDto.add(linkSelf, linkCreate, linkUpdate, linkDelete, linkAddCertificates, linkDeleteCertificates);

        Link linkCertificates = linkTo(methodOn(OrderController.class)
                .getAllCertificatesByOrderId(null, 0, 5,
                        Arrays.asList(""), orderId, authentication))
                .withRel("certificates");
        orderDto.add(linkCertificates);

        return orderDto;
    }

    @Override
    public OrderDto assemble(Long number, OrderDto dto) {
        return assemble(number, dto, null);
    }

    public CollectionModel<OrderDto> toCollectionModel(OrderFilterDto filter) {
        ListWrapperDto<OrderDto, OrderFilterDto> wrapper = orderService.getAll(filter);
        List<OrderDto> orders = wrapper.getList();
        filter = wrapper.getFilterDto();
        orders.forEach(o -> {
            Link selfLink = linkTo(methodOn(OrderController.class).get(o.getId(),
                    null
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
                    )).withRel("user_id_" + filter.getUserId() + "_orders");
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
            addNextPreviousForAdmin(collectionModel, filter);
        }

        return collectionModel;
    }

    private void addNextPrevious(CollectionModel<OrderDto> collectionModel, OrderFilterDto filter) {
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
                    )).withRel("user_id_" + filter.getUserId() + "_orders_previous_page");
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
                    )).withRel("user_id_" + filter.getUserId() + "_orders_next_page");
            collectionModel.add(link);
        }
    }

    private void addNextPreviousForAdmin(CollectionModel<OrderDto> collectionModel, OrderFilterDto filter) {
        int page = filter.getPage();

        if (page > 0 && page <= filter.getTotalPages()) {
            Link link = linkTo(methodOn(UserController.class)
                    .getAll(filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getPage() - 1,
                            filter.getSize(),
                            filter.getSortParams(),
                            new Principal() {
                                @Override
                                public String getName() {
                                    return null;
                                }
                            }
                    )).withRel("orders_previous_page");
            collectionModel.add(link);
        }

        if (page >= 0 && page < filter.getTotalPages()) {
            Link link = linkTo(methodOn(UserController.class)
                    .getAll(filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getPage() + 1,
                            filter.getSize(),
                            filter.getSortParams(),
                            new Principal() {
                                @Override
                                public String getName() {
                                    return null;
                                }
                            }
                    )).withRel("orders_next_page");
            collectionModel.add(link);
        }
    }
}
