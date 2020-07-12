package com.epam.esm.web.assembler;

import com.epam.esm.service.dto.FilterDto;
import org.springframework.hateoas.CollectionModel;

public interface Assembler<N, D> {
    D assemble(N number, D dto);

    CollectionModel<D> toCollectionModel(FilterDto f);
}
