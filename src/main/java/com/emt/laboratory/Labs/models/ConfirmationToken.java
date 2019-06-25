package com.emt.laboratory.Labs.models;


import org.springframework.beans.factory.annotation.Value;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "token_id")
    private long tokenid;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    public LocalDateTime expiryTime;

    @Value("${profile_validation_expiry_in_hours}")
    public long expiryhours;

    @OneToOne(targetEntity = Profile.class, fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "profileid")
    private Profile profile;

    public ConfirmationToken(Profile profile) {
        this.profile = profile;
        confirmationToken = UUID.randomUUID().toString();
        expiryTime = LocalDateTime.now().plusHours(expiryhours);
    }

    public ConfirmationToken(){}

    public String getConfirmationToken() {
        return confirmationToken;
    }


    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(this.expiryTime);
    }

}
