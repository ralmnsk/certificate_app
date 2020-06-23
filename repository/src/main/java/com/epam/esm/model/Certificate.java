package com.epam.esm.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;


/**
 * The type Certificate.
 */
public class Certificate extends Identifiable<Long> {

    private String name;

    private String description;

    private BigDecimal price;

    private Instant creation;

    private Instant modification;

    private Integer duration;

    private Set<Tag> tags = new HashSet<>();

    /**
     * Instantiates a new Certificate.
     * Default constructor.  Setters are used to set fields of the certificate.
     */
    public Certificate() {
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description of the certificate
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets price.
     *
     * @return the price of the certificate
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price of the certificate
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets creation.
     *
     * @return the creation date of the certificate
     */
    public Instant getCreation() {
        return creation;
    }

    /**
     * Sets creation.
     *
     * @param creation the creation date of the certificate
     */
    public void setCreation(Instant creation) {
        this.creation = creation;
    }

    /**
     * Gets modification.
     *
     * @return the modification date of the certificate
     */
    public Instant getModification() {
        return modification;
    }

    /**
     * Sets modification.
     *
     * @param modification the modification date of the certificate
     */
    public void setModification(Instant modification) {
        this.modification = modification;
    }

    /**
     * Gets duration.
     *
     * @return the duration in days of the certificate
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration in days of the certificate
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Gets tags.
     *
     * @return the tags contained in the certificate
     */
    public Set<Tag> getTags() {
        return tags;
    }

    /**
     * Sets tags.
     *
     * @param tags the tags contained in the certificate
     */
    public void setTags(Set<Tag> tags) {
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
        Certificate c = (Certificate) obj;
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
