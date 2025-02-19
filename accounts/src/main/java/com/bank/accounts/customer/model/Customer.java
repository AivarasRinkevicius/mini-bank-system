package com.bank.accounts.customer.model;

import com.bank.accounts.address.model.Address;
import com.bank.accounts.common.BaseEntity;
import com.bank.accounts.common.Constants.CustomerType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Entity
@Audited
@SuperBuilder
@NoArgsConstructor
@Getter
@Setter
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "lastName", "phoneNumber", "email"})
})
public class Customer extends BaseEntity {
    CustomerType customerType;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    List<Address> address = new ArrayList<>();
    @NonNull
    private String name;
    @NonNull
    private String lastName;
    @NonNull
    private String phoneNumber;
    @NonNull
    private String email;

    public void setAddress(List<Address> addresses, String username) {
        if(!this.address.isEmpty()){
            this.address.clear();
        }

        addresses.forEach(address -> address.setCreatedBy(username));

        this.address.addAll(addresses);
    }
}
