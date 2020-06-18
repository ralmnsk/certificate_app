package com.epam.esm.service.dto;

import com.epam.esm.service.view.Profile;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties(ignoreUnknown = false, allowGetters = true, value = {"creation", "modification"})
public class CertificateDto extends Dto<Long> {
    @JsonView(Profile.PublicView.class)
    @NotNull
    @Size(min = 2, max = 256, message
            = "Name must be between 2 and 256 characters")
    private String name;

    @JsonView(Profile.PublicView.class)
    @Size(min = 0, max = 999, message
            = "Description must be between 0 and 999 characters")
    private String description;

    @JsonView(Profile.PublicView.class)
    @DecimalMin(value = "0.00")
    @DecimalMax(value = "1000000000000.00")
    @Digits(integer = 13, fraction = 2)
    private BigDecimal price;

    @JsonView(Profile.PublicView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    private Instant creation;

    @JsonView(Profile.PublicView.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "UTC")
    private Instant modification;

    @JsonView(Profile.PublicView.class)
    @NotNull
    @Min(value = 0)
    @Max(value = 100000)
    private Integer duration;

    @JsonView(Profile.PublicView.class)
    private Set<TagDto> tagDtos = new HashSet<TagDto>();

    public CertificateDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Instant getCreation() {
        return creation;
    }

    public void setCreation(Instant creation) {
        this.creation = creation;
    }

    public Instant getModification() {
        return modification;
    }

    public void setModification(Instant modification) {
        this.modification = modification;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Set<TagDto> getTagDtos() {
        return tagDtos;
    }

    public void setTagDtos(Set<TagDto> tagDtos) {
        this.tagDtos = tagDtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CertificateDto that = (CertificateDto) o;

        if (!name.equals(that.name)) return false;
        return creation.equals(that.creation);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + creation.hashCode();
        return result;
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
                ", tagDtos=" + tagDtos +
                '}';
    }
}
