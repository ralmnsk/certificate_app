package com.epam.esm.web.assembler;

import com.epam.esm.service.dto.FilterDto;
import org.springframework.hateoas.CollectionModel;

//number Dto Controller
public interface Assembler<N, D> {
    D assemble(N n, D d);

    CollectionModel<D> toCollectionModel(FilterDto f);
}
