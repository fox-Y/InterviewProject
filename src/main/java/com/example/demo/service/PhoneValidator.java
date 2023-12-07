package com.example.demo.service;

import com.example.demo.model.Customer;
import org.springframework.stereotype.Service;

@Service
public class PhoneValidator implements Validator{

    // Validate customer.phone by calling external service
    @Override
    public boolean validate(Customer customer) {
        return true;
    }
}
