package com.epam.esm.web.assembler;

import com.epam.esm.service.dto.FilterDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.controller.UserController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler implements Assembler<Long, UserDto> {
    private UserService<UserDto, Long> userService;

    public UserAssembler(UserService<UserDto, Long> userService) {
        this.userService = userService;
    }

    public UserDto assemble(Long id, UserDto userDto) {
        Link linkSelf = linkTo(methodOn(UserController.class).get(userDto.getId())).withSelfRel();
        userDto.add(linkSelf);

        return userDto;
    }

    public CollectionModel<UserDto> toCollectionModel(FilterDto filter) {
        List<UserDto> users = userService.getAll(filter);
        filter = userService.getFilterDto();

        if (!users.isEmpty()) {
            users
                    .forEach(u -> {
                        Link link = linkTo(methodOn(UserController.class).get(u.getId())).withSelfRel();
                        u.add(link);
                    });
        }

        Link linkUsers = linkTo(methodOn(UserController.class)
                .getAll(filter.getUserSurname(), filter.getUserName(), filter.getPage(), filter.getSize(), filter.getSortParams())).withRel("users");
        CollectionModel<UserDto> collectionModel = CollectionModel.of(users, linkUsers);
        addNextPrevious(collectionModel, filter);

        return collectionModel;
    }

    private void addNextPrevious(CollectionModel<UserDto> collectionModel, FilterDto filter) {
        int page = filter.getPage();

        if (page > 0 && page <= filter.getTotalPages()) {
            Link link = linkTo(methodOn(UserController.class)
                    .getAll(
                            filter.getUserSurname(),
                            filter.getUserName(),
                            filter.getPage() - 1,
                            filter.getSize(),
                            filter.getSortParams()
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
                            filter.getSortParams()
                    )).withRel("users next page");
            collectionModel.add(link);
        }
    }
}
