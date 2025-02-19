package com.bank.accounts.account.dto;

import com.bank.accounts.customer.model.Customer;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@Builder
public record AccountDto(@NonNull List<Customer> customers, @NonNull String username) {
}

