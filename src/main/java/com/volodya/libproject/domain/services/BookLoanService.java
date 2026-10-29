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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
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
        log.debug("creating book loan for bookId {} and readerId {}, due date: {}", bookId, readerId, dueDate);
        var book = bookService.getBookEntityById(bookId); // transactional, so it's probably okay
        log.trace("checking if book {} has any available copies", bookId);
        if (book.getAvailableCopies() <= 0) { // it can't be less than zero, but i feel safer that way
            throw new BookOutOfStockException(book);
        }
        var reader = readerService.getReaderEntityById(readerId);

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        log.trace("book's available copies decremented to {}", book.getAvailableCopies());

        var loan = new BookLoanEntity(UUID.randomUUID(), book, reader, dueDate);
        loan = bookLoanJpaRepository.save(loan);
        log.info("loan for {} readerId with id {} created (bookId: {})", readerId, loan.getId(), bookId);
        return bookLoanMapper.toDto(loan);
    }

    BookLoanEntity getBookLoanEntityById(UUID id) {
        log.debug("searching for book loan {}", id);
        return bookLoanJpaRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(ENTITY_NAME, id));
    }

    public BookLoanDto getBookLoanDtoById(UUID id) {
        return bookLoanMapper.toDto(getBookLoanEntityById(id));
    }

    public BookLoanDto returnBook(UUID id) {
        log.debug("returning book loan {}", id);
        var loan = getBookLoanEntityById(id);

        loan.setReturnDate(Instant.now());
        log.trace("{} book loan's return date set to {}", loan.getId(), loan.getReturnDate());

        loan.setStatus(BookLoanStatus.RETURNED);
        log.trace("{} book loan's status set to {}", loan.getId(), loan.getStatus());

        loan.getBook().setAvailableCopies(loan.getBook().getAvailableCopies() + 1);
        log.trace("book's available copies incremented to {}", loan.getBook().getAvailableCopies());
        log.info("loan {} returned", loan.getId());
        return bookLoanMapper.toDto(loan);
    }

    public List<BookLoanDto> getReaderLoans(UUID readerId) {
        log.debug("searching for loans for reader {}", readerId);
        var reader = readerService.getReaderEntityById(readerId);
        log.trace("executing findByReader repository method");
        var loans = bookLoanJpaRepository.findByReader(reader);
        log.debug("found {} reader's loans, amount: {}", reader, loans.size());
        return loans.stream().map(bookLoanMapper::toDto).toList();
    }

    public List<ReaderDto> getDebtors() {
        log.debug("searching for debtors");
        var debtors = bookLoanJpaRepository.findByDueDateLessThan(Instant.now())
            .stream()
            .map(bookLoanMapper::toDto)
            .map(BookLoanDto::reader)
            .toList();
        log.debug("found debtors ({})", debtors.size());
        return debtors;
    }
}
