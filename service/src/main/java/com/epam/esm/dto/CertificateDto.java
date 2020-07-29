package com.epam.esm.dto;

import com.epam.esm.deserializer.CertificateDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@JsonRootName("certificate")
@Relation(collectionRelation = "certificates")
@JsonIgnoreProperties(ignoreUnknown = false, allowGetters = true, value = {"creation", "modification", "deleted", "tags"})
@JsonDeserialize(using = CertificateDeserializer.class)
public class CertificateDto extends IdentifiableDto<Long> {

    //    @NotNull(message = "Name must be not null")
//    @Size(min = 2, max = 256, message
//            = "Name must be between 2 and 256 characters")
    private String name;

    //    @NotNull
//    @Size(min = 0, max = 999, message
//            = "Description must be between 0 and 999 characters")
    private String description;

    //    @NotNull(message = "Price must be not null")
//    @Digits(integer = 13, fraction = 2, message = " Price should be numeric, example: 12.34 ")
//    @DecimalMin(value = "0.00", message = "Price should be 0.00 - 1000000000000.00")
//    @DecimalMax(value = "1000000000000.00", message = "Price should be 0.00 - 1000000000000.00")
    private BigDecimal price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Timestamp creation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "UTC")
    private Timestamp modification;

    //    @NotNull(message = "duration has to be not null")
//    @Range(min = 0, max = 100000, message = "duration range: 0 - 100000")
    private Integer duration;

    @JsonIgnore
    private Set<TagDto> tags = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CertificateDto that = (CertificateDto) o;

        if (isDeleted() != that.isDeleted()) return false;
        if (!name.equals(that.name)) return false;
        if (!description.equals(that.description)) return false;
        if (!price.equals(that.price)) return false;
        return duration.equals(that.duration);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + duration.hashCode();
        result = 31 * result + (isDeleted() ? 1 : 0);
        return result;
    }
}