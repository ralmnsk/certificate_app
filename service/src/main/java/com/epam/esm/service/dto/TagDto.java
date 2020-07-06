package com.epam.esm.service.dto;

import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
//@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@JsonRootName("tag")
@Relation(collectionRelation = "tags")
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonDeserialize(using = TagDeserializer.class)
public class TagDto extends RepresentationModel<TagDto> implements Serializable {

    private Integer id;

    @Size(max = 128, message
            = "Name must be between 2 and 128 characters")
    private String name;

    private boolean deleted;

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

        if (deleted != tagDto.deleted) return false;
        return name.equals(tagDto.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }
}
