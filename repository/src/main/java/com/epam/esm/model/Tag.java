package com.epam.esm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Entity
@Cacheable
@Table(name = "tag")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Tag extends Identifiable<Integer> {
    @NotNull(message = "name could not be null")
    private String name;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (deleted != tag.deleted) return false;
        return name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }
}
