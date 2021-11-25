package ro.home.project.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ro.home.project.assembler.FilePaymentsAssembler;
import ro.home.project.domain.entity.FilePayment;
import ro.home.project.domain.entity.PaymentDetails;
import ro.home.project.domain.enums.ProcessStatus;
import ro.home.project.domain.repository.FilePaymentRepository;
import ro.home.project.domain.repository.PaymentDetailsRepository;
import ro.home.project.exception.ResourceNotFoundException;
import ro.home.project.model.request.FilePaymentRq;
import ro.home.project.model.request.PaymentDetailsRq;
import ro.home.project.model.response.FilePaymentsRs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilePaymentsService {

	private final FilePaymentRepository filePaymentRepository;
	private final PaymentDetailsRepository paymentDetailsRepository;

	private final FilePaymentsAssembler assembler;

	@Transactional
	public FilePaymentsRs getFilePaymentById(final long id) {
		return assembler.fromEntity(getFilePayment(id));
	}

	@Transactional
	public FilePayment getFilePayment(final long id) {
		return filePaymentRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Entity not found for: " + id));
	}

	@Transactional
	public List<FilePaymentsRs> getFilePaymentsByInsertedAtBefore(final LocalDateTime localDateTime) {
		return assembler.fromList(filePaymentRepository.findAllByInsertedAtBefore(localDateTime));
	}

	@Transactional
	public List<FilePaymentsRs> getFilePaymentsByLeCif(final String leCif) {
		return assembler.fromList(filePaymentRepository.findAllByCompanyCif(leCif));
	}

	@Transactional
	public void deleteFilePayment(final List<Long> ids) {
		filePaymentRepository.deleteFilePaymentsByIds(ids);
	}

	public FilePaymentsRs createFilePayment(final FilePaymentRq rq) {

		FilePayment fp = FilePayment
				.builder()
				.clientIban(rq.getClientIban())
				.clientName(rq.getClientName())
				.companyCif(rq.getCompanyCif())
				.currency(Currency.getInstance(rq.getCurrency()))
				.fileHash(rq.getFileHash())
				.fileName(rq.getFileName())
				.processStatus(ProcessStatus.PENDING)
				.totalAmount(rq.getPayments().stream().map(PaymentDetailsRq::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add))
				.fromAccount(rq.getFromAccount())
				.originalFileName(rq.getOriginalFileName())
				.paymentDescription(rq.getPaymentDescription())
				.fileContent("test".getBytes())
				.build();

		fp = filePaymentRepository.save(fp);

		paymentDetailsRepository.saveAll(getCollectPaymentDetails(rq, fp));

		return assembler.fromEntity(fp);
	}

	private List<PaymentDetails> getCollectPaymentDetails(FilePaymentRq rq, FilePayment finalFp) {
		return rq.getPayments()
				.stream()
				.map(assembler::fromPaymentDetails)
				.map(detail -> {
					detail.setFilePayment(finalFp);
					return detail;
				})
				.collect(Collectors.toList());
	}

	@Transactional
	public FilePaymentsRs updateEntity(final long id, final FilePaymentRq rq) {
		FilePayment fp = filePaymentRepository
				.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Entity not found for id: " + id));

		fp.setClientIban(rq.getClientIban());
		fp.setClientName(rq.getClientName());
		fp.setCompanyCif(rq.getCompanyCif());

		return assembler.fromEntity(fp);
	}
}
