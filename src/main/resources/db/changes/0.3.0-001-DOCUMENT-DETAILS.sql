--liquibase formatted sql

--changeset t026dansm:ALE-5555-01
CREATE TABLE DOCUMENT_DETAILS
(
    ID                  SERIAL PRIMARY KEY,
    FROM_ACCOUNT        VARCHAR(40) NOT NULL,
    PAYMENT_DESCRIPTION VARCHAR(40) NOT NULL,
    PROCESSED_AMOUNT    REAL DEFAULT 0 NOT NULL,
    PROCESS_STATUS      INTEGER DEFAULT 0 NOT NULL,
    CURRENCY            VARCHAR(3) DEFAULT 'RON' NOT NULL,
    FROM_ACCOUNT_KEY    VARCHAR(25) NOT NULL,
    CLIENT_NAME         VARCHAR(25) NOT NULL,
    INSERTED_AT         TIMESTAMP(6) NOT NULL,
    UPDATED_AT          TIMESTAMP(6) NOT NULL,
    INSERTED_BY         VARCHAR(50) NOT NULL,
    UPDATED_BY          VARCHAR(50) NOT NULL
);
--rollback;

--changeset t026dansm:ALE-5555-02
COMMENT ON TABLE DOCUMENT_DETAILS IS 'Table of file payments';

COMMENT ON COLUMN DOCUMENT_DETAILS.ID IS 'PK';
COMMENT ON COLUMN DOCUMENT_DETAILS.FROM_ACCOUNT IS 'Debit account from which payments are made';
COMMENT ON COLUMN DOCUMENT_DETAILS.PAYMENT_DESCRIPTION IS 'The company CIF';

COMMENT ON COLUMN DOCUMENT_DETAILS.PROCESSED_AMOUNT IS 'Total amount of the debit transaction';
COMMENT ON COLUMN DOCUMENT_DETAILS.CLIENT_NAME IS 'Name of the client';
COMMENT ON COLUMN DOCUMENT_DETAILS.FROM_ACCOUNT_KEY IS 'From Account key of the paying customer';
COMMENT ON COLUMN DOCUMENT_DETAILS.CURRENCY IS 'Currency of the payment';
COMMENT ON COLUMN DOCUMENT_DETAILS.PROCESS_STATUS IS 'The status of the process';
COMMENT ON COLUMN DOCUMENT_DETAILS.UPDATED_BY IS 'updated by account';
COMMENT ON COLUMN DOCUMENT_DETAILS.UPDATED_AT IS 'updated at timestamp';
COMMENT ON COLUMN DOCUMENT_DETAILS.INSERTED_BY IS 'inserted by account';
COMMENT ON COLUMN DOCUMENT_DETAILS.INSERTED_AT IS 'inserted at timestamp';
--rollback ;


--changeset t026dansm:ALE-5555-03
ALTER TABLE DOCUMENT_DETAILS ADD CONSTRAINT CK_DD_PROCESS_STATUS CHECK (PROCESS_STATUS>=0 AND PROCESS_STATUS<=6);
--rollback;