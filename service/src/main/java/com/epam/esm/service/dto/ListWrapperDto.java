package com.epam.esm.service.dto;

import com.epam.esm.service.dto.filter.AbstractFilterDto;
import lombok.Data;

import java.util.List;

@Data
public class ListWrapperDto<T, F extends AbstractFilterDto> {
    private List<T> list;
    private F filterDto;
}
