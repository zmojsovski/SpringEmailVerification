package com.emt.laboratory.Labs.repositoryJPA.mail;

import com.emt.laboratory.Labs.models.ConfirmationToken;
import com.emt.laboratory.Labs.models.projections.ConfirmationTokenProjection;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConfirmationTokenRepositoryID extends CrudRepository<ConfirmationToken, Long> {
    List<ConfirmationTokenProjection> findByExpiryTimeAfter(LocalDateTime time);
}
