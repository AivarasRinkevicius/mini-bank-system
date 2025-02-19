package com.bank.accounts.account.service;

import com.bank.accounts.account.dto.AccountDto;
import com.bank.accounts.account.model.Account;
import com.bank.accounts.account.repository.AccountRepository;
import com.bank.accounts.customer.model.Customer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;

    public Account create(AccountDto accountDto) {
        List<Customer> customers = accountDto.customers();
        Account account = Account.builder()
                .numberOfOwners(customers.size())
                .customers(customers)
                .createdBy(accountDto.username()).build();
        return accountRepository.save(account);
    }

    public void assignCustomer(Customer customer, Long accountId, String username) {
        Account account = validAccount(customer, accountId);

        account.lastModifiedBy = username;
        account.getCustomers().add(customer);
        account.numberOfOwners = account.getNumberOfOwners() + 1;
        accountRepository.save(account);
    }

    public Account getById(long id) {
        return accountRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(format("Account with id: %s not found", id)));
    }

    private Account validAccount(Customer customer, Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(
                () -> new EntityNotFoundException(format("Account with id: %s not found", accountId)));

        if (account.getCustomers().stream().anyMatch(accountCustomer ->
                accountCustomer.getName().equals(customer.getName())
                        && accountCustomer.getLastName().equals(customer.getLastName())
                        && accountCustomer.getEmail().equals(customer.getEmail())
                        && accountCustomer.getPhoneNumber().equals(customer.getPhoneNumber()))) {
            throw new IllegalArgumentException(
                    format("Customer: %s %s is already assigned to this account id %s.", customer.getName(),
                            customer.getLastName(), accountId));
        }

        return account;
    }
}
