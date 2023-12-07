package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@RestController
@RequestMapping("interview/v1/customer")
@NoArgsConstructor
public class CustomerController {
    private CustomerService customerService;

    @Autowired
    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Get customer by Id
    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable(value = "id") Long id) {
        Customer customer = customerService.getCustomerById(id);

        if (customer == null) {
            return new ResponseEntity<>("Can't find customer by id " + id + "!", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(customer, HttpStatus.OK);
        }
    }

    // Check weather the input customer is valid.
    // If it is valid then save customer to database
    @PostMapping()
    public ResponseEntity<?> createCustomer(@RequestBody @Valid Customer customer) {
        CompletableFuture<Customer> customerFuture = customerService.processAsyncValidations(customer);

        try {
            Customer savedCustomer = customerFuture.join();

            return new ResponseEntity<>(savedCustomer, HttpStatus.OK);
        } catch (CompletionException exception) {
            System.err.println("Error processing customer: " + exception.getMessage());
        }

        return new ResponseEntity<>("Input customer is not valid!", HttpStatus.BAD_REQUEST);
    }
}
