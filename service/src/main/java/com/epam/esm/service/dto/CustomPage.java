package com.epam.esm.service.dto;

import com.epam.esm.service.view.Profile;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Custom page.
 *
 * @param <T> the type parameter
 * @param <E> the type parameter
 */
public class CustomPage<T, E extends Number> {
    @JsonView(Profile.PublicView.class)
    private List<T> list = new ArrayList<>();

    @JsonView(Profile.PublicView.class)
    private E number;

    @JsonView(Profile.PublicView.class)
    private E size;

    @JsonView(Profile.PublicView.class)
    private E totalElements;

    @JsonView(Profile.PublicView.class)
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CustomPage<?, ?> that = (CustomPage<?, ?>) o;

        if (!number.equals(that.number)) return false;
        if (!size.equals(that.size)) return false;
        if (!totalElements.equals(that.totalElements)) return false;
        return totalPages.equals(that.totalPages);
    }

    @Override
    public int hashCode() {
        int result = number.hashCode();
        result = 31 * result + size.hashCode();
        result = 31 * result + totalElements.hashCode();
        result = 31 * result + totalPages.hashCode();
        return result;
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
