package com.epam.esm.calculator;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.OrderDto;
import com.epam.esm.model.Certificate;
import com.epam.esm.model.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TotalCostCalculator {

    public Order calc(Order order) {
        Set<Certificate> certificates = order.getCertificates();
        return getOrder(order, certificates);
    }

    private Order getOrder(Order order, Set<Certificate> certificates) {
        if (certificates.isEmpty()) {
            return order;
        }
        BigDecimal totalCost = new BigDecimal("0.00");
        List<BigDecimal> collect = certificates
                .stream()
                .filter(c -> c.getPrice() != null)
                .filter(c -> !c.isDeleted())
                .map(Certificate::getPrice)
                .collect(Collectors.toList());

        for (BigDecimal b : collect) {
            totalCost = totalCost.add(b);
        }
        order.setTotalCost(totalCost);

        return order;
    }

    public OrderDto calc(OrderDto order) {
        Set<CertificateDto> certificates = order.getCertificates();
        return getOrderDto(order, certificates);
    }

    private OrderDto getOrderDto(OrderDto order, Set<CertificateDto> certificates) {
        if (certificates.isEmpty()) {
            return order;
        }
        BigDecimal totalCost = new BigDecimal("0.00");
        List<BigDecimal> collect = certificates
                .stream()
                .filter(c -> c.getPrice() != null)
                .filter(c -> !c.isDeleted())
                .map(CertificateDto::getPrice)
                .collect(Collectors.toList());

        for (BigDecimal b : collect) {
            totalCost = totalCost.add(b);
        }
        order.setTotalCost(totalCost);

        return order;
    }

}
