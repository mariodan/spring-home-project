package ro.home.project.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;
import ro.home.project.domain.entity.DocumentDetails;
import ro.home.project.domain.enums.ProcessStatus;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.Set;

@Repository
public interface DocumentDetailsRepository extends JpaRepository<DocumentDetails, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<DocumentDetails> findById(final Long id);

    Set<DocumentDetails> findAllByProcessStatus(final ProcessStatus processStatus);
}
