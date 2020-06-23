package com.epam.esm.model;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Filter.
 * The Filter is used for pagination as a property container.
 */
public class Filter {

    private String tagName;
    private String name;
    private int page;
    private int size;
    private List<String> sortParams = new ArrayList<>();
    private boolean isTurnCountingOn;

    /**
     * Gets tag name.
     *
     * @return the tag name for the filtration
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * Sets tag name.
     *
     * @param tagName the tag name for the filtration
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    /**
     * Gets name.
     *
     * @return the name of the certificate for the filtration
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name of the certificate for the filtration
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets page.
     *
     * @return the page number
     */
    public int getPage() {
        return page;
    }

    /**
     * Sets page.
     *
     * @param page the page number
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * Gets size.
     *
     * @return the size that is a count of items on a page
     */
    public int getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size the size that is a count of items on a page
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Gets sort params.
     *
     * @return the sort params are parameters for sorting items on the page
     */
    public List<String> getSortParams() {
        return sortParams;
    }

    /**
     * Sets sort params.
     *
     * @param sortParams the sort params are parameters for sorting items on the page
     */
    public void setSortParams(List<String> sortParams) {
        this.sortParams = sortParams;
    }

    /**
     * This parameter turns on the counting of items when it is set as true.
     *
     * @return the boolean
     */
    public boolean isTurnCountingOn() {
        return isTurnCountingOn;
    }

    /**
     * Sets turn counting on.
     *
     * @param turnCountingOn the turn counting on
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
        Filter f = (Filter) obj;
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
        return "Filter{" +
                "tagName='" + tagName + '\'' +
                ", name='" + name + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", sort=" + sortParams +
                ", isTurnCountingOn=" + isTurnCountingOn +
                '}';
    }
}
