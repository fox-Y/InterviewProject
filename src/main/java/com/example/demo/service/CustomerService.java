package com.example.demo.service;

import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.concurrent.CompletableFuture;

@Service
@EnableAsync
public class CustomerService {
    private final CustomerRepository customerRepository;

    private final SsnValidator ssnValidator;

    private final PhoneValidator phoneValidator;

    private final EmailValidator emailValidator;


    @Autowired
    CustomerService(CustomerRepository customerRepository, SsnValidator ssnValidator, PhoneValidator phoneValidator, EmailValidator emailValidator) {
        this.customerRepository = customerRepository;
        this.ssnValidator = ssnValidator;
        this.phoneValidator = phoneValidator;
        this.emailValidator = emailValidator;
    }

    // Make 3 dummy calls to 3 backend services (SSN Validation, Phone Validation and Email Validation) asynchronous calls
    @Async
    public CompletableFuture<Customer> processAsyncValidations(Customer customer) {

        // Call the fake services to check weather the ssn, phone and, email is valid
        CompletableFuture<Boolean> ssnValidationFuture = CompletableFuture.supplyAsync(
                () -> ssnValidator.validate(customer));

        CompletableFuture<Boolean> phoneValidationFuture = CompletableFuture.supplyAsync(
                () -> phoneValidator.validate(customer));

        CompletableFuture<Boolean> emailValidationFuture = CompletableFuture.supplyAsync(
                () -> emailValidator.validate(customer));

        CompletableFuture<Void> allOf = CompletableFuture.allOf(
                ssnValidationFuture, phoneValidationFuture, emailValidationFuture);


        return allOf.thenApplyAsync(ignored -> {
            // Retrieve the results of the asynchronous calls
            boolean ssnValid = ssnValidationFuture.join();
            boolean phoneValid = phoneValidationFuture.join();
            boolean emailValid = emailValidationFuture.join();
            boolean ageValid = validateAge(customer.getBirthday());

            if (ssnValid && phoneValid && emailValid && ageValid) {
                return customerRepository.save(customer);
            } else {
                throw new ValidationException("Customer creation failed due to validation errors.");
            }
        });
    }

    // Check if the age is larger than 18
    private boolean validateAge(LocalDate dob) {
        LocalDate curDate = LocalDate.now();

        int age = Period.between(dob, curDate).getYears();

        return age > 18;
    }


    // Get customer from database by id
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }
}
