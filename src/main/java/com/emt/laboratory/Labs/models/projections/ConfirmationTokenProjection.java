package com.emt.laboratory.Labs.models.projections;

import java.time.LocalDateTime;

public interface ConfirmationTokenProjection {

    Long getTokenid();

    LocalDateTime getExpiryTime();

}
