package ro.home.project.domain.entity;


import ro.home.project.domain.enums.ProcessStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

public interface FileDetails {

    Long getId();

    String getOriginalFileName();

    ProcessStatus getProcessStatus();

    String getInsertedBy();

    LocalDateTime getInsertedAt();

    BigDecimal getTotalAmount();

    BigDecimal getProcessedAmount();

    Currency getCurrency();

    String getPaymentDescription();

    Integer getTotalEntries();

    String getErrorMessage();

}
