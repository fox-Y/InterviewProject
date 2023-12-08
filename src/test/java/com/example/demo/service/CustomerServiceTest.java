package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private SsnValidator ssnValidator;

    @Mock
    private PhoneValidator phoneValidator;

    @Mock
    private EmailValidator emailValidator;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void testProcessAsyncValidations() {
        // Mock data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy").withLocale(new Locale("English", "United States"));
        Customer customer = new Customer(1L, "Jacky", "555-55-5555",
                "555-555-5555", "jacky@gmail.com", LocalDate.parse("05/05/2004", formatter));

        // Mock the behavior of validation clients
        when(ssnValidator.validate(customer)).thenReturn(true);
        when(phoneValidator.validate(customer)).thenReturn(true);
        when(emailValidator.validate(customer)).thenReturn(true);

        // Mock the behavior of the repository
        when(customerRepository.save(any())).thenReturn(customer);

        // Call the method under test
        CompletableFuture<Customer> customerFuture = customerService.processAsyncValidations(customer);

        // Wait for the completion of the CompletableFuture and get the result
        Customer savedCustomer = customerFuture.join();

        // Verify that the validation clients were called with the expected arguments
        verify(ssnValidator).validate(eq(customer));
        verify(phoneValidator).validate(eq(customer));
        verify(emailValidator).validate(eq(customer));

        // Verify that the repository's save method was called with the expected argument
        verify(customerRepository).save(any(Customer.class));

        // Assert that the returned customer is not null
        assertNotNull(savedCustomer);
    }

    @Test
    void getCustomerById() {
        // Mock data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy").withLocale(new Locale("English", "United States"));
        Customer customer = new Customer(1L, "Jacky", "555-55-5555",
                "555-555-5555", "jacky@gmail.com", LocalDate.parse("05/05/2004", formatter));

        // Verify the result of getCustomerById()
        when(customerRepository.findById(customer.getId())).thenReturn(Optional.of(customer));

        // When
        customerService.getCustomerById(customer.getId());

        // Verify
        verify(customerRepository).findById(customer.getId());
    }






}