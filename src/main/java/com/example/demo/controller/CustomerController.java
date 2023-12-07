package com.example.demo.controller;

import com.example.demo.model.Customer;
import com.example.demo.service.CustomerService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.rmi.ServerException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Slf4j
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

        try {
            CompletableFuture<Customer> customerFuture = customerService.processAsyncValidations(customer);

            Customer savedCustomer = customerFuture.join();

            return new ResponseEntity<>(savedCustomer, HttpStatus.OK);
        } catch (CompletionException e) {
            log.info("Validation process failed!" + e.getMessage());

            return new ResponseEntity<>("Validation process failed!", HttpStatus.BAD_REQUEST);
        }

    }
}
