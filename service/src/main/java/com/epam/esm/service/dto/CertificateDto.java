package com.epam.esm.service.dto;

import com.epam.esm.service.deserializer.StringToDecimalConverter;
import com.epam.esm.service.deserializer.StringToIntegerConverter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Certificate dto.
 * The CertificateDto is a DTO used to transfer
 * an certificate data between software application subsystems or
 * layers.
 */
@JsonIgnoreProperties(ignoreUnknown = false, allowGetters = true, value = {"creation", "modification"})
public class CertificateDto extends Dto<Long> {
    @NotNull(message = "Name must be not null")
    @Size(min = 2, max = 256, message
            = "Name must be between 2 and 256 characters")
    private String name;

    @Size(min = 0, max = 999, message
            = "Description must be between 0 and 999 characters")
    private String description;

    @Digits(integer = 13, fraction = 2, message = " Price should be numeric, example: 12.34 ")
    @DecimalMin(value = "0.00", message = "Price should be 0.00 - 1000000000000.00")
    @DecimalMax(value = "1000000000000.00", message = "Price should be 0.00 - 1000000000000.00")
    @JsonDeserialize(converter = StringToDecimalConverter.class)
    private BigDecimal price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    private Instant creation;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    private Instant modification;

    @NotNull(message = "duration has to be not null")
    @Range(min = 0, max = 100000, message = "duration range: 0 - 100000")
    @JsonDeserialize(converter = StringToIntegerConverter.class)
    private Integer duration;

    private Set<TagDto> tags = new HashSet<>();

    /**
     * Instantiates a new Certificate dto.
     */
    public CertificateDto() {
    }

    /**
     * Gets name.
     *
     * @return the name of the CertificateDto.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name of the CertificateDto.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets description.
     *
     * @return the description of the CertificateDto.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description of the CertificateDto
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets price.
     *
     * @return the price of the CertificateDto.
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price of the CertificateDto.
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets creation.
     *
     * @return the creation date of the CertificateDto.
     */
    public Instant getCreation() {
        return creation;
    }

    /**
     * Sets creation.
     *
     * @param creation the creation date of the CertificateDto
     */
    public void setCreation(Instant creation) {
        this.creation = creation;
    }

    /**
     * Gets modification.
     *
     * @return the modification date of the CertificateDto
     */
    public Instant getModification() {
        return modification;
    }

    /**
     * Sets modification.
     *
     * @param modification the modification date of the CertificateDto
     */
    public void setModification(Instant modification) {
        this.modification = modification;
    }

    /**
     * Gets duration.
     *
     * @return the duration in days of the CertificateDto
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration in days of the CertificateDto
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Gets tags.
     *
     * @return the tags that are contained in the CertificateDto
     */
    public Set<TagDto> getTags() {
        return tags;
    }

    /**
     * Sets tags.
     *
     * @param tags the tags that are contained in the CertificateDto
     */
    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        CertificateDto c = (CertificateDto) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.name, c.getName())
                .append(this.description, c.getDescription())
                .append(this.price, c.getPrice())
                .append(this.creation, c.getCreation())
                .append(this.modification, c.getModification())
                .append(this.duration, c.getDescription())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .append(description)
                .append(price)
                .append(creation)
                .append(duration)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "CertificateDto{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", creation=" + creation +
                ", modification=" + modification +
                ", duration=" + duration +
                ", tags=" + tags +
                '}';
    }
}
