package net.tifoha.utils.debug;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class CyclicStopwatch {
    long nanos = 0;
    private long start = -1;

    public CyclicStopwatch start() {
        start = System.nanoTime();
        return this;
    }

    public void stop() {
        if (start < 0) {
            throw new IllegalArgumentException();
        }

        nanos += System.nanoTime() - start;
        start = -1;
    }

    public void reset() {
        nanos = 0;
        start = -1;
    }

    public long getNanos() {
        return nanos;
    }

    public long getMillis() {
        return TimeUnit.NANOSECONDS.toMillis(nanos);
    }

    public long getSeconds() {
        return TimeUnit.NANOSECONDS.toSeconds(nanos);
    }

    public long getMinutes() {
        return TimeUnit.NANOSECONDS.toMinutes(nanos);
    }

    public Duration getDuration() {
        return Duration.ofNanos(nanos);
    }
}
