CREATE TABLE client (
                        id_client BIGSERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        email VARCHAR(100) NOT NULL UNIQUE,
                        password_hash VARCHAR(255) NOT NULL
);


CREATE TABLE account (
                         id_account BIGSERIAL PRIMARY KEY,
                         id_client BIGINT NOT NULL,
                         account_number VARCHAR(20) NOT NULL UNIQUE,
                         balance DECIMAL(15, 2) NOT NULL CHECK (balance >= 0),
                         is_active BOOLEAN NOT NULL DEFAULT TRUE,
                         CONSTRAINT fk_account_client FOREIGN KEY (id_client) REFERENCES client(id_client)
);


CREATE TABLE transactions (
                              id_transfer BIGSERIAL PRIMARY KEY,
                              id_account_origin BIGINT,
                              id_account_destiny BIGINT,
                              amount DECIMAL(15, 2) NOT NULL CHECK (amount > 0),
                              transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT fk_transaction_origin FOREIGN KEY (id_account_origin) REFERENCES account(id_account),
                              CONSTRAINT fk_transaction_destiny FOREIGN KEY (id_account_destiny) REFERENCES account(id_account)
);


INSERT INTO client (name, email, password_hash) VALUES
                                                    ('Ana Garcia', 'ana@bank.com', 'fake_hash_123'),
                                                    ('Pablo Martinez', 'pablo@bank.com', 'fake_hash_456');

INSERT INTO account (id_client, account_number, balance, is_active) VALUES
                                                                        (1, 'ACC-1001', 5000.00, true),
                                                                        (2, 'ACC-1002', 2500.50, true);