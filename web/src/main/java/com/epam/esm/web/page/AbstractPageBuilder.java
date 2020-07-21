package com.epam.esm.web.page;

import com.epam.esm.page.FilterDirection;
import com.epam.esm.page.FilterOrder;
import com.epam.esm.page.FilterSort;
import com.epam.esm.service.CrudService;
import com.epam.esm.dto.CustomPageDto;
import com.epam.esm.dto.filter.AbstractFilterDto;
import com.epam.esm.web.assembler.Assembler;
import org.springframework.hateoas.CollectionModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class AbstractPageBuilder<T, S extends CrudService, A extends Assembler, F extends AbstractFilterDto> {
    private final String EMPTY = "";
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

    public F validateFilter(F filterDto) {
        if (filterDto.getTagName() == null) {
            filterDto.setTagName(EMPTY);
        }
        if (filterDto.getCertificateName() == null) {
            filterDto.setCertificateName(EMPTY);
        }
        if (filterDto.getUserSurname() == null) {
            filterDto.setUserSurname(EMPTY);
        }
        if (filterDto.getCreation() == null) {
            filterDto.setCreation("1970-01-01 00:00:00");
        }
        if (filterDto.getModification() == null) {
            filterDto.setModification("1970-01-01 00:00:00");
        }
        if (filterDto.getDescription() == null) {
            filterDto.setDescription(EMPTY);
        }
        if (filterDto.getPrice() == null) {
            filterDto.setPrice(new BigDecimal(0.00));
        }
        if (filterDto.getDuration() == null) {
            filterDto.setDuration(0);
        }
        if (filterDto.getUserSurname() == null) {
            filterDto.setUserSurname(EMPTY);
        }
        if (filterDto.getUserName() == null) {
            filterDto.setUserName(EMPTY);
        }
        if (filterDto.getSize() == 0) {
            filterDto.setSize(5);
        }
        if (filterDto.getPage() == 0) {
            filterDto.setPage(0);
        }
        if (filterDto.getSortParams() == null) {
            List<String> params = new ArrayList<>();
            params.add("name+");
            filterDto.setSortParams(params);
        }

        return filterDto;
    }


    public FilterSort getSort(List<String> params) {
        List<FilterOrder> filterOrders = new ArrayList<>();
        for (String param : params) {
            if (param.matches("[a-z.]{0,20}(([+]{0,1})|([-]{0,1}))")) {
                FilterDirection filterDirection = FilterDirection.DESC;
                param = param.trim();
                if (param.contains("+")) {
                    filterDirection = FilterDirection.ASC;
                    param = param.replace("+", "");
                } else {
                    param = param.replace("-", "");
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
}
