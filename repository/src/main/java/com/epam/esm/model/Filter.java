package com.epam.esm.model;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

public class Filter {

    private String tagName;
    private String name;
    private int page;
    private int size;
    private List<String> sort = new ArrayList<>();
    private boolean isCount;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getSort() {
        return sort;
    }

    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    public boolean isCount() {
        return isCount;
    }

    public void setCount(boolean count) {
        isCount = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filter filter = (Filter) o;

        if (page != filter.page) return false;
        if (size != filter.size) return false;
        if (isCount != filter.isCount) return false;
        if (tagName != null ? !tagName.equals(filter.tagName) : filter.tagName != null) return false;
        return name != null ? name.equals(filter.name) : filter.name == null;
    }

    @Override
    public int hashCode() {
        int result = tagName != null ? tagName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + page;
        result = 31 * result + size;
        result = 31 * result + (isCount ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "tagName='" + tagName + '\'' +
                ", name='" + name + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", sort=" + sort +
                ", isCount=" + isCount +
                '}';
    }
}
