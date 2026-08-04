package com.volodya.application.services;

import com.volodya.domain.services.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public final class TransactionServiceImpl implements TransactionService {

    @Override
    @Transactional
    public <T> T supplyTransactional(Supplier<T> supplier) {
        return supplier.get();
    }
}
