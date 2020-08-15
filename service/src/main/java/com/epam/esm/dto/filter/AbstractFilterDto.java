package com.epam.esm.dto.filter;


import com.epam.esm.page.FilterSort;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode
@Data
@NoArgsConstructor
public class AbstractFilterDto {


    private int page;
    private int size;
    private FilterSort filterSort;
    private long totalPages;
    private long totalElements;


    private List<String> sortParams = new ArrayList<>();

}