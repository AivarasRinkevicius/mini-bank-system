package com.bank.accounts.customer.dto;

import com.bank.accounts.address.model.Address;
import com.bank.accounts.common.Constants.CustomerType;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record UpdateCustomerDto(@NonNull String username, String name, String lastName,
                                String phoneNumber, String email, CustomerType customerType,
                                List<Address> address) {
}
