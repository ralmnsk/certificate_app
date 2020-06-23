package com.epam.esm.model;


import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    private String tagName;
    private String name;
    private int page;
    private int size;
    private List<String> sortParams = new ArrayList<>();
    private boolean isTurnCountingOn;

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

    public List<String> getSortParams() {
        return sortParams;
    }

    public void setSortParams(List<String> sortParams) {
        this.sortParams = sortParams;
    }

    public boolean isTurnCountingOn() {
        return isTurnCountingOn;
    }

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
