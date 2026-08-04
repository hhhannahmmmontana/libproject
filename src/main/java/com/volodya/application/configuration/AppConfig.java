package com.volodya.application.configuration;

import com.volodya.domain.repositories.BookRepository;
import com.volodya.domain.services.BookService;
import com.volodya.domain.services.BookServiceImpl;
import com.volodya.domain.services.TransactionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@ComponentScan("com.volodya")
@EnableWebMvc
public abstract class AppConfig {

    @Bean
    public BookService bookService(
        BookRepository bookRepository,
        TransactionService transactionService
    ) {
        return new BookServiceImpl(
            bookRepository,
            transactionService
        );
    }
}
