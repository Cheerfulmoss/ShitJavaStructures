package shitstructures.functional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

class SyncTest {

    static class Counter {
        int count = 0;

        void increment() {
            count++;
        }

        int get() {
            return count;
        }
    }

    @Test
    void testSingleThreadedMap() {
        Object lock = new Object();
        Counter counter = new Counter();

        Sync<Counter> syncCounter = Sync.of(lock, () -> counter);

        Sync<Integer> incremented = syncCounter.transform(c -> {
            c.increment();
            return c.get();
        });

        assertEquals(1, incremented.get());
        assertEquals(1, counter.get());
    }

    @Test
    void testChainingWithDifferentLocks() {
        Object lock1 = new Object();
        Object lock2 = new Object();
        Counter counter = new Counter();

        Sync<Counter> syncCounter = Sync.of(lock1, () -> counter);

        Sync<Integer> result = syncCounter
                .transform(c -> {
                    c.increment();
                    return c.get();
                })
                .transform(lock2, i -> i + 10);

        assertEquals(11, result.get());
        assertEquals(1, counter.get());
    }

    @Test
    void testFmapChaining() {
        Object lock = new Object();
        Counter counter = new Counter();

        Sync<Counter> syncCounter = Sync.of(lock, () -> counter);

        Sync<Integer> result = syncCounter.compose(c -> {
            c.increment();
            return Sync.of(lock, c::get);
        });

        assertEquals(1, result.get());
        assertEquals(1, counter.get());
    }

    @Test
    void testConcurrency() throws InterruptedException {
        Object lock = new Object();
        Counter counter = new Counter();
        counter.count = 0;

        Sync<Counter> syncCounter = Sync.of(lock, () -> counter);

        int threads = 10;
        int incrementsPerThread = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(threads);

        Runnable task = () -> {
            for (int i = 0; i < incrementsPerThread; i++) {
                syncCounter.transform(c -> {
                    c.increment();
                    return c.get();
                }).get();
            }
        };

        for (int i = 0; i < threads; i++) {
            executor.submit(task);
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);
        assertTrue(finished, "Executor did not finish in time");

        int expected = threads * incrementsPerThread;
        assertEquals(expected, counter.get(), "Counter value mismatch after concurrency test");
    }

    @Test
    void testSyncWithExternalSynchronized() throws InterruptedException {
        Object sharedLock = new Object();
        Counter counter = new Counter();

        Sync<Counter> syncCounter = Sync.of(sharedLock, () -> counter);

        int threads = 5;
        int incrementsPerThread = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(threads * 2);

        // Task using Sync class
        Runnable syncTask = () -> {
            for (int i = 0; i < incrementsPerThread; i++) {
                syncCounter.transform(c -> {
                    c.increment();
                    return c.get();
                }).get();
            }
        };

        // Task using manual synchronized block on the same lock
        Runnable manualSyncTask = () -> {
            for (int i = 0; i < incrementsPerThread; i++) {
                synchronized (sharedLock) {
                    counter.increment();
                }
            }
        };

        for (int i = 0; i < threads; i++) {
            executor.submit(syncTask);
            executor.submit(manualSyncTask);
        }

        executor.shutdown();
        boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);
        assertTrue(finished, "Executor did not finish in time");

        int expected = threads * incrementsPerThread * 2; // both tasks incrementing
        assertEquals(expected, counter.get(), "Counter value mismatch with mixed synchronization");
    }
}
