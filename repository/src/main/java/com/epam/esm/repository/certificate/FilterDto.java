package com.epam.esm.repository.certificate;


import java.util.ArrayList;
import java.util.List;

public class FilterDto {

    private String tagName;            // filter by tag name
    private String name;               //search by certificate name
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
}
