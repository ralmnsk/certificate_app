package com.epam.esm.service.calculator;

import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.OrderDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TotalCostCalculator {
    public OrderDto calc(OrderDto order) {
        Set<CertificateDto> certificates = order.getCertificates();
        if (certificates.isEmpty()) {
            return order;
        }
        BigDecimal totalCost = new BigDecimal("0.00");
        List<BigDecimal> collect = certificates
                .stream()
                .filter(c -> c.getPrice() != null)
                .map(CertificateDto::getPrice)
                .collect(Collectors.toList());

        for (BigDecimal b : collect) {
            totalCost = totalCost.add(b);
        }
        order.setTotalCost(totalCost);

        return order;
    }

}
