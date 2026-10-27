package com.volodya.libproject.presentation.controllers;

import com.volodya.libproject.domain.dto.ReaderDto;
import com.volodya.libproject.domain.services.ReaderService;
import com.volodya.libproject.presentation.requests.readers.CreateReaderRequest;
import com.volodya.libproject.presentation.requests.readers.PatchReaderRequest;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/readers")
@RequiredArgsConstructor
public final class ReaderController {

    private final ReaderService readerService;

    @PostMapping
    public ResponseEntity<ReaderDto> createReader(@RequestBody CreateReaderRequest request) {
        return new ResponseEntity<>(readerService.createReader(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.phone()
        ), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderDto> getReaderById(@PathParam("id") UUID id) {
        return ResponseEntity.ok(readerService.getReaderById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ReaderDto> patchReader(
        @PathParam("id") UUID id,
        @RequestBody PatchReaderRequest request
    ) {
        return ResponseEntity.ok(readerService.patchReader(
            id,
            request.firstName(),
            request.lastName(),
            request.email(),
            request.phone(),
            request.registrationDate()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReader(@PathParam("id") UUID id) {
        readerService.deleteReader(id);
        return ResponseEntity.noContent().build();
    }
}
