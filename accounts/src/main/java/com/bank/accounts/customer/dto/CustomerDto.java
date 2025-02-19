package com.bank.accounts.customer.dto;

import com.bank.accounts.address.model.Address;
import com.bank.accounts.common.Constants.CustomerType;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record CustomerDto(@NonNull String username, @NonNull String name, @NonNull String lastName,
                          @NonNull String phoneNumber, @NonNull String email, CustomerType customerType,
                          List<Address> address, @NonNull Long accountId) {
}
