package com.epam.esm.service.dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Custom page.
 *
 * @param <T> the type parameter
 * @param <E> the type parameter
 */
public class CustomPage<T, E extends Number> implements Serializable {
    private List<T> list = new ArrayList<>();

    private E number;

    private E size;

    private E totalElements;

    private E totalPages;

    /**
     * Instantiates a new Custom page.
     */
    public CustomPage() {
    }

    /**
     * Gets number.
     *
     * @return the number
     */
    public E getNumber() {
        return number;
    }

    /**
     * Sets number.
     *
     * @param number the number
     */
    public void setNumber(E number) {
        this.number = number;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public E getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(E size) {
        this.size = size;
    }

    /**
     * Gets total elements.
     *
     * @return the total elements
     */
    public E getTotalElements() {
        return totalElements;
    }

    /**
     * Sets total elements.
     *
     * @param totalElements the total elements
     */
    public void setTotalElements(E totalElements) {
        this.totalElements = totalElements;
    }

    /**
     * Gets total pages.
     *
     * @return the total pages
     */
    public E getTotalPages() {
        return totalPages;
    }

    /**
     * Sets total pages.
     *
     * @param totalPages the total pages
     */
    public void setTotalPages(E totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Gets list.
     *
     * @return the list
     */
    public List<T> getList() {
        return list;
    }

    /**
     * Sets list.
     *
     * @param list the list
     */
    public void setList(List<T> list) {
        this.list = list;
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
        CustomPage c = (CustomPage) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.number, c.getNumber())
                .append(this.size, c.getSize())
                .append(this.totalElements, c.getTotalElements())
                .append(this.totalPages, c.getTotalPages())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(number)
                .append(size)
                .append(totalElements)
                .append(totalPages)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "CustomPage{" +
                "number=" + number +
                ", size=" + size +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                '}';
    }
}
