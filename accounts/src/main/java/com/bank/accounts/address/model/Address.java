package com.bank.accounts.address.model;

import com.bank.accounts.common.BaseEntity;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.util.Objects;

@Entity
@Audited
@SuperBuilder
@Getter
@NoArgsConstructor
public class Address extends BaseEntity {
    String country;
    String city;
    String street;
    int buildingNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o instanceof Address other
                && buildingNumber == other.buildingNumber
                && Objects.equals(country, other.country)
                && Objects.equals(city, other.city)
                && Objects.equals(street, other.street);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, street, buildingNumber);
    }
}