package com.bank.accounts.address.repository;

import com.bank.accounts.address.model.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
