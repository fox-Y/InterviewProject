package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @Test
    void testCreateCustomer() throws Exception {
        // Mock data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy").withLocale(new Locale("English", "United States"));
        Customer customer = new Customer(1L, "Jacky", "555-55-5555",
                "555-555-5555", "jacky@gmail.com", LocalDate.parse("05/05/2004", formatter));

        // Mock the behavior of the customerService
        when(customerService.processAsyncValidations(eq(customer)))
                .thenReturn(CompletableFuture.completedFuture(customer));

        // Call the method under test
        ResponseEntity<?> responseEntity = customerController.createCustomer(customer);

        // Assert the response status code
        assertEquals(200, responseEntity.getStatusCodeValue());

        // Assert the response body
        assertEquals(customer, responseEntity.getBody());
    }
}