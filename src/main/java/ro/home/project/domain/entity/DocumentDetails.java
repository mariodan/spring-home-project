package ro.home.project.domain.entity;

import lombok.*;
import ro.home.project.domain.enums.ProcessStatus;
import ro.home.project.domain.enums.ProcessStatusConverter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Currency;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "DOCUMENT_DETAILS")
public class DocumentDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "serial", updatable = false, nullable = false)
    private Long id;

    @Column(name = "FROM_ACCOUNT")
    @NotEmpty
    @Size(max = 24, message = "ERR_FROMACCOUNT_SIZE")
    private String fromAccount;

    @Column(name = "PAYMENT_DESCRIPTION")
    @Size(max = 70, message = "ERR_PAYMENT_DESCRIPTION_SIZE")
    private String paymentDescription;

    @Column(name = "PROCESSED_AMOUNT")
    @PositiveOrZero
    @Builder.Default
    private BigDecimal processedAmount = BigDecimal.ZERO;

    @Column(name = "PROCESS_STATUS")
    @Convert(converter = ProcessStatusConverter.class)
    @Builder.Default
    private ProcessStatus processStatus = ProcessStatus.PENDING;

    @Column(name = "CURRENCY")
    private Currency currency;

    @Column(name = "FROM_ACCOUNT_KEY")
    private String fromAccountKey;

    @Column(name = "CLIENT_NAME")
    @NotEmpty
    private String clientName;

}

