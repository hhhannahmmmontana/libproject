package com.volodya.libproject.domain.configuration;

import com.volodya.libproject.domain.configuration.properties.DataPopulationConfigurationProperties;
import com.volodya.libproject.domain.dto.BookDto;
import com.volodya.libproject.domain.dto.ReaderDto;
import com.volodya.libproject.domain.entities.Isbn;
import com.volodya.libproject.domain.exceptions.BookOutOfStockException;
import com.volodya.libproject.domain.exceptions.DataPopulationException;
import com.volodya.libproject.domain.services.BookLoanService;
import com.volodya.libproject.domain.services.BookService;
import com.volodya.libproject.domain.services.ReaderService;
import lombok.RequiredArgsConstructor;
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
public class DataPopulationInitializer implements ApplicationRunner {

    private final DataPopulationConfigurationProperties configurationProperties;
    private final BookService bookService;
    private final ReaderService readerService;
    private final BookLoanService bookLoanService;

    @Override
    public void run(@NonNull ApplicationArguments args) throws Exception {
        if (!configurationProperties.isEnabled()) {
            return;
        }
        assert configurationProperties.getDataAmount() != null;
        var faker = new Faker();

        var booksAmount = configurationProperties.getDataAmount().getBooks();
        var books = new ArrayList<BookDto>(booksAmount);
        for (int i = 0; i < booksAmount; ++i) {
            books.add(bookService.createBook(
                faker.book().title(),
                faker.book().author(),
                new Isbn(faker.regexify("\\d{10}|\\d{13}")),
                Year.from(faker.timeAndDate().past().atZone(ZoneId.systemDefault())),
                faker.number().numberBetween(0, 100)
            ));
        }

        var readersAmount = configurationProperties.getDataAmount().getReaders();
        var readers = new ArrayList<ReaderDto>(readersAmount);
        for (int i = 0; i < readersAmount; ++i) {
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

        var bookLoansAmount = configurationProperties.getDataAmount().getBookLoans();
        if (bookLoansAmount > 0 && (booksAmount < 1 || readersAmount < 1)) {
            throw new DataPopulationException("To populate book loans db must contain at least 1 book and 1 reader");
        }

        for (int i = 0; i < bookLoansAmount; ++i) {
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
    }
}