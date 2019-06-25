package com.emt.laboratory.Labs.ports.scheduled;

import com.emt.laboratory.Labs.models.exceptions.TokenNotFoundException;
import com.emt.laboratory.Labs.models.projections.ConfirmationTokenProjection;
import com.emt.laboratory.Labs.repositoryJPA.mail.ConfirmationTokenRepositoryID;
import com.emt.laboratory.Labs.services.AccountManagementService;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Profile("cron-invoked-expiry")
@Component
public class CleanExpiredProfiles {

    private final ConfirmationTokenRepositoryID confirmationTokenRepositoryID;

    private final AccountManagementService accountManagementService;

    public CleanExpiredProfiles(ConfirmationTokenRepositoryID confirmationTokenRepositoryID, AccountManagementService accountManagementService) {
        this.accountManagementService = accountManagementService;
        this.confirmationTokenRepositoryID = confirmationTokenRepositoryID;

    }

    @Scheduled(cron = "0 0 * * *")
    public void cleanExpiredAccounts(){
        List<ConfirmationTokenProjection> expiredProfiles  = this.confirmationTokenRepositoryID.findByExpiryTimeAfter(LocalDateTime.now());
        expiredProfiles
                .stream()
                .map(ConfirmationTokenProjection::getTokenid)
                .forEach(tokenid -> {
                    try {
                        this.accountManagementService.expiredProfile(tokenid);
                    } catch (TokenNotFoundException e) {
                        e.printStackTrace();
                    }
                });
    }

}
