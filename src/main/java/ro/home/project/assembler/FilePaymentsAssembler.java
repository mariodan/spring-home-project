package ro.home.project.assembler;

import org.springframework.stereotype.Component;
import ro.home.project.domain.entity.FilePayment;
import ro.home.project.domain.entity.PaymentDetails;
import ro.home.project.model.request.PaymentDetailsRq;
import ro.home.project.model.response.FilePaymentsRs;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FilePaymentsAssembler {

	public FilePaymentsRs fromEntity(final FilePayment fp) {
		return FilePaymentsRs
				.builder()
				.fromAccount(fp.getFromAccount())
				.id(fp.getId())
				.originalFileName(fp.getOriginalFileName())
				.paymentDescription(fp.getPaymentDescription())
				.processedAmount(fp.getProcessedAmount())
				.totalAmount(fp.getTotalAmount())
				.clientIban(fp.getClientIban())
				.clientName(fp.getClientName())
				.companyCif(fp.getCompanyCif())
				.fileName(fp.getFileName())
				.currency(fp.getCurrency().getCurrencyCode())
				.build();
	}

	public List<FilePaymentsRs> fromList(final List<FilePayment> payments) {
		return payments.stream().map(this::fromEntity).collect(Collectors.toList());
	}

	public PaymentDetails fromPaymentDetails(final PaymentDetailsRq p) {
		return PaymentDetails
				.builder()
				.accountNr(p.getAccountNr())
				.amount(p.getAmount())
				.currency(Currency.getInstance(p.getCurrency()))
				.description(p.getDescription())
				.name(p.getName())
				.paymentStatus(p.getPaymentStatus())
				.fileRowNr(1)
				.personalId("not collected")
				.build();
	}
}
