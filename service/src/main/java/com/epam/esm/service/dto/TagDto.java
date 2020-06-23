package com.epam.esm.service.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * The type Tag dto.
 * The TagDto is a DTO used to transfer
 * an tag data between software application subsystems or
 * layers.
 */
public class TagDto extends Dto<Integer> {
    @NotNull
    @Size(min = 2, max = 128, message
            = "Name must be between 2 and 128 characters")
    private String name;

    /**
     * Instantiates a new Tag dto.
     */
    public TagDto() {
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
        TagDto t = (TagDto) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.name, t.getName())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(name)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "TagDto{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                '}';
    }
}
