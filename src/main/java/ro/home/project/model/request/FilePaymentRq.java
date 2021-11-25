package ro.home.project.model.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class FilePaymentRq {
	private String fromAccount;

	@Positive
	private BigDecimal amount;
	private String paymentDescription;
	private String originalFileName;
	private String fileName;

	@NotEmpty
	private String companyCif;
	private String fileHash;

	@NotEmpty
	private String currency;

	@NotEmpty
	private String clientName;

	@NotEmpty
	private String clientIban;

	private List<PaymentDetailsRq> payments;
}
