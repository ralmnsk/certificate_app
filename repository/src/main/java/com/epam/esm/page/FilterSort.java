package com.epam.esm.page;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FilterSort {
    private List<FilterOrder> filterOrders;
}
