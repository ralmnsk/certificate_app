package com.epam.esm.web.page;

import com.epam.esm.dto.CustomPageDto;
import com.epam.esm.dto.UserDto;
import com.epam.esm.dto.filter.UserFilterDto;
import com.epam.esm.dto.wrapper.ListWrapperDto;
import com.epam.esm.service.UserService;
import com.epam.esm.web.assembler.UserAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;

@Component
public class UserPageBuilder extends AbstractPageBuilder<UserDto, UserService, UserAssembler, UserFilterDto> {
    private static final String EMPTY = "";
    private static final String SURNAME = "surname";
    private static final String NAME = "name";

    public UserPageBuilder(UserService service, UserAssembler assembler) {
        super(new HashSet<>(Arrays.asList(SURNAME, NAME)), service, assembler);
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


    private UserFilterDto validateFilter(UserFilterDto filterDto) {

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