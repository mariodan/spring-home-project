package ro.home.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.home.project.domain.entity.DocumentDetails;
import ro.home.project.domain.enums.ProcessStatus;
import ro.home.project.domain.repository.DocumentDetailsRepository;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DocumentDetailsService {

    private final DocumentDetailsRepository documentDetailsRepository;

    @Transactional
    public Set<DocumentDetails> getEligibleDocumentDetails(final ProcessStatus status) {
        return documentDetailsRepository.findAllByProcessStatus(status);
    }

    @Transactional
    public Optional<DocumentDetails> getByIdWithWriteLock(long id) {
        return documentDetailsRepository.findById(id);
    }

    @Transactional
    public DocumentDetails save(DocumentDetails documentDetails) {
        return documentDetailsRepository.save(documentDetails);
    }
}
