CREATE TABLE book_loans
(
    id          UUID PRIMARY KEY,
    book_id     UUID                     NOT NULl REFERENCES books (id),
    reader_id   UUID                     NOT NULL REFERENCES readers (id),
    loan_date   TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT current_timestamp,
    due_date    TIMESTAMP WITH TIME ZONE NOT NULL,
    return_date TIMESTAMP WITH TIME ZONE,
    status      VARCHAR(16)              NOT NULL
);