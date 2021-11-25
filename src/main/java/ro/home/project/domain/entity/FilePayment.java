package ro.home.project.domain.entity;

import lombok.*;
import org.hibernate.annotations.Type;
import ro.home.project.domain.enums.ProcessStatus;
import ro.home.project.domain.enums.ProcessStatusConverter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "FILE_PAYMENTS")
public class FilePayment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "filepayments_generator")
    @SequenceGenerator(name="filepayments_generator", sequenceName = "file_payments_id_seq", allocationSize=1)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "FROM_ACCOUNT")
    @NotEmpty
    @Size(max = 24, message = "ERR_FROMACCOUNT_SIZE")
    private String fromAccount;

    @Column(name = "PAYMENT_DESCRIPTION")
    @Size(max = 70, message = "ERR_PAYMENT_DESCRIPTION_SIZE")
    private String paymentDescription;

    @Column(name = "TOTAL_AMOUNT")
    @Positive
    private BigDecimal totalAmount;

    @Column(name = "PROCESSED_AMOUNT")
    @PositiveOrZero
    @Builder.Default
    private BigDecimal processedAmount = BigDecimal.ZERO;

    @Column(name = "ORIGINAL_FILE_NAME")
    @NotEmpty
    @Size(max = 30, message = "ERR_FILENAME_SIZE")
    private String originalFileName;

    @Column(name = "FILE_NAME")
    @NotEmpty
    @Size(max = 128)
    private String fileName;

    @Column(name = "COMPANY_CIF")
    @NotEmpty
    @Size(max = 10)
    private String companyCif;

    @Lob
    @Column(name = "FILE_CONTENT")
    @Type(type="org.hibernate.type.BinaryType")
    @JsonIgnore
    @ToString.Exclude
    private byte[] fileContent;

    @Lob
    @Column(name = "RESULT_FILE_CONTENT")
    @Type(type="org.hibernate.type.BinaryType")
    @JsonIgnore
    @ToString.Exclude
    private byte[] resultFileContent;

    @Column(name = "SUCCESS_RESULT_FILE_NAME")
    @Size(max = 128)
    private String successResultFileName;

    @Column(name = "ERROR_RESULT_FILE_NAME")
    @Size(max = 128)
    private String errorResultFileName;

    @Column(name = "FILE_HASH")
    @NotEmpty
    private String fileHash;

    @Column(name = "PROCESS_STATUS")
    @Convert(converter = ProcessStatusConverter.class)
    @Builder.Default
    private ProcessStatus processStatus = ProcessStatus.PENDING;

    @Column(name = "ERROR_MESSAGE")
    @JsonIgnore
    @ToString.Exclude
    private String errorMessage;

    @Column(name = "RESULT_MESSAGE")
    @JsonIgnore
    @ToString.Exclude
    private String resultMessage;

    @Column(name = "CURRENCY")
    private Currency currency;

    @Column(name = "FROM_ACCOUNT_KEY")
    private String fromAccountKey;

    @Column(name = "CLIENT_IBAN")
    @NotEmpty
    private String clientIban;

    @Column(name = "CLIENT_NAME")
    @NotEmpty
    private String clientName;

    @JsonIgnore
    @OneToMany(mappedBy = "filePayment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private List<PaymentDetails> payments;

}
