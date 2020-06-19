package com.epam.esm.model;


import java.util.ArrayList;
import java.util.List;

/**
 * The type Filter.
 */
public class Filter {

    private String tagName;
    private String name;
    private int page;
    private int size;
    private List<String> sort = new ArrayList<>();
    private boolean isCount;

    /**
     * Gets tag name.
     *
     * @return the tag name
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * Sets tag name.
     *
     * @param tagName the tag name
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
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
     * Gets page.
     *
     * @return the page
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets page.
     *
     * @param page the page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Gets size.
     *
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets sort.
     *
     * @return the sort
     */
    public List<String> getSort() {
        return sort;
    }

    /**
     * Sets sort.
     *
     * @param sort the sort
     */
    public void setSort(List<String> sort) {
        this.sort = sort;
    }

    /**
     * Is count boolean.
     *
     * @return the boolean
     */
    public boolean isCount() {
        return isCount;
    }

    /**
     * Sets count.
     *
     * @param count the count
     */
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
