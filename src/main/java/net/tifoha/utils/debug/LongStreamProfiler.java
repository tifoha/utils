package net.tifoha.utils.debug;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.LongConsumer;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

@Slf4j
@RequiredArgsConstructor
public class LongStreamProfiler implements LongConsumer {
    private AtomicInteger counter = new AtomicInteger();
    private final int chunk;

    public static void main(String[] args) {
        LongStream.range(1, 10)
                .peek(new LongStreamProfiler(2))
                .count();
    }

    @Override
    public void accept(long value) {
        int index = counter.incrementAndGet();
        if (index % chunk == 0) {
            log.debug("Processed {} values", index);
        }
    }

    public int getCounter() {
        return counter.get();
    }
}
