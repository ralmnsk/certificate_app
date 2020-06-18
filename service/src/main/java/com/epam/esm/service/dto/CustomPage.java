package com.epam.esm.service.dto;

import com.epam.esm.service.view.Profile;
import com.fasterxml.jackson.annotation.JsonView;

import java.util.ArrayList;
import java.util.List;

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

    public CustomPage() {
    }

    public E getNumber() {
        return number;
    }

    public void setNumber(E number) {
        this.number = number;
    }

    public E getSize() {
        return size;
    }

    public void setSize(E size) {
        this.size = size;
    }

    public E getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(E totalElements) {
        this.totalElements = totalElements;
    }

    public E getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(E totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getList() {
        return list;
    }

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
