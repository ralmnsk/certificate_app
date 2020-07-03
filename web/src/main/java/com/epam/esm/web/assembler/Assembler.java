package com.epam.esm.web.assembler;

import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;

import java.util.List;

//number Dto Controller
public interface Assembler<N, D> {
    D assemble(N n, D d);

    CollectionModel<D> toCollectionModel(N id, List<D> d, Pageable pageable);
}
