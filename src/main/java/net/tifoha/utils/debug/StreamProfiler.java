package net.tifoha.utils.debug;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * @author Vitalii Sereda
 */
@Slf4j
@RequiredArgsConstructor
public class StreamProfiler<T> implements Consumer<T> {
    private AtomicInteger counter = new AtomicInteger();
    private final int chunk;

    public static void main(String[] args) {
        IntStream.range(1, 10)
                .boxed()
                .peek(new StreamProfiler<Integer>(2))
                .count();
    }

    @Override
    public void accept(T t) {
        int index = counter.incrementAndGet();
        if (index % chunk == 0) {
            log.debug("Processed {} values", index);
        }
    }
}
