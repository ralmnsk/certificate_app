package com.epam.esm.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterOrder {
    private FilterDirection filterDirection;
    private String parameter;
}
