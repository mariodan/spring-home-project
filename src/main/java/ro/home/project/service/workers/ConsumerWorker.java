package ro.home.project.service.workers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ro.home.project.domain.entity.DocumentDetails;
import ro.home.project.domain.enums.ProcessStatus;
import ro.home.project.service.DocumentDetailsService;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public class ConsumerWorker implements Runnable {

    private final BlockingQueue<DocumentDetails> queue;
    private final DocumentDetailsService documentDetailsService;

    @Override
    public void run() {
        Optional.ofNullable(queue.poll())
                .ifPresentOrElse(processDocument(), () -> LOGGER.debug("Could not fetch element from queue. Queue may be empty"));
    }

    private Consumer<DocumentDetails> processDocument() {
        return  documentDetails -> documentDetailsService
                .getByIdWithWriteLock(documentDetails.getId())
                .ifPresentOrElse(doc -> {
                    int sleepTime = new Random().nextInt(10) * 1000;
                    LOGGER.debug("Acquired lock by: {}, for id: {}, sleep ms: {}", Thread.currentThread().getName(), doc.getId(), sleepTime);
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    doc.setProcessStatus(ProcessStatus.COMPLETED);
                    DocumentDetails saved = documentDetailsService.save(doc);
                    LOGGER.debug("Saved doc: {} with status: {}", saved.getId(), saved.getProcessStatus());
                }, () -> LOGGER.warn("Could not get lock on entity with id: {}", documentDetails.getId()));
    }
}
