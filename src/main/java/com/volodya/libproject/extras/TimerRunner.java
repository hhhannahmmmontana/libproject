package com.volodya.libproject.extras;

import org.springframework.util.StopWatch;

import java.util.function.Supplier;

public final class TimerRunner {

    private TimerRunner() {}

    public static <T> TimedExecution<T> supply(Supplier<T> supplier) {
        var timer = new StopWatch();
        var data = supplier.get();
        return new TimedExecution<>(data, timer.getTotalTimeMillis());
    }
}
