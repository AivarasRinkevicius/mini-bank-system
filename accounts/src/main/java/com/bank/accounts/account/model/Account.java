package com.bank.accounts.account.model;

import com.bank.accounts.common.BaseEntity;
import com.bank.accounts.customer.model.Customer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.envers.Audited;

import java.util.List;

//@Formula("SELECT COUNT(*) from account left join customer on account.id")
//TODO int or Integer
@Entity
@Audited
@SuperBuilder
@NoArgsConstructor
@Getter
public class Account extends BaseEntity {

    public int numberOfOwners;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Customer> customers;
}
