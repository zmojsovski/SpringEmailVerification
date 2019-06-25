package com.emt.laboratory.Labs.web;

import com.emt.laboratory.Labs.models.ConfirmationToken;
import com.emt.laboratory.Labs.models.Profile;
import com.emt.laboratory.Labs.models.Role;
import com.emt.laboratory.Labs.models.exceptions.*;
import com.emt.laboratory.Labs.repositoryJPA.ConfirmationTokenRepository;
import com.emt.laboratory.Labs.repositoryJPA.mail.MailSenderRepository;
import com.emt.laboratory.Labs.services.AccountManagementService;
import com.emt.laboratory.Labs.services.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ProfileController {

    @Autowired
    private AccountManagementService accountManagementService;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private MailSenderRepository mailSenderRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/signup")
    public String signUp(Model model) {
        model.addAttribute("profile", new Profile());
        model.addAttribute("signup");
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(Model model, Profile profile) throws PasswordNotValidException, EmailValidationException, IOException, MessagingException {
        // accountManagementService.RegisterAccount(profile.getEmail(),profile.getName(),profile.getPassword());
        profile.setPassword(passwordEncoder.encode(profile.getPassword()));
        profile.setRole(Role.User);
        ConfirmationToken confirmationToken = new ConfirmationToken(profile);
        confirmationTokenRepository.save(confirmationToken);
        //SimpleMailMessage mailMessage = new SimpleMailMessage();
        Map<String, String> map = new HashMap<>();
        map.put("person", profile.getName());
        map.put("token", confirmationToken.getConfirmationToken());
        mailSenderRepository.sendHtmlMail(profile.getEmail(), "Complete your registration!", "profile-verification-mail.html", map);
        // emailSenderService.sendEmail(mailMessage);
        model.addAttribute("email", profile.getEmail());
        model.addAttribute("name", profile.getName());
        model.addAttribute("MailSent");
        return "MailSent";
    }

    @GetMapping(value = "/confirm-account")
    public String confirmUserAccount(Model model,@RequestParam("token") String confirmationToken) throws EmailNotFoundException {
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

        if (token != null) {
            Profile profile = accountManagementService.ActivateProfile(token.getProfile().getEmail());
            if (profile == null) {
                return "error";
            }
        }
        model.addAttribute("confirmsuccess");
        return "confirmsuccess";
    }

    @GetMapping(value = "/signin")
    public String SignIn(Model model,Profile profile) {
        model.addAttribute("profile", profile);
        model.addAttribute("signin");
        return "signin";
    }

    @PostMapping(value = "/signin")
    public String SignIn(Model model, Profile profile, boolean rememberMe) throws PasswordIncorrectException, UsernameNotFoundException {
        accountManagementService.LogInToAccount(profile.getName(),profile.getPassword(), rememberMe);
        model.addAttribute("profile", profile);
        model.addAttribute("home");
        return "home";

    }

    @GetMapping(value = "/home")
    public String Home(Model model, Profile profile) {
        model.addAttribute("profile",profile);
        model.addAttribute("home");
        return "home";
    }


    @GetMapping(value = "/forgottenpassword")
    public String ForgottenPassword(Model model, String email) {
        model.addAttribute("email", email);
        model.addAttribute("forgottenpassword");
        return "forgottenpassword";
    }
    @PostMapping(value = "/forgottenpassword")
    public String ForgottenPassword(String email) throws EmailNotFoundException, EmailValidationException, PasswordIncorrectException, IOException, MessagingException {
            Profile profile = accountManagementService.ResetForgottenPassword(email);
            if(profile !=null)
        {
            Map<String, String> map = new HashMap<>();
            map.put("person", profile.getName());
            map.put("password",profile.getPassword());
            mailSenderRepository.sendHtmlMail(email, "Your Password is Changed", "profile-password-change-mail.html", map);
            return "forgottenpasswordconfirm.html";
        }

            return "forgottenpassword";
    }
}