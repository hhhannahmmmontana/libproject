create table readers
(
    id                UUID PRIMARY KEY,
    first_name        VARCHAR(255)             NOT NULL,
    last_name         VARCHAR(255)             NOT NULL,
    email             VARCHAR(255)             NOT NULL UNIQUE,
    phone             VARCHAR(16)              NOT NULL UNIQUE,
    registration_date TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp
);