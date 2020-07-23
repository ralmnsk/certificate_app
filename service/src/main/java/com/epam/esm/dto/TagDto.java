package com.epam.esm.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@JsonRootName("tag")
@Relation(collectionRelation = "tags")
public class TagDto extends IdentifiableDto<Integer> {

    @NotNull(message = "Name has to be not null")
    @Size(max = 128, message
            = "Name must be between 2 and 128 characters")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        TagDto tagDto = (TagDto) o;

        if (isDeleted() != tagDto.isDeleted()) return false;
        return name.equals(tagDto.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (isDeleted() ? 1 : 0);
        return result;
    }
}
