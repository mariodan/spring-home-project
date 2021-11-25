--liquibase formatted sql

--changeset t026dansm:ALE-6859-01
ALTER TABLE FILE_PAYMENTS ADD CLIENT_IBAN VARCHAR(24) NULL;
COMMENT ON COLUMN FILE_PAYMENTS.CLIENT_IBAN IS 'Eligible account IBAN';
--rollback ;

--changeset t026dansm:ALE-6859-02
ALTER TABLE FILE_PAYMENTS ADD CLIENT_NAME VARCHAR(50) NULL;
COMMENT ON COLUMN FILE_PAYMENTS.CLIENT_NAME IS 'Client name';
--rollback ;