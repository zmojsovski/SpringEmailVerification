package com.emt.laboratory.Labs.services.impl;

import com.emt.laboratory.Labs.services.EmailValidator;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EmailValidatorImpl implements EmailValidator {
    @Override
    public boolean isValid(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
