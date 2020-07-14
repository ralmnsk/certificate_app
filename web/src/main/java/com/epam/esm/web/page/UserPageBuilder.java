package com.epam.esm.web.page;

import com.epam.esm.service.dto.CustomPageDto;
import com.epam.esm.service.dto.ListWrapperDto;
import com.epam.esm.service.dto.UserDto;
import com.epam.esm.service.dto.filter.UserFilterDto;
import com.epam.esm.service.user.UserService;
import com.epam.esm.web.assembler.UserAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class UserPageBuilder extends AbstractPageBuilder<UserDto, UserService<UserDto, Long, UserFilterDto>, UserAssembler, UserFilterDto> {
    private final String EMPTY = "";

    public UserPageBuilder(UserService<UserDto, Long, UserFilterDto> service, UserAssembler assembler) {
        super(new HashSet<>(Arrays.asList("surname", "name")), service, assembler);
    }

    public CustomPageDto<UserDto> build(UserFilterDto filterDto) {
        CustomPageDto<UserDto> page = new CustomPageDto<>();
        filterDto = validateFilter(filterDto);

        filterDto = setSort(filterDto);

        ListWrapperDto<UserDto, UserFilterDto> listWrapperDto = getService().getAll(filterDto);
        CollectionModel<UserDto> collectionModel = getCollectionModel(filterDto);

        filterDto = listWrapperDto.getFilterDto();
        page.setSize(filterDto.getSize());
        page.setTotalElements(filterDto.getTotalElements());
        page.setPage(filterDto.getPage());
        page.setElements(collectionModel);
        page.setTotalPage(filterDto.getTotalPages());

        return page;
    }
}