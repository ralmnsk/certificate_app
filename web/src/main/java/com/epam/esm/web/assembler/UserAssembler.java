package com.epam.esm.web.assembler;

import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.UserUpdateDto;
import com.epam.esm.dto.filter.UserFilterDto;
import com.epam.esm.dto.security.RegistrationDto;
import com.epam.esm.dto.wrapper.ListWrapperDto;
import com.epam.esm.service.UserService;
import com.epam.esm.web.controller.GateController;
import com.epam.esm.web.controller.UserController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

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

    public UserDto assemble(Long id, UserDto userDto, Authentication authentication) {
        Link linkSelf = linkTo(methodOn(UserController.class).get(userDto.getId(),
                authentication)).withSelfRel();
        Link linkRegister = linkTo(methodOn(GateController.class).register(new RegistrationDto())).withRel("post_registration_user");
        Link linkUpdate = linkTo(methodOn(UserController.class).update(new UserUpdateDto(), id, authentication)).withRel("put_update_user");
        Link linkDelete = linkTo(UserController.class).slash(userDto.getId()).withRel("delete_user");
        userDto.add(linkSelf, linkRegister, linkUpdate, linkDelete);


        Link linkOrders = linkTo(methodOn(UserController.class)
                .getAllOrdersByUserId(null, null, 0, 5, Arrays.asList(""),
                        id, null))
                .withRel("user_id_" + userDto.getId() + "_orders");
        userDto.add(linkOrders);
        return userDto;
    }

    @Override
    public UserDto assemble(Long number, UserDto dto) {
        return assemble(number, dto, null);
    }

    public CollectionModel<UserDto> toCollectionModel(UserFilterDto filter) {
        ListWrapperDto<UserDto, UserFilterDto> wrapper = userService.getAll(filter);
        List<UserDto> users = wrapper.getList();
        filter = wrapper.getFilterDto();

        if (!users.isEmpty()) {
            users
                    .forEach(u -> {
                        Link link = linkTo(methodOn(UserController.class).get(u.getId(),
                                null)).withSelfRel();
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
            Link myAccountLink = linkTo(methodOn(UserController.class)
                    .get(filter.getUserId(), null)).withRel("my_account");
            collectionModel.add(myAccountLink);
        }
        addNextPrevious(collectionModel, filter);

        return collectionModel;
    }

    private void addNextPrevious(CollectionModel<UserDto> collectionModel, UserFilterDto filter) {
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
                    )).withRel("users_previous_page");
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
                    )).withRel("users_next_page");
            collectionModel.add(link);
        }
    }
}
