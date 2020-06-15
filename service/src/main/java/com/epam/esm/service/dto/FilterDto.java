package com.epam.esm.service.dto;


import java.util.ArrayList;
import java.util.List;

public class FilterDto {

    private String tagName;
    private String name;
    private String page;
    private String size;
    private String sort;
    private List<String> params = new ArrayList<>();
    private int pageInt;
    private int sizeInt;

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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> params) {
        this.params = params;
    }

    public int getPageInt() {
        return pageInt;
    }

    public void setPageInt(int pageInt) {
        this.pageInt = pageInt;
    }

    public int getSizeInt() {
        return sizeInt;
    }

    public void setSizeInt(int sizeInt) {
        this.sizeInt = sizeInt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FilterDto filter = (FilterDto) o;

        if (pageInt != filter.pageInt) return false;
        if (sizeInt != filter.sizeInt) return false;
        if (tagName != null ? !tagName.equals(filter.tagName) : filter.tagName != null) return false;
        if (name != null ? !name.equals(filter.name) : filter.name != null) return false;
        if (page != null ? !page.equals(filter.page) : filter.page != null) return false;
        if (size != null ? !size.equals(filter.size) : filter.size != null) return false;
        return sort != null ? sort.equals(filter.sort) : filter.sort == null;
    }

    @Override
    public int hashCode() {
        int result = tagName != null ? tagName.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (page != null ? page.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (sort != null ? sort.hashCode() : 0);
        result = 31 * result + pageInt;
        result = 31 * result + sizeInt;
        return result;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "tagName='" + tagName + '\'' +
                ", name='" + name + '\'' +
                ", page='" + page + '\'' +
                ", size='" + size + '\'' +
                ", sort='" + sort + '\'' +
                ", params=" + params +
                ", pageInt=" + pageInt +
                ", sizeInt=" + sizeInt +
                '}';
    }
}
