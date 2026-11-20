
CREATE TABLE ledger_transactions (
    id UUID PRIMARY KEY,
    reference VARCHAR(100) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'COMPLETED',
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_ledger_transactions_status
        CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED'))
);

CREATE TABLE ledger_entries (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL,
    account_id UUID NOT NULL,
    entry_type VARCHAR(10) NOT NULL,
    amount_paise BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_ledger_entries_transaction
        FOREIGN KEY (transaction_id)
        REFERENCES ledger_transactions(id),

    CONSTRAINT fk_ledger_entries_account
        FOREIGN KEY (account_id)
        REFERENCES accounts(id),

    CONSTRAINT chk_ledger_entries_type
        CHECK (entry_type IN ('DEBIT', 'CREDIT')),

    CONSTRAINT chk_ledger_entries_amount
        CHECK (amount_paise > 0)
);

CREATE INDEX idx_ledger_entries_transaction_id
    ON ledger_entries(transaction_id);

CREATE INDEX idx_ledger_entries_account_created
    ON ledger_entries(account_id, created_at);
