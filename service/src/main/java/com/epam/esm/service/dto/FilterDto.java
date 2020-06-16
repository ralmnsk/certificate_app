package com.epam.esm.service.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.List;

public class FilterDto {

    private String tagName;
    private String name;
    @Max(value = 1000000)
    @Min(value = 1)
    private int page;
    @Max(value = 100)
    @Min(value =1)
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

        FilterDto filterDto = (FilterDto) o;

        if (page != filterDto.page) return false;
        if (size != filterDto.size) return false;
        if (isCount != filterDto.isCount) return false;
        if (tagName != null ? !tagName.equals(filterDto.tagName) : filterDto.tagName != null) return false;
        return name != null ? name.equals(filterDto.name) : filterDto.name == null;
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
        return "FilterDto{" +
                "tagName='" + tagName + '\'' +
                ", name='" + name + '\'' +
                ", page=" + page +
                ", size=" + size +
                ", sort=" + sort +
                ", isCount=" + isCount +
                '}';
    }
}
