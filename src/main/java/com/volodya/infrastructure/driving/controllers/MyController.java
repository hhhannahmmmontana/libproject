package com.volodya.infrastructure.driving.controllers;

import com.volodya.infrastructure.driven.entities.BookJpaEntity;
import com.volodya.infrastructure.driven.repositories.BookJpaRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class MyController {

    private final BookJpaRepository repository;

    @GetMapping
    public ResponseEntity<@NonNull List<BookJpaEntity>> hello() {
        return ResponseEntity.ok(repository.findAll());
    }
}
