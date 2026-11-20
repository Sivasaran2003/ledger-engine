ALTER TABLE ledger_transactions
    ADD COLUMN from_account_id UUID,
    ADD COLUMN to_account_id UUID,
    ADD COLUMN amount_paise BIGINT;

ALTER TABLE ledger_transactions
    ADD CONSTRAINT fk_transaction_from_account
        FOREIGN KEY (from_account_id) REFERENCES accounts(id),
    ADD CONSTRAINT fk_transaction_to_account
        FOREIGN KEY (to_account_id) REFERENCES accounts(id),
    ADD CONSTRAINT chk_transaction_amount
        CHECK (amount_paise IS NULL OR amount_paise > 0);