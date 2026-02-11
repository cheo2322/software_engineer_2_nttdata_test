-- Table creation --
CREATE TABLE IF NOT EXISTS persons (
    id SERIAL PRIMARY KEY,
    person_name VARCHAR(100) NOT NULL,
    genre VARCHAR(20),
    age INT,
    identification VARCHAR(50) UNIQUE,
    address VARCHAR(200),
    phone VARCHAR(20) UNIQUE
);

CREATE TABLE IF NOT EXISTS clients (
    id SERIAL PRIMARY KEY,
    password_hash VARCHAR(255),
    client_status VARCHAR(20),
    person_id INT UNIQUE,
    CONSTRAINT fk_person FOREIGN KEY (person_id) REFERENCES persons(id)
);

CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL PRIMARY KEY,
    account_number VARCHAR(50) NOT NULL,
    account_type VARCHAR(20),
    initial_balance NUMERIC(15,2),
    account_status VARCHAR(20),
    client_id INT,
    CONSTRAINT fk_client FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE IF NOT EXISTS movements (
    id SERIAL PRIMARY KEY,
    movement_timestamp TIMESTAMP NOT NULL,
    movement_type VARCHAR(20),
    movement_value NUMERIC(15,2),
    balance NUMERIC(15,2),
    account_id INT UNIQUE,
    CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES accounts(id)
    );


-- Data insertion --
INSERT INTO persons (person_name, genre, age, identification, address, phone)
VALUES
    ('Jose Lema', 'MALE', 30, '1001234567', 'Otavalo sn y principal', '098254785'),
    ('Marianela Montalvo', 'FEMALE', 25, '1001234568', 'Amazonas y NNUU', '097548965'),
    ('Juan Osorio', 'MALE', 40, '1005678912', '13 junio y Equinoccial', '098874587')
ON CONFLICT (identification) DO NOTHING;


INSERT INTO clients (password_hash, client_status, person_id)
SELECT '$2a$10$7EqJtq98hPqEX7fNZaFWoO5f5J8bQxj5lHcG4z0f5zE7Yy1fQJ9lG', TRUE, p.id
FROM persons p
WHERE p.identification = '1001234567'
  AND NOT EXISTS (SELECT 1 FROM clients WHERE person_id = p.id);

INSERT INTO clients (password_hash, client_status, person_id)
SELECT '$2a$10$u1cXfQyQkYJvQkYJvQkYJvQkYJvQkYJvQkYJvQkYJvQkYJvQkYJ', TRUE, p.id
FROM persons p
WHERE p.identification = '1001234568'
  AND NOT EXISTS (SELECT 1 FROM clients WHERE person_id = p.id);

INSERT INTO clients (password_hash, client_status, person_id)
SELECT '$2a$10$9vQJtq98hPqEX7fNZaFWoO5f5J8bQxj5lHcG4z0f5zE7Yy1fQJ9lG', TRUE, p.id
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
