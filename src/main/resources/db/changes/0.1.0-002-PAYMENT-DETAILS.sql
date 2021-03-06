--liquibase formatted sql

--changeset t026dansm:ALE-4953-01
CREATE TABLE PAYMENT_DETAILS
(
  ID                     SERIAL PRIMARY KEY,
  FILE_PAYMENT_ID        SERIAL NOT NULL,
  FILE_ROW_NR            SMALLINT NOT NULL,
  NAME                   VARCHAR(50) NOT NULL,
  ACCOUNT_NR             VARCHAR(50) NOT NULL,
  AMOUNT                 REAL DEFAULT 0 NOT NULL,
  CURRENCY               VARCHAR(3) NOT NULL,
  PERSONAL_ID            VARCHAR(50) NOT NULL,
  DESCRIPTION            VARCHAR(70) NOT NULL,
  INSERTED_AT            TIMESTAMP(6) NOT NULL,
  UPDATED_AT             TIMESTAMP(6),
  INSERTED_BY            VARCHAR(50) NOT NULL,
  UPDATED_BY             VARCHAR(50),
  CONSTRAINT FK_FILE_PAYMENT_ID FOREIGN KEY (FILE_PAYMENT_ID) REFERENCES FILE_PAYMENTS (ID)
);
--rollback DROP TABLE "PAYMENT_DETAILS"

--changeset t026dansm:ALE-4953-02
COMMENT ON COLUMN PAYMENT_DETAILS.ID is 'PK';

COMMENT ON COLUMN PAYMENT_DETAILS.FILE_PAYMENT_ID is 'The FK to FILE_PAYMENT.ID';
COMMENT ON COLUMN PAYMENT_DETAILS.FILE_ROW_NR is 'The corresponding line in the input file';
COMMENT ON COLUMN PAYMENT_DETAILS.NAME is 'The name of beneficiary';
COMMENT ON COLUMN PAYMENT_DETAILS.ACCOUNT_NR is 'The destination account number';
COMMENT ON COLUMN PAYMENT_DETAILS.AMOUNT is 'The amount of the transaction';
COMMENT ON COLUMN PAYMENT_DETAILS.CURRENCY is 'The currency of the transaction';
COMMENT ON COLUMN PAYMENT_DETAILS.PERSONAL_ID is 'CUI/CNP of the beneficiary';
COMMENT ON COLUMN PAYMENT_DETAILS.DESCRIPTION is 'Description of the transaction';

COMMENT ON COLUMN PAYMENT_DETAILS.UPDATED_BY IS 'updated by account';
COMMENT ON COLUMN PAYMENT_DETAILS.UPDATED_AT IS 'updated at timestamp';
COMMENT ON COLUMN PAYMENT_DETAILS.INSERTED_BY IS 'inserted by account';
COMMENT ON COLUMN PAYMENT_DETAILS.INSERTED_AT IS 'inserted at timestamp';
--rollback ;

--changeset t026savai:ALE-7359-01
ALTER TABLE PAYMENT_DETAILS
    ADD PAYMENT_STATUS VARCHAR(10);
comment on column PAYMENT_DETAILS.PAYMENT_STATUS
    is 'Status of a payment: PENDING, SUCCESS, FAILED.';
--rollback ALTER TABLE PAYMENT_DETAILS DROP COLUMN PAYMENT_STATUS;

--changeset t026savai:ALE-7359-02
ALTER TABLE PAYMENT_DETAILS
    ADD ERROR_MESSAGE VARCHAR;
comment on column PAYMENT_DETAILS.ERROR_MESSAGE
    is 'Error message for a failed transaction.';
--rollback ALTER TABLE PAYMENT_DETAILS DROP COLUMN ERROR_MESSAGE;

--changeset t026savai:ALE-7164
comment on column PAYMENT_DETAILS.ERROR_MESSAGE
    is 'Error message for a failed transaction.';
--rollback;