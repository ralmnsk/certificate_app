package com.epam.esm.service.dto;

import com.epam.esm.service.deserializer.StringToDecimalConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Data
//@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@JsonRootName("order")
@Relation(collectionRelation = "orders")
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = false, allowGetters = true, value = {"created"})
public class OrderDto extends RepresentationModel<OrderDto> {
    private Long id;

    private UserDto userDto;

    @Size(min = 0, max = 999, message = "Description must be between 0 and 999 characters")
    private String description;

    @Digits(integer = 13, fraction = 2, message = "Cost should be numeric, example: 12.34 ")
    @DecimalMin(value = "0.00", message = "Cost should be 0.00 - 1000000000000.00")
    @DecimalMax(value = "1000000000000.00", message = "Cost should be 0.00 - 1000000000000.00")
    @JsonDeserialize(converter = StringToDecimalConverter.class)
    private BigDecimal totalCost;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Instant created;

    private boolean deleted;

    private Set<CertificateDto> certificates = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        OrderDto orderDto = (OrderDto) o;

        if (deleted != orderDto.deleted) return false;
        if (!description.equals(orderDto.description)) return false;
        if (!totalCost.equals(orderDto.totalCost)) return false;
        return created.equals(orderDto.created);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + totalCost.hashCode();
        result = 31 * result + created.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }
}