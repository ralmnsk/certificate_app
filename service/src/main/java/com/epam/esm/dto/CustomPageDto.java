package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.CollectionModel;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomPageDto<T> {
    private String surname;
    private String name;
    private long page;
    private long size;
    private long totalPage;
    private long totalElements;
    private CollectionModel<T> elements;
}
