package com.walgreens.rxi.purchasing.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.walgreens.rxi.purchasing.domain.enumeration.LocationType;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

/**
 * A Location.
 */
@Entity
@Table(name = "location")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Location implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Type(type = "uuid-char")
    @Column(name = "location_number", length = 36, nullable = false)
    private UUID locationNumber;

    @NotNull
    @Column(name = "location_name", nullable = false)
    private String locationName;

    @Column(name = "location")
    private String location;

    @Column(name = "city")
    private String city;

    @Column(name = "state")
    private String state;

    @Column(name = "county")
    private String county;

    @NotNull
    @Column(name = "phone_number", precision = 21, scale = 2, nullable = false)
    private BigDecimal phoneNumber;

    @NotNull
    @Column(name = "pharmacy_hours", nullable = false)
    private String pharmacyHours;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private LocationType type;

    @JsonIgnoreProperties(value = { "location" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Address address;

    @OneToMany(mappedBy = "location")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "orderItems", "location" }, allowSetters = true)
    private Set<LocationOrder> orders = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Location id(Long id) {
        this.id = id;
        return this;
    }

    public UUID getLocationNumber() {
        return this.locationNumber;
    }

    public Location locationNumber(UUID locationNumber) {
        this.locationNumber = locationNumber;
        return this;
    }

    public void setLocationNumber(UUID locationNumber) {
        this.locationNumber = locationNumber;
    }

    public String getLocationName() {
        return this.locationName;
    }

    public Location locationName(String locationName) {
        this.locationName = locationName;
        return this;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocation() {
        return this.location;
    }

    public Location location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return this.city;
    }

    public Location city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return this.state;
    }

    public Location state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCounty() {
        return this.county;
    }

    public Location county(String county) {
        this.county = county;
        return this;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public BigDecimal getPhoneNumber() {
        return this.phoneNumber;
    }

    public Location phoneNumber(BigDecimal phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(BigDecimal phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPharmacyHours() {
        return this.pharmacyHours;
    }

    public Location pharmacyHours(String pharmacyHours) {
        this.pharmacyHours = pharmacyHours;
        return this;
    }

    public void setPharmacyHours(String pharmacyHours) {
        this.pharmacyHours = pharmacyHours;
    }

    public LocationType getType() {
        return this.type;
    }

    public Location type(LocationType type) {
        this.type = type;
        return this;
    }

    public void setType(LocationType type) {
        this.type = type;
    }

    public Address getAddress() {
        return this.address;
    }

    public Location address(Address address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Set<LocationOrder> getOrders() {
        return this.orders;
    }

    public Location orders(Set<LocationOrder> locationOrders) {
        this.setOrders(locationOrders);
        return this;
    }

    public Location addOrder(LocationOrder locationOrder) {
        this.orders.add(locationOrder);
        locationOrder.setLocation(this);
        return this;
    }

    public Location removeOrder(LocationOrder locationOrder) {
        this.orders.remove(locationOrder);
        locationOrder.setLocation(null);
        return this;
    }

    public void setOrders(Set<LocationOrder> locationOrders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setLocation(null));
        }
        if (locationOrders != null) {
            locationOrders.forEach(i -> i.setLocation(this));
        }
        this.orders = locationOrders;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return id != null && id.equals(((Location) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Location{" +
            "id=" + getId() +
            ", locationNumber='" + getLocationNumber() + "'" +
            ", locationName='" + getLocationName() + "'" +
            ", location='" + getLocation() + "'" +
            ", city='" + getCity() + "'" +
            ", state='" + getState() + "'" +
            ", county='" + getCounty() + "'" +
            ", phoneNumber=" + getPhoneNumber() +
            ", pharmacyHours='" + getPharmacyHours() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
