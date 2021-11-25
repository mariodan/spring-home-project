package ro.home.project.model.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ro.home.project.domain.enums.PaymentStatus;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PaymentDetailsRq {
	private String name;
	private String accountNr;
	private BigDecimal amount;
	private String currency;
	private String personalId;
	private String description;
	private PaymentStatus paymentStatus;
}
