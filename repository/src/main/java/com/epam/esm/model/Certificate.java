package com.epam.esm.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


public class Certificate extends Entity<Long> {

    private String name;

    private String description;

    private BigDecimal price;

    private Instant creation;

    private Instant modification;

    private Integer duration;

    private Set<Tag> tags = new HashSet<Tag>();

    public Certificate() {
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Certificate that = (Certificate) o;
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
        return "Certificate{" +
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
