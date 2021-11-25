package ro.home.project.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.home.project.domain.entity.FilePayment;
import ro.home.project.domain.entity.PaymentDetails;
import ro.home.project.domain.enums.PaymentStatus;

import java.util.List;

@Repository
public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {

    Page<PaymentDetails> findAllByFilePayment(FilePayment filePayment, Pageable pageable);

    @Modifying
    @Query("update PaymentDetails p set p.paymentStatus = :status where p.filePayment = :filePayment")
    void updateStatusByFilePaymentId(@Param("status") PaymentStatus status, @Param("filePayment") FilePayment filePayment);

    @Modifying
    @Query("DELETE FROM PaymentDetails p where p.filePayment.id in ?1")
    void deletePaymentDetailsByFilePayments(final List<Long> ids);
}
