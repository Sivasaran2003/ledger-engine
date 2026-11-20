CREATE TABLE accounts (
    id UUID PRIMARY KEY,
    currency CHAR(3) NOT NULL,
    balance_paise BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_accounts_currency
        CHECK (currency ~ '^[A-Z]{3}$'),

    CONSTRAINT chk_accounts_balance_nonnegative
        CHECK (balance_paise >= 0)
);