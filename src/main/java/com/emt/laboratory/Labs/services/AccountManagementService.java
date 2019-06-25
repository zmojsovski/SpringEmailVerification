package com.emt.laboratory.Labs.services;

import com.emt.laboratory.Labs.models.Profile;
import com.emt.laboratory.Labs.models.Role;
import com.emt.laboratory.Labs.models.exceptions.*;

public interface AccountManagementService {

    Profile RegisterAccount(String email,String username, String password)
            throws EmailValidationException,PasswordNotValidException;

    Profile LogInToAccount(String username,String password,boolean rememberMe)
            throws UsernameNotFoundException, PasswordIncorrectException;

    Profile ResetForgottenPassword(String email)
            throws EmailNotFoundException,PasswordIncorrectException,EmailValidationException;

    Profile EditAccountData(String username,String password,String newUsername, String newEmail, String newPassword)
            throws UsernameNotFoundException,PasswordIncorrectException;

    Profile ChangeAccountRole(Profile profile)
            throws ProfileNotFoundException,PasswordIncorrectException;

    Profile ActivateProfile(String email) throws EmailNotFoundException;

    Profile expiredProfile(long tokenid) throws TokenNotFoundException;

    Boolean DeleteAccount(Profile profile)
            throws  ProfileNotFoundException,PasswordIncorrectException;




}
