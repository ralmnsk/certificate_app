package com.epam.esm.web.assembler;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.filter.AbstractFilterDto;
import com.epam.esm.dto.filter.UserFilterDto;
import com.epam.esm.dto.wrapper.ListWrapperDto;
import com.epam.esm.service.UserService;
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
public class UserAssembler implements Assembler<Long, UserDto, UserFilterDto> {
    private UserService userService;

    public UserAssembler(UserService userService) {
        this.userService = userService;
    }

    public UserDto assemble(Long id, UserDto userDto) {
        Link linkSelf = linkTo(methodOn(UserController.class).get(userDto.getId(),
                null)).withSelfRel();
        userDto.add(linkSelf);

        Link linkOrders = linkTo(methodOn(UserController.class)
                .getAllOrdersByUserId(null, null, 0, 5, Arrays.asList(""), id, null))
                .withRel("user: " + userDto.getSurname() + " " + userDto.getName() + " orders");
        userDto.add(linkOrders);
        return userDto;
    }

    public CollectionModel<UserDto> toCollectionModel(UserFilterDto filter) {
        ListWrapperDto<UserDto, UserFilterDto> wrapper = userService.getAll(filter);
        List<UserDto> users = wrapper.getList();
        filter = wrapper.getFilterDto();

        if (!users.isEmpty()) {
            users
                    .forEach(u -> {
                        Link link = linkTo(methodOn(UserController.class).get(u.getId(), new Principal() {
                            @Override
                            public String getName() {
                                return null;
                            }
                        })).withSelfRel();
                        u.add(link);
                    });
        }

        Link linkUsers = linkTo(methodOn(UserController.class)
                .getAll(
                        filter.getUserSurname(),
                        filter.getUserName(),
                        filter.getPage(),
                        filter.getSize(),
                        filter.getSortParams(),
                        null
                )).withRel("users");
        CollectionModel<UserDto> collectionModel = CollectionModel.of(users, linkUsers);
        if (filter.getUserId() != null && filter.getUserId() > 0) {
            Link myAccountLink = linkTo(methodOn(UserController.class).get(filter.getUserId(), null)).withRel("My account");
            collectionModel.add(myAccountLink);
        }
        addNextPrevious(collectionModel, filter);

        return collectionModel;
    }

    private void addNextPrevious(CollectionModel<UserDto> collectionModel, AbstractFilterDto filter) {
        int page = filter.getPage();

        if (page > 0 && page <= filter.getTotalPages()) {
            Link link = linkTo(methodOn(UserController.class)
                    .getAll(
                            filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getPage() - 1,
                            filter.getSize(),
                            filter.getSortParams(),
                            null
                    )).withRel("users previous page");
            collectionModel.add(link);
        }

        if (page >= 0 && page < filter.getTotalPages()) {
            Link link = linkTo(methodOn(UserController.class)
                    .getAll(
                            filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getPage() + 1,
                            filter.getSize(),
                            filter.getSortParams(),
                            null
                    )).withRel("users next page");
            collectionModel.add(link);
        }
    }
}
