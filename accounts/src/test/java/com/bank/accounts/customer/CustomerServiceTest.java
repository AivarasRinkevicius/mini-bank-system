package com.bank.accounts.customer;

import com.bank.accounts.account.service.AccountService;
import com.bank.accounts.address.model.Address;
import com.bank.accounts.address.repository.AddressRepository;
import com.bank.accounts.customer.dto.CustomerDto;
import com.bank.accounts.customer.dto.CustomerSearchDto;
import com.bank.accounts.customer.dto.UpdateCustomerDto;
import com.bank.accounts.customer.model.Customer;
import com.bank.accounts.customer.repository.CustomerRepository;
import com.bank.accounts.customer.service.CustomerService;
import com.bank.accounts.customer.service.mapper.CustomerMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.bank.accounts.common.Constants.CustomerType.PUBLIC;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AccountService accountService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private static final String USERNAME = "admin";
    private static final long CUSTOMER_ID = 2L;        ;
    private static final CustomerDto MOCK_CUSTOMER_DTO =
            CustomerDto.builder().customerType(PUBLIC)
                    .username(USERNAME)
                    .name("Vardas")
                    .lastName("Pavarde")
                    .email("pastas@gmail.com")
                    .phoneNumber("+3700000001")
                    .address(new ArrayList<>())
                    .accountId(1L)
                    .build();
    private static final Customer MOCK_CUSTOMER =
            Customer.builder().id(2L).customerType(PUBLIC)
                    .name("Vardas")
                    .lastName("Pavarde")
                    .email("pastas@gmail.com")
                    .phoneNumber("+3700000001")
                    .address(new ArrayList<>())
                    .build();
    private static final UpdateCustomerDto MOCK_UPDATE_CUSTOMER_DTO =
            UpdateCustomerDto.builder()
                    .username(USERNAME)
                    .customerType(PUBLIC)
                    .name("Vardas")
                    .lastName("Pavarde")
                    .email("pastas@gmail.com")
                    .phoneNumber("+3700000001")
                    .address(new ArrayList<>(List.of(Address.builder()
                            .city("Vilnius").country("Lithuania")
                            .buildingNumber(1).build())))
                    .build();

    @Test
    void shouldCreateCustomer() {
        when(customerMapper.toCustomer(MOCK_CUSTOMER_DTO)).thenReturn(MOCK_CUSTOMER);
        when(customerRepository.existsByNameAndLastNameAndPhoneNumberAndEmail(
                any(), any(), any(), any())).thenReturn(false);
        when(customerRepository.save(any())).thenReturn(MOCK_CUSTOMER);

        Customer result = customerService.create(MOCK_CUSTOMER_DTO);

        assertEquals(MOCK_CUSTOMER, result);
        verify(addressRepository, times(MOCK_CUSTOMER_DTO.address().size())).save(any());
        verify(accountService).assignCustomer(any(), anyLong(), anyString());
    }

    @Test
    void shouldThrowEntityNotFoundException_WhenCustomerIsDuplicated() {
        when(customerRepository.existsByNameAndLastNameAndPhoneNumberAndEmail(
                any(), any(), any(), any())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> customerService.create(MOCK_CUSTOMER_DTO));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void shouldUpdateCustomer() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(MOCK_CUSTOMER));
        when(customerRepository.save(any())).thenReturn(MOCK_CUSTOMER);

        Customer result = customerService.update(CUSTOMER_ID, MOCK_UPDATE_CUSTOMER_DTO);

        assertNotNull(result);
        assertEquals(USERNAME, result.getLastModifiedBy());
        verify(customerRepository).save(MOCK_CUSTOMER);
    }


    @Test
    void shouldThrowEntityNotFoundException_WhenCustomerNotFound() {
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> customerService.update(CUSTOMER_ID, MOCK_UPDATE_CUSTOMER_DTO));
    }


    @Test
    void shouldFindCustomerBySearchTerm() {
        String searchTerm = "anySearchKeyword";
        Page<Customer> mockPage = new PageImpl<>(List.of(MOCK_CUSTOMER));
        when(customerRepository.findCustomers(eq(searchTerm), any(Pageable.class))).thenReturn(mockPage);

        CustomerSearchDto result = customerService.searchCustomers(searchTerm, 0, 10);

        assertEquals(1, result.getCustomers().size());
        assertEquals(1, result.getTotalCount());
    }

    @Test
    void shouldThrowEntityExistsException_WhenThereCreatingNonNonUniqueCustomer() {
        when(customerRepository.existsByNameAndLastNameAndPhoneNumberAndEmail(
                any(), any(), any(), any())).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> customerService.create(MOCK_CUSTOMER_DTO));
    }

    @Test
    void shouldSaveNewAddress() {
        // Arrange
        when(customerRepository.findById(CUSTOMER_ID)).thenReturn(Optional.of(MOCK_CUSTOMER));

        // Act
        customerService.update(CUSTOMER_ID, MOCK_UPDATE_CUSTOMER_DTO);

        // Assert
        assertEquals(1, MOCK_CUSTOMER.getAddress().size());
    }
}