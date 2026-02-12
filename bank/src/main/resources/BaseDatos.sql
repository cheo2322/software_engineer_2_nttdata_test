-- Table creation --
CREATE TABLE IF NOT EXISTS persons (
    id SERIAL PRIMARY KEY,
    person_name VARCHAR(255),
    genre VARCHAR(20) CHECK (genre IN ('MALE','FEMALE','OTHER')),
    age INT,
    identification VARCHAR(255) UNIQUE,
    address VARCHAR(255),
    phone VARCHAR(50) UNIQUE
);

CREATE TABLE IF NOT EXISTS clients (
    id SERIAL PRIMARY KEY,
    password_hash VARCHAR(255),
    client_status BOOLEAN,
    person_id INT UNIQUE,
    CONSTRAINT fk_client_person FOREIGN KEY (person_id) REFERENCES persons(id)
);

CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    account_number VARCHAR(50) UNIQUE,
    account_type VARCHAR(20) CHECK (account_type IN ('SAVINGS','CHECKING')),
    initial_balance NUMERIC(15,2),
    account_status BOOLEAN,
    client_id INT,
    CONSTRAINT fk_account_client FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE IF NOT EXISTS movements (
    id SERIAL PRIMARY KEY,
    movement_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    movement_type VARCHAR(20) CHECK (movement_type IN ('DEPOSIT','WITHDRAWAL')),
    movement_value NUMERIC(15,2),
    balance NUMERIC(15,2),
    account_id INT,
    CONSTRAINT fk_movement_account FOREIGN KEY (account_id) REFERENCES accounts(id)
);


-- Data insertion --
INSERT INTO persons (person_name, genre, age, identification, address, phone)
VALUES
    ('Jose Lema', 'MALE', 30, '1001234567', 'Otavalo sn y principal', '098254785'),
    ('Marianela Montalvo', 'FEMALE', 25, '1001234568', 'Amazonas y NNUU', '097548965'),
    ('Juan Osorio', 'MALE', 40, '1005678912', '13 junio y Equinoccial', '098874587')
ON CONFLICT (identification) DO NOTHING;


INSERT INTO clients (password_hash, client_status, person_id)
SELECT 'A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=', TRUE, p.id
FROM persons p
WHERE p.identification = '1001234567'
  AND NOT EXISTS (SELECT 1 FROM clients WHERE person_id = p.id);

INSERT INTO clients (password_hash, client_status, person_id)
SELECT '+GOLl5svT3k92229GX4O4lp6bqMrCuIvXjxdEZ2DnnU=', TRUE, p.id
FROM persons p
WHERE p.identification = '1001234568'
  AND NOT EXISTS (SELECT 1 FROM clients WHERE person_id = p.id);

INSERT INTO clients (password_hash, client_status, person_id)
SELECT 'jCRLNwdHwZMKTglnJUd43btp9qQJ5ivuvl+SGRoJo6E=', TRUE, p.id
FROM persons p
WHERE p.identification = '1005678912'
  AND NOT EXISTS (SELECT 1 FROM clients WHERE person_id = p.id);


INSERT INTO accounts (account_number, account_type, initial_balance, account_status, client_id)
SELECT '478758', 'SAVINGS', 2000.00, TRUE, c.id
FROM clients c
         JOIN persons p ON c.person_id = p.id
WHERE p.identification = '1001234567'
  AND NOT EXISTS (SELECT 1 FROM accounts WHERE account_number = '478758');

INSERT INTO accounts (account_number, account_type, initial_balance, account_status, client_id)
SELECT '225487', 'CHECKING', 100.00, TRUE, c.id
FROM clients c
         JOIN persons p ON c.person_id = p.id
WHERE p.identification = '1001234568'
  AND NOT EXISTS (SELECT 1 FROM accounts WHERE account_number = '225487');

INSERT INTO accounts (account_number, account_type, initial_balance, account_status, client_id)
SELECT '495878', 'SAVINGS', 0.00, TRUE, c.id
FROM clients c
         JOIN persons p ON c.person_id = p.id
WHERE p.identification = '1005678912'
  AND NOT EXISTS (SELECT 1 FROM accounts WHERE account_number = '495878');

INSERT INTO accounts (account_number, account_type, initial_balance, account_status, client_id)
SELECT '496825', 'SAVINGS', 540.00, TRUE, c.id
FROM clients c
         JOIN persons p ON c.person_id = p.id
WHERE p.identification = '1001234568'
  AND NOT EXISTS (SELECT 1 FROM accounts WHERE account_number = '496825');
