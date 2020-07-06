package com.epam.esm.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = false)
@Data
@NoArgsConstructor
@Entity
//@Cacheable
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Order extends Identifiable<Long> {

    private String description;
    private BigDecimal totalCost;
    @Column(updatable = false)
    @CreationTimestamp
    private Instant created;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
    @Column(columnDefinition = "boolean default false")
    private boolean completed;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "order_certificate",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id"))
    private Set<Certificate> certificates = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (deleted != order.deleted) return false;
        if (completed != order.completed) return false;
        if (!description.equals(order.description)) return false;
        if (!totalCost.equals(order.totalCost)) return false;
        return created.equals(order.created);
    }

    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + totalCost.hashCode();
        result = 31 * result + created.hashCode();
        result = 31 * result + (deleted ? 1 : 0);
        result = 31 * result + (completed ? 1 : 0);
        return result;
    }
}
