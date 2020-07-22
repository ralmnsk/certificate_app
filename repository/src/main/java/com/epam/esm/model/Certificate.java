package com.epam.esm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
@Cacheable
@Table(name = "certificate")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Certificate extends Identifiable<Long> {

    private String name;
    private String description;
    private BigDecimal price;
    @Column(updatable = false)
    private Timestamp creation;
    private Timestamp modification;
    private Integer duration;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "cert_tag",
            joinColumns = @JoinColumn(name = "certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Certificate that = (Certificate) o;

        if (deleted != that.deleted) return false;
        if (!name.equals(that.name)) return false;
        if (!description.equals(that.description)) return false;
        if (!price.equals(that.price)) return false;
        return duration.equals(that.duration);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + duration.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        return result;
    }
}
