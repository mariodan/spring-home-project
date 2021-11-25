package ro.home.project.model.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class FilePaymentsRs {
	private Long id;
	private String fromAccount;
	private BigDecimal totalAmount;
	private BigDecimal processedAmount;
	private String paymentDescription;
	private String originalFileName;
	private String fileName;
	private String companyCif;
	private String fileHash;
	private String currency;
	private String clientName;
	private String clientIban;
}
