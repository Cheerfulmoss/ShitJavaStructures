package shitstructures.functional;

import org.junit.jupiter.api.*;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class LazyAsyncTest {
    private static ExecutorService executor;

    @BeforeAll
    static void setup() {
        executor = Executors.newFixedThreadPool(2);
    }

    @AfterAll
    static void teardown() {
        executor.shutdown();
    }

    @Test
    void lazyEvaluation() {
        AtomicBoolean evaluated = new AtomicBoolean(false);

        LazyAsync<String> lazyAsync = LazyAsync.of(() ->
                Async.of(executor, () -> {
                    evaluated.set(true);
                    return "hello";
                })
        );

        // Should not have evaluated yet
        assertFalse(evaluated.get(), "Should not evaluate until forced");

        // Force evaluation
        Async<String> async = lazyAsync.createAsyncTask();

        // Still lazy, only runs when run() is called
        assertFalse(evaluated.get(), "Should not evaluate until run() called");

        // Now run the async
        Try<String> result = async.await();

        assertTrue(result.isSuccess(), "Async run failed");

        assertTrue(evaluated.get(), "Supplier should have been evaluated");
        assertEquals("hello", result.get());
    }

    @Test
    void mapTransformsValue() {
        LazyAsync<Integer> lazyAsync = LazyAsync.of(() ->
                Async.of(executor, () -> 10)
        );

        LazyAsync<String> mapped = lazyAsync.map(i -> "Number: " + i);

        Try<String> result = mapped.await();

        assertTrue(result.isSuccess(), "Async run failed");
        assertEquals("Number: 10", result.get());
    }

    @Test
    void flatMapChainsLazyAsync() {
        LazyAsync<Integer> lazyAsync = LazyAsync.of(() ->
                Async.of(executor, () -> 5)
        );

        LazyAsync<Integer> chained = lazyAsync.flatMap(i ->
                LazyAsync.of(() -> Async.of(executor, () -> i * 2))
        );

        Try<Integer> result = chained.await();

        assertTrue(result.isSuccess(), "Async run failed");
        assertEquals(10, result.get());
    }

    @Test
    void propagatesFailure() {
        Exception expected = new RuntimeException("fail");

        LazyAsync<String> failing = LazyAsync.of(() ->
                Async.of(executor, () -> {
                    throw expected;
                })
        );

        Async<String> async = failing.createAsyncTask();
        Try<String> result = async.await();

        assertTrue(result.isFailure());
        assertEquals(expected, result.getCause());
    }

    @Test
    void flatMapWithFailurePropagates() {
        Exception expected = new RuntimeException("fail");

        LazyAsync<Integer> failing = LazyAsync.of(() ->
                Async.of(executor, () -> {
                    throw expected;
                })
        );

        LazyAsync<Integer> flatMapped = failing.flatMap(i ->
                LazyAsync.of(() -> Async.of(executor, () -> i * 2))
        );

        Try<Integer> result = flatMapped.await();

        assertTrue(result.isFailure());
        assertEquals(expected, result.getCause());
    }
}
