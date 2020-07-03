package com.epam.esm.web.assembler;

import com.epam.esm.service.dto.UserDto;
import com.epam.esm.web.controller.UserController;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.web.controller.ControllerConstants.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements Assembler<Long, UserDto> {
    @Override
    public UserDto assemble(Long id, UserDto userDto) {
        Link linkSelf = linkTo(methodOn(UserController.class).get(userDto.getId())).withSelfRel();
        Link linkToUserOrders = linkTo(methodOn(UserController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE, Sort.by(DEFAULT_SORT_ORDERS)), id)).withRel("orders");
        Link linkToAll = linkTo(methodOn(UserController.class).getAll(PageRequest.of(DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE))).withRel("users");
        userDto.add(linkSelf, linkToUserOrders, linkToAll);

        return userDto;
    }

    @Override
    public CollectionModel<UserDto> toCollectionModel(Long userId, List<UserDto> users, Pageable pageable) {
        users.forEach(u -> {
            Link link = linkTo(methodOn(UserController.class).get(u.getId())).withSelfRel();
            u.add(link);
//            Link linkOrders = linkTo(methodOn(UserController.class).getAll(pageable, u.getId())).withRel("orders");
//            u.add(linkOrders);
        });
        Link linkUsers = linkTo(methodOn(UserController.class).getAll(pageable)).withRel("users");

        return CollectionModel.of(users, linkUsers);
    }
}
