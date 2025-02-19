package com.bank.accounts.account;

import com.bank.accounts.account.dto.AccountDto;
import com.bank.accounts.account.model.Account;
import com.bank.accounts.account.repository.AccountRepository;
import com.bank.accounts.account.service.AccountService;
import com.bank.accounts.customer.model.Customer;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bank.accounts.common.Constants.CustomerType.PUBLIC;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private static final String USERNAME = "admin";
    private static final Customer MOCK_CUSTOMER =
            Customer.builder().id(10L).customerType(PUBLIC)
            .name("Vardas")
            .lastName("Pavarde")
            .email("pastas@gmail.com")
            .phoneNumber("+3700000001")
            .build();
    private static final long ACCOUNT_ID = 1L;
    private static final Account MOCK_ACCOUNT =
            Account.builder().customers(new ArrayList<>()).numberOfOwners(0).id(ACCOUNT_ID).build();
    private static final Account MOCK_ACCOUNT_2 =
            Account.builder().customers(new ArrayList<>(List.of(MOCK_CUSTOMER)))
                    .numberOfOwners(1).id(ACCOUNT_ID).build();

    @Test
    void shouldCreateAccount() {
        List<Customer> customers = List.of(new Customer(), new Customer(), new Customer());
        AccountDto mockAccountDto = AccountDto.builder().username(USERNAME).customers(customers).build();
        Account expectedAccount = Account.builder()
                .createdBy(USERNAME)
                .customers(customers)
                .numberOfOwners(2)
                .build();

        when(accountRepository.save(any(Account.class))).thenReturn(expectedAccount);

        Account result = accountService.create(mockAccountDto);

        assertNotNull(result);
        assertEquals(2, result.getNumberOfOwners());
        assertEquals(USERNAME, result.getCreatedBy());
        assertEquals(customers, result.getCustomers());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void shouldAssignCustomer() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(MOCK_ACCOUNT));
        when(accountRepository.save(any(Account.class))).thenReturn(MOCK_ACCOUNT);

        accountService.assignCustomer(MOCK_CUSTOMER, ACCOUNT_ID, USERNAME);

        assertTrue(MOCK_ACCOUNT.getCustomers().contains(MOCK_CUSTOMER));
        assertEquals(1, MOCK_ACCOUNT.getNumberOfOwners());
        assertEquals(USERNAME, MOCK_ACCOUNT.getLastModifiedBy());
        verify(accountRepository, times(1)).save(MOCK_ACCOUNT);
    }

    @Test
    void shouldThrowIllegalArgumentException_WhenCustomerAlreadyAssignedToAccount() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(MOCK_ACCOUNT_2));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> accountService.assignCustomer(MOCK_CUSTOMER, ACCOUNT_ID, USERNAME));

        assertTrue(exception.getMessage().contains("already assigned"));
        verify(accountRepository, never()).save(any());
    }

    @Test
    void shouldReturnAccount() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.of(MOCK_ACCOUNT));

        Account result = accountService.getById(ACCOUNT_ID);

        assertSame(MOCK_ACCOUNT, result);
    }

    @Test
    void shouldThrowEntityNotFoundException_WhenAccountDoesNotExist() {
        when(accountRepository.findById(ACCOUNT_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> accountService.getById(ACCOUNT_ID));

        assertTrue(exception.getMessage().contains("not found"));
    }
}