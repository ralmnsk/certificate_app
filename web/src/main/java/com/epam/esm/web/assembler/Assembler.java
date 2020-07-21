package com.epam.esm.web.assembler;

import com.epam.esm.dto.filter.AbstractFilterDto;
import org.springframework.hateoas.CollectionModel;

public interface Assembler<N, D, F extends AbstractFilterDto> {
    D assemble(N number, D dto);

    CollectionModel<D> toCollectionModel(F f);
}
