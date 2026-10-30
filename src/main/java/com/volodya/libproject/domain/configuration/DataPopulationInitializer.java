package com.volodya.libproject.domain.configuration;

import com.volodya.libproject.domain.configuration.properties.DataPopulationConfigurationProperties;
import com.volodya.libproject.domain.configuration.properties.DataPopulationStrategy;
import com.volodya.libproject.domain.dto.BookDto;
import com.volodya.libproject.domain.dto.ReaderDto;
import com.volodya.libproject.domain.entities.Isbn;
import com.volodya.libproject.domain.exceptions.BookOutOfStockException;
import com.volodya.libproject.domain.exceptions.DataPopulationException;
import com.volodya.libproject.domain.services.BookLoanService;
import com.volodya.libproject.domain.services.BookService;
import com.volodya.libproject.domain.services.ReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.Year;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

@Component
@Profile("!prod")
@RequiredArgsConstructor
@Slf4j
public final class DataPopulationInitializer implements ApplicationRunner {

    private final DataPopulationConfigurationProperties configurationProperties;
    private final BookService bookService;
    private final ReaderService readerService;
    private final BookLoanService bookLoanService;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        if (!configurationProperties.isEnabled()) {
            return;
        }

        log.info("starting data population");
        var faker = new Faker();

        var persistentBooksAmount = bookService.countBooks();
        var persistentReadersAmount = readerService.countReaders();
        var persistentBookLoansAmount = bookLoanService.countBookLoans();

        if (configurationProperties.getStrategy() == DataPopulationStrategy.EMPTY_ONLY) {
            if (persistentBooksAmount != 0 || persistentReadersAmount != 0 || persistentBookLoansAmount != 0) {
                log.warn("database is not empty, stopping data population (strategy: empty-only)");
                return;
            }
        }

        log.debug("populating books");
        var booksAmount = configurationProperties.getDataAmount().getBooks();
        var books = new ArrayList<BookDto>(booksAmount);
        if (persistentBooksAmount > 0) {
            books.addAll(bookService.getAllBooks());
        }
        for (long i = persistentBooksAmount; i < booksAmount; ++i) {
            books.add(bookService.createBook(
                faker.book().title(),
                faker.book().author(),
                new Isbn(faker.regexify("\\d{10}|\\d{13}")),
                Year.from(faker.timeAndDate().past().atZone(ZoneId.systemDefault())),
                faker.number().numberBetween(0, 100)
            ));
        }
        log.info("books populated, total added data: {}", booksAmount - persistentBooksAmount);

        log.debug("populating readers");
        var readersAmount = configurationProperties.getDataAmount().getReaders();
        var readers = new ArrayList<ReaderDto>(readersAmount);
        if (persistentReadersAmount > 0) {
            readers.addAll(readerService.getAllReaders());
        }
        for (long i = persistentReadersAmount; i < readersAmount; ++i) {
            try {
                readers.add(readerService.createReader(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.internet().emailAddress(),
                    faker.phoneNumber().phoneNumber().replaceAll("[\\s\\-()]", "")
                ));
            } catch (DataIntegrityViolationException ex) {
                --i;
            }
        }
        log.info("readers populated, total added data: {}", readersAmount - persistentReadersAmount);

        log.debug("populating book loans");
        var bookLoansAmount = configurationProperties.getDataAmount().getBookLoans();
        if (bookLoansAmount > 0 && (booksAmount < 1 || readersAmount < 1)) {
            throw new DataPopulationException("To populate book loans db must contain at least 1 book and 1 reader");
        }

        for (long i = persistentBookLoansAmount; i < bookLoansAmount; ++i) {
            try {
                bookLoanService.createBookLoan(
                    faker.options().option(books.toArray(BookDto[]::new)).id(),
                    faker.options().nextElement(readers.toArray(ReaderDto[]::new)).id(),
                    faker.timeAndDate().between(
                        Instant.now().minus(5 * 365, ChronoUnit.DAYS),
                        Instant.now().plus(5 * 365, ChronoUnit.DAYS)
                    )
                );
            } catch (BookOutOfStockException ex) {
                --i;
            }
        }
        log.info("book loans populated, total added data: {}", bookLoansAmount - persistentBookLoansAmount);

        log.info("finished data population");
    }
}