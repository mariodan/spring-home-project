package ro.home.project.domain.enums;

public enum PaymentStatus {
	PENDING("Tranzactie in asteptare"),
	FAILED("Failed"),
	REJECTED("Rejected"),
	CANCELLED("Cancelled"),
	SUCCESS("Success");

	private String description;

	PaymentStatus(String description) {
		this.description = description;
	}

	public String getDesc() {
		return description;
	}
}
