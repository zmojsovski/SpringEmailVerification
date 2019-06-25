package com.emt.laboratory.Labs.services.impl;

import com.emt.laboratory.Labs.models.ConfirmationToken;
import com.emt.laboratory.Labs.models.Profile;
import com.emt.laboratory.Labs.models.exceptions.*;
import com.emt.laboratory.Labs.repositoryJPA.ConfirmationTokenRepository;
import com.emt.laboratory.Labs.repositoryJPA.ProfileRepository;
import com.emt.laboratory.Labs.repositoryJPA.mail.ConfirmationTokenRepositoryID;
import com.emt.laboratory.Labs.services.AccountManagementService;
import com.emt.laboratory.Labs.services.EmailValidator;
import com.emt.laboratory.Labs.services.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountManagementServiceImpl implements AccountManagementService {


    private final ProfileRepository profileRepository;

    private final EmailValidator emailValidator;

    private final ConfirmationTokenRepositoryID confirmationTokenRepository;

    private final PasswordEncoder passwordEncoder;

    @Value("${profile_validation_expiry_in_hours}")
    private int CodeActivationExpiryTime;


    public AccountManagementServiceImpl(ProfileRepository profileRepository, EmailValidator emailValidator, ConfirmationTokenRepositoryID confirmationTokenRepository,PasswordEncoder passwordEncoder) {
        this.profileRepository = profileRepository;
        this.emailValidator = emailValidator;
        this.confirmationTokenRepository = confirmationTokenRepository;
       this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Profile RegisterAccount(String email, String username, String password) throws EmailValidationException,PasswordNotValidException{
      if(!emailValidator.isValid(email))
          throw new EmailValidationException();
      if(password.length()<5)
          throw new PasswordNotValidException();
       Profile profile = new Profile(username,email,password);
        profileRepository.save(profile);
        return profile;
    }



    @Override
    public Profile LogInToAccount(String username, String password, boolean rememberMe) throws UsernameNotFoundException, PasswordIncorrectException {
        Optional<Profile> profile = profileRepository.findByUsername(username);
        if(!profile.isPresent())
            throw new UsernameNotFoundException();
        Profile AcquiredProfile = profile.get();
        if(!passwordEncoder.matches(password,AcquiredProfile.getPassword()))
            throw new PasswordIncorrectException();
        return AcquiredProfile;
    }

    @Override
    public Profile ResetForgottenPassword(String email) throws EmailNotFoundException,PasswordIncorrectException,EmailValidationException {

        if(!emailValidator.isValid(email))
            throw new EmailValidationException();
        Optional<Profile> profile = profileRepository.findByEmail(email);
        if(!profile.isPresent())
            return null;
        Profile AcquiredProfile = profile.get();
        String pw = UUID.randomUUID().toString();
        AcquiredProfile.setPassword(pw);
        return AcquiredProfile;

    }

    @Override
    public Profile EditAccountData(String username, String password, String newUsername, String newEmail, String newPassword) throws UsernameNotFoundException,PasswordIncorrectException {
        Optional<Profile> profile = profileRepository.findByUsername(username);
        if(!profile.isPresent())
            throw new UsernameNotFoundException();
        Profile AcquiredProfile = profile.get();
        if(!password.equals(AcquiredProfile.getPassword()))
            throw new PasswordIncorrectException();
        if(newUsername != null)
            AcquiredProfile.setName(username);
        if((newEmail !=null)&& emailValidator.isValid(newEmail))
            AcquiredProfile.setEmail(newEmail);
        if(newPassword !=null)
            AcquiredProfile.setPassword(passwordEncoder.encode(newPassword));
        profileRepository.save(AcquiredProfile);
        return AcquiredProfile;
    }

    @Override
    public Profile ChangeAccountRole(Profile profile) throws ProfileNotFoundException,PasswordIncorrectException {
        Optional<Profile> profile1 = profileRepository.findByUsername(profile.getName());
        if(!profile1.isPresent())
            throw new ProfileNotFoundException("username - " + profile.getName());
        Profile AcquiredProfile = profile1.get();
        if(!profile.getPassword().equals(AcquiredProfile.getPassword()))
            throw new PasswordIncorrectException();
        AcquiredProfile.setRole(profile.getRole());
        return AcquiredProfile;
    }

    @Override
    public Profile ActivateProfile(String email) throws EmailNotFoundException {
        Optional<Profile> profile = profileRepository.findByEmail(email);
        if(profile.isPresent()){
            Profile profile1 = profile.get();
            profile1.setActive();
            profileRepository.save(profile1);
            return profile1;
        }

        return null;
    }

    @Override
    public Profile expiredProfile(long tokenid) throws TokenNotFoundException {
        Optional<ConfirmationToken> confirmationToken = this.confirmationTokenRepository.findById(tokenid);
        if(confirmationToken.isPresent()){
            ConfirmationToken token = confirmationToken.get();
            if(token.isExpired()){
                Profile profile = token.getProfile();
                this.profileRepository.delete(profile);
                return profile;
            }
        }
        return null;
    }

    @Override
    public Boolean DeleteAccount(Profile profile) throws ProfileNotFoundException,PasswordIncorrectException {
        Optional<Profile> profile1 = profileRepository.findByUsername(profile.getName());
        if(!profile1.isPresent())
            throw new ProfileNotFoundException("username - " + profile.getName());
        Profile AcquiredProfile = profile1.get();
        if(!profile.getPassword().equals(AcquiredProfile.getPassword()))
            throw new PasswordIncorrectException();
        profileRepository.delete(AcquiredProfile);
        return true;
    }
}
