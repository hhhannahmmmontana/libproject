CREATE TABLE books
(
    id               UUID PRIMARY KEY,
    title            VARCHAR(255) NOT NULL,
    author           VARCHAR(255) NOT NULL,
    isbn             VARCHAR(13)  NOT NULL UNIQUE,
    publication_year INT          NOT NULL,
    total_copies     BIGINT       NOT NULL,
    available_copies BIGINT       NOT NULL,
    version          BIGINT       NOT NULL,

    CONSTRAINT uq_title_author UNIQUE (title, author)
);