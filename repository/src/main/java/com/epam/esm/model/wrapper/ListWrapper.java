package com.epam.esm.model.wrapper;

import com.epam.esm.model.filter.AbstractFilter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ListWrapper<T,F extends AbstractFilter> {
    private List<T> list;
    private F filter;
}
