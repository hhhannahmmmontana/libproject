package com.volodya.domain.services;

import java.util.function.Supplier;

public interface TransactionService {

    <T> T supplyTransactional(Supplier<T> supplier);
}
