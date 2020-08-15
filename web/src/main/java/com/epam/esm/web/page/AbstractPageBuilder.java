package com.epam.esm.web.page;

import com.epam.esm.dto.CustomPageDto;
import com.epam.esm.dto.filter.AbstractFilterDto;
import com.epam.esm.page.FilterDirection;
import com.epam.esm.page.FilterOrder;
import com.epam.esm.page.FilterSort;
import com.epam.esm.service.CrudService;
import com.epam.esm.web.assembler.Assembler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractPageBuilder<T, S extends CrudService, A extends Assembler, F extends AbstractFilterDto> {
    private static final String EMPTY = "";
    private static final String NAME = "name+";
    private static final String REGEX = "[a-zA-Z.+-]{0,20}";
    //"[a-zA-Z.]{0,20}(([+]{0,1})|([-]{0,1}))";
    private static final String PLUS = "+";
    private static final String MINUS = "-";

    private Set<String> fieldSet;
    private S service;
    private A assembler;

    public AbstractPageBuilder(Set<String> fieldSet, S service, A assembler) {
        this.fieldSet = fieldSet;
        this.service = service;
        this.assembler = assembler;
    }

    public S getService() {
        return service;
    }

    public void setService(S service) {
        this.service = service;
    }

    public abstract CustomPageDto<T> build(F filterDto);

    public CollectionModel<T> getCollectionModel(F filterDto) {
        return assembler.toCollectionModel(filterDto);
    }

    public F setSort(F filterDto) {
        List<String> paramsNonFiltered = filterDto.getSortParams();
        List<String> params = paramsNonFiltered.stream().filter(Objects::nonNull).collect(Collectors.toList());
        FilterSort filterSort = getSort(params);
        filterDto.setFilterSort(filterSort);
        return filterDto;
    }

    public F validateAbstractFilter(F filterDto) {
        if (filterDto.getSize() == 0) {
            filterDto.setSize(5);
        }
        if (filterDto.getPage() == 0) {
            filterDto.setPage(0);
        }
        if (filterDto.getSortParams() == null) {
            List<String> params = new ArrayList<>();
            params.add(NAME);
            filterDto.setSortParams(params);
        }

        return filterDto;
    }


    public FilterSort getSort(List<String> params) {
        List<FilterOrder> filterOrders = new ArrayList<>();
        for (String param : params) {

            if (param.matches(REGEX)) {
                FilterDirection filterDirection = FilterDirection.DESC;
                param = param.trim();
                if (param.contains("+")) {
                    filterDirection = FilterDirection.ASC;
                    param = param.replace(PLUS, EMPTY);
                } else {
                    param = param.replace(MINUS, EMPTY);
                }
                if (fieldSet.contains(param)) {
                    FilterOrder filterOrder = new FilterOrder(filterDirection, param);
                    filterOrders.add(filterOrder);
                }
            }
        }
        FilterSort filterSort = new FilterSort();
        filterSort.setFilterOrders(filterOrders);
        return filterSort;
    }

//    public boolean isContainDirectionMoreThenOne(String str) {
//        int plusCount = 0;
//        int minusCount = 0;
//        while (str.contains(PLUS)) {
//            str = str.replace(PLUS, EMPTY);
//            plusCount++;
//        }
//        while (str.contains(MINUS)) {
//            str = str.replace(MINUS, EMPTY);
//            minusCount++;
//        }
//
//        return (plusCount + minusCount) > 1;
//    }
}
