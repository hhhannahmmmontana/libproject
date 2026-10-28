package com.volodya.libproject.domain.services;

import com.volodya.libproject.domain.dto.BookLoanDto;
import com.volodya.libproject.domain.dto.ReaderDto;
import com.volodya.libproject.domain.exceptions.BookOutOfStockException;
import com.volodya.libproject.domain.exceptions.EntityNotFoundException;
import com.volodya.libproject.domain.mappers.BookLoanDtoEntityMapper;
import com.volodya.libproject.infrastructure.entites.BookLoanEntity;
import com.volodya.libproject.infrastructure.entites.enums.BookLoanStatus;
import com.volodya.libproject.infrastructure.repositories.BookLoanJpaRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class BookLoanService {

    public final static String ENTITY_NAME = "Book loan";

    private final BookService bookService;
    private final ReaderService readerService;
    private final BookLoanJpaRepository bookLoanJpaRepository;
    private final BookLoanDtoEntityMapper bookLoanMapper;

    public BookLoanDto createBookLoan(
        UUID bookId,
        UUID readerId,
        Instant dueDate
    ) {
        var book = bookService.getBookEntityById(bookId); // transactional, so it's probably okay
        if (book.getAvailableCopies() <= 0) { // it can't be less than zero, but i feel safer that way
            throw new BookOutOfStockException(book);
        }

        var reader = readerService.getReaderEntityById(readerId);
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        var loan = new BookLoanEntity(UUID.randomUUID(), book, reader, dueDate);
        return bookLoanMapper.toDto(bookLoanJpaRepository.save(loan));
    }

    BookLoanEntity getBookLoanEntityById(UUID id) {
        return bookLoanJpaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
    }

    public BookLoanDto getBookLoanDtoById(UUID id) {
        return bookLoanMapper.toDto(getBookLoanEntityById(id));
    }

    public BookLoanDto returnBook(UUID id) {
        var loan = getBookLoanEntityById(id);
        loan.setReturnDate(Instant.now());
        loan.setStatus(BookLoanStatus.RETURNED);
        loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies() + 1);
        return bookLoanMapper.toDto(loan);
    }

    public List<BookLoanDto> getReaderLoans(UUID readerId) {
        var reader = readerService.getReaderEntityById(readerId);
        var loans = bookLoanJpaRepository.findByReader(reader);
        return loans.stream().map(bookLoanMapper::toDto).toList();
    }

    public List<ReaderDto> getDebtors() {
        return bookLoanJpaRepository.findByDueDateLessThan(Instant.now())
            .stream()
            .map(bookLoanMapper::toDto)
            .map(BookLoanDto::reader)
            .toList();
    }
}
