package com.walgreens.rxi.purchasing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walgreens.rxi.purchasing.domain.enumeration.OrderStatus;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A LocationOrder.
 */
@Entity
@Table(name = "location_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class LocationOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Type(type = "uuid-char")
    @Column(name = "code", length = 36, nullable = false)
    private UUID code;

    @NotNull
    @Column(name = "placed_date", nullable = false)
    private Instant placedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "invoice_id")
    private Long invoiceId;

    @OneToMany(mappedBy = "locationOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "locationOrder" }, allowSetters = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "address", "orders" }, allowSetters = true)
    private Location location;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocationOrder id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getCode() {
        return this.code;
    }

    public LocationOrder code(UUID code) {
        this.code = code;
        return this;
    }

    public void setCode(UUID code) {
        this.code = code;
    }

    public Instant getPlacedDate() {
        return this.placedDate;
    }

    public LocationOrder placedDate(Instant placedDate) {
        this.placedDate = placedDate;
        return this;
    }

    public void setPlacedDate(Instant placedDate) {
        this.placedDate = placedDate;
    }

    public OrderStatus getStatus() {
        return this.status;
    }

    public LocationOrder status(OrderStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getInvoiceId() {
        return this.invoiceId;
    }

    public LocationOrder invoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
        return this;
    }

    public void setInvoiceId(Long invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Set<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public LocationOrder orderItems(Set<OrderItem> orderItems) {
        this.setOrderItems(orderItems);
        return this;
    }

    public LocationOrder addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setLocationOrder(this);
        return this;
    }

    public LocationOrder removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setLocationOrder(null);
        return this;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        if (this.orderItems != null) {
            this.orderItems.forEach(i -> i.setLocationOrder(null));
        }
        if (orderItems != null) {
            orderItems.forEach(i -> i.setLocationOrder(this));
        }
        this.orderItems = orderItems;
    }

    public Location getLocation() {
        return this.location;
    }

    public LocationOrder location(Location location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocationOrder)) {
            return false;
        }
        return id != null && id.equals(((LocationOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocationOrder{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", placedDate='" + getPlacedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", invoiceId=" + getInvoiceId() +
            "}";
    }
}
