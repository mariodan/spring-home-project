package ro.home.project.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.home.project.domain.entity.FilePayment;

import javax.persistence.LockModeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FilePaymentRepository extends JpaRepository<FilePayment, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<FilePayment> findById(final Long id);

    List<FilePayment> findAllByInsertedAtBefore(final LocalDateTime localDateTime);

    List<FilePayment> findAllByCompanyCif(final String companyCif);

    @Modifying
    @Query("DELETE FROM FilePayment f where f.id in ?1")
    void deleteFilePaymentsByIds(final List<Long> ids);


}
