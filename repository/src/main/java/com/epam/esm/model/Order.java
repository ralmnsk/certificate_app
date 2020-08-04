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
@Table(name = "orders")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Order extends Identifiable<Long> {

    private String description;
    private BigDecimal totalCost;
//    @Column(updatable = false)
    private Timestamp created;

    @Column(columnDefinition = "boolean default false")
    private boolean completed;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "order_certificate",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "certificate_id"))
    private Set<Certificate> certificates = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Order order = (Order) o;

        if (isDeleted() != order.isDeleted()) return false;
        if (completed != order.completed) return false;
        if (!description.equals(order.description)) return false;
        return created.equals(order.created);
    }

    @Override
    public int hashCode() {
        int result = description.hashCode();
        result = 31 * result + created.hashCode();
        result = 31 * result + (isDeleted() ? 1 : 0);
        result = 31 * result + (completed ? 1 : 0);
        return result;
    }
}
