package com.epam.esm.service.dto;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Filter dto.
 */
public class FilterDto {

    private String tagName;

    private String name;

    private int page;

    private int size;
    private List<String> sortParams = new ArrayList<>();
    private boolean isTurnCountingOn;

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
    public List<String> getSortParams() {
        return sortParams;
    }

    /**
     * Sets sort.
     *
     * @param sortParams the sort
     */
    public void setSortParams(List<String> sortParams) {
        this.sortParams = sortParams;
    }

    /**
     * Is count boolean.
     *
     * @return the boolean
     */
    public boolean isTurnCountingOn() {
        return isTurnCountingOn;
    }

    /**
     * Sets count.
     *
     * @param turnCountingOn the count
     */
    public void setTurnCountingOn(boolean turnCountingOn) {
        isTurnCountingOn = turnCountingOn;
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
        FilterDto f = (FilterDto) obj;
        return new EqualsBuilder()
                .appendSuper(super.equals(obj))
                .append(this.tagName, f.getTagName())
                .append(this.name, f.getName())
                .append(this.page, f.getPage())
                .append(this.size, f.getSize())
                .append(this.isTurnCountingOn, f.isTurnCountingOn())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(tagName)
                .append(name)
                .append(page)
                .append(size)
                .append(isTurnCountingOn)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "FilterDto{" +
                "tagName='" + tagName + '\'' +
                ", name='" + name + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", sort=" + sortParams +
                ", isCount=" + isTurnCountingOn +
                '}';
    }
}
