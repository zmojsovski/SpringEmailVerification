package com.emt.laboratory.Labs.models.Validators;

public class EmailValidator {
    private String email;

    public EmailValidator(String email) {
        this.email = email;
    }

    public boolean isValid()
    {
        if(email.contains("@")&& email.contains("."))
            return true;

        return false;
    }

}
