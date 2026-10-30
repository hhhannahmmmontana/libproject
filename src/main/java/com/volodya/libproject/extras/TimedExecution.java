package com.volodya.libproject.extras;

public record TimedExecution<T>(T data, long timeMs) {}
