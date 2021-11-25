package ro.home.project.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ro.home.project.domain.enums.PaymentStatus;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Currency;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(callSuper = true, exclude = {"filePayment"})
@Entity
@Table(name = "PAYMENT_DETAILS")
public class PaymentDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "paymentdetails_generator")
    @SequenceGenerator(name="paymentdetails_generator", sequenceName = "payment_details_id_seq", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FILE_PAYMENT_ID")
    @JsonIgnore
    @ToString.Exclude
    private FilePayment filePayment;

    @Column(name = "FILE_ROW_NR")
    private Integer fileRowNr;

    @Column(name = "NAME")
    @NotEmpty
    @Size(max = 50, message = "ERR_NUMEBENEFICIAR_SIZE")
    private String name;

    @Column(name = "ACCOUNT_NR")
    @NotEmpty
    @Size(max = 25, message = "ERR_CONTBENEFICIAR_SIZE")
    @Pattern(regexp = "^[a-zA-Z0-9]+$")
    private String accountNr;

    @Column(name = "AMOUNT")
    @Positive(message = "ERR_AMOUNT_POSITIVE")
    private BigDecimal amount;

    @Column(name = "CURRENCY")
    @NotNull
    private Currency currency;

    @Column(name = "PERSONAL_ID")
    @Size(max = 50)
    @Deprecated
    private String personalId;

    @Column(name = "DESCRIPTION")
    @Size(max = 70, message = "ERR_TRANSAC_DESC_SIZE")
    private String description;

    @Column(name = "PAYMENT_STATUS")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.PENDING;

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;
}
