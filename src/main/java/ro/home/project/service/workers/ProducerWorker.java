package ro.home.project.service.workers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ro.home.project.domain.entity.DocumentDetails;
import ro.home.project.domain.enums.ProcessStatus;
import ro.home.project.service.DocumentDetailsService;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Slf4j
@Component
@ConditionalOnProperty(name = "spring.workers.start", havingValue = "true", matchIfMissing = false)
public class ProducerWorker {

    private final BlockingQueue<DocumentDetails> documentDetailsQueue;
    private final DocumentDetailsService documentDetailsService;
    private final Executor asyncExecutor;

    public ProducerWorker(@Qualifier("documentDetailsProcessingQueue") BlockingQueue<DocumentDetails> documentDetailsQueue,
                          @Qualifier("documentDetailsService") DocumentDetailsService documentDetailsService,
                          @Qualifier("asyncExecutor") Executor asyncExecutor) {
        this.documentDetailsQueue = documentDetailsQueue;
        this.documentDetailsService = documentDetailsService;
        this.asyncExecutor = asyncExecutor;
    }

    @Scheduled(fixedDelay = 30000)
    public void produce() {

        Set<DocumentDetails> results = documentDetailsService.getEligibleDocumentDetails(ProcessStatus.PENDING);

        results.stream().skip(0).limit(10).forEach(addToQueue());

        startConsumers();
    }

    private Consumer<DocumentDetails> addToQueue() {
        return document -> {
            if (!documentDetailsQueue.contains(document)) {
                LOGGER.info("Adding to queue entity with id: {}, queue size: {}, remaining slots: {}", document.getId(),
                        documentDetailsQueue.size(), documentDetailsQueue.remainingCapacity());
                documentDetailsQueue.add(document);
            }
        };
    }

    private void startConsumers() {
        IntStream.rangeClosed(1, 5).forEach(i -> asyncExecutor.execute(new ConsumerWorker(documentDetailsQueue, documentDetailsService)));
    }
}
