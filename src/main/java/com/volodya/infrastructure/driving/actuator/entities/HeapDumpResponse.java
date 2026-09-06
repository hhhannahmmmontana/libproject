package com.volodya.infrastructure.driving.actuator.entities;

import java.io.File;

public record HeapDumpResponse(
    File heapDumpFile
) {}
