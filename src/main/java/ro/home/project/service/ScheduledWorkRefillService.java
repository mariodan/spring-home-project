package ro.home.project.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.home.project.domain.entity.DocumentDetails;
import ro.home.project.domain.enums.ProcessStatus;
import ro.home.project.domain.repository.DocumentDetailsRepository;
import static ro.home.project.util.StringUtils.*;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Random;

@Slf4j
@Component
public class ScheduledWorkRefillService {

    private final DocumentDetailsRepository documentDetailsRepository;

    public ScheduledWorkRefillService(DocumentDetailsRepository documentDetailsRepository) {
        this.documentDetailsRepository = documentDetailsRepository;
    }

    @Scheduled(fixedDelay = 4000)
    public void fillWorkForProducers() {
        
        DocumentDetails documentDetails = DocumentDetails
                .builder()
                .clientName(getRandomString("clientName"))
                .currency(Currency.getInstance("RON"))
                .fromAccount(getRandomString("fromAccount"))
                .fromAccountKey(getRandomString("fromAccountKey"))
                .paymentDescription(getRandomString("paymentDescription"))
                .processedAmount(getRandomAmount())
                .processStatus(ProcessStatus.PENDING)
                .build();

        documentDetails = documentDetailsRepository.save(documentDetails);

        LOGGER.info("Start filling work...{}", documentDetails);
    }

    private BigDecimal getRandomAmount() {
        Random random = new Random();
        return new BigDecimal(random.nextInt(100));
    }
}
