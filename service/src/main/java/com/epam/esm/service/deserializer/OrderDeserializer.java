package com.epam.esm.service.deserializer;

import com.epam.esm.service.calculator.TotalCostCalculator;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.OrderDto;
import com.epam.esm.service.exception.JsonDeserializationException;
import com.epam.esm.service.exception.NotFoundException;
import com.epam.esm.service.order.OrderService;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Slf4j
//@Component
public class OrderDeserializer extends StdDeserializer<OrderDto> {
    private OrderService<OrderDto, Long> service;
    private CertificateDeserializer certificateDeserializer;
    private TotalCostCalculator calculator;

    public OrderDeserializer() {
        this(null);
    }

    public OrderDeserializer(Class<?> vc) {
        super(vc);
    }

    @Autowired
    public void setService(OrderService<OrderDto, Long> service) {
        this.service = service;
    }

    @Autowired
    public void setCertificateDeserializer(CertificateDeserializer certificateDeserializer) {
        this.certificateDeserializer = certificateDeserializer;
    }

    @Autowired
    public void setCalculator(TotalCostCalculator calculator) {
        this.calculator = calculator;
    }

    @Override
    public OrderDto deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        return deserializeOrderNode(node);
    }

    public OrderDto deserializeOrderNode(JsonNode node) {
        OrderDto orderDto = new OrderDto();
        List<String> errorList = new ArrayList<>();

        Long id = null;
        String description = null;

        try {
            id = node.get("id").longValue();
            if (id < 1) {
                errorList.add("id must be more then 0");
                throw new JsonDeserializationException("id must be more then 0");
            }
            Optional<OrderDto> orderDtoOptional = service.get(id);
            if (orderDtoOptional.isPresent()) {
                orderDto = orderDtoOptional.get();
                orderDto.getCertificates().clear();
            } else{
                throw new NotFoundException(id);
            }
        } catch (Exception e) {
            if (e.getClass().equals(NotFoundException.class)) {
                throw new NotFoundException(id);
            }
            log.warn("Order exception, id:" + e.getMessage());
        }

        try {
            description = node.get("description").asText();
            if (description == null || description.isEmpty()) {
                errorList.add("order description == null JsonDeserializationException");
            }
            description = node.get("description").asText();

            if (description.length() < 2 || description.length() > 999) {
                errorList.add("Name must be between 2 and 999 characters");
            }
            orderDto.setDescription(description);
        } catch (Exception e) {
//            errorList.add(e.getMessage());
        }


        Iterator<JsonNode> tagsIterator = node.get("certificates").elements();
        Set<CertificateDto> certificateDtos = new HashSet<>();
        try {
            while (tagsIterator.hasNext()) {
                JsonNode certificateNode = tagsIterator.next();
                CertificateDto certificateDto = certificateDeserializer.deserializeCertificateNode(certificateNode);
                certificateDtos.add(certificateDto);
            }
        } catch (Exception e) {
            errorList.add(e.getMessage());
        }

        if (!errorList.isEmpty()) {
            String result = errorList.stream().reduce("", (one, two) -> one + ", " + two);
            log.warn(result);
            throw new JsonDeserializationException(result);
        }

        orderDto.setCertificates(certificateDtos);
        calculator.calc(orderDto);

        return orderDto;
    }
}
