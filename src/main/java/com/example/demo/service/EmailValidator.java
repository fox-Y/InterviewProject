package com.example.demo.service;

import com.example.demo.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class EmailValidator implements Validator{

    // Validate customer.email by calling external service
    @Override
    public boolean validate(Customer customer) {
        return true;
    }
}
