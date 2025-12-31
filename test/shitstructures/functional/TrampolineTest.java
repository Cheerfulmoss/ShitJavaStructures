package shitstructures.functional;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TrampolineTest {

    // Naive (non-memoized) recursive Fibonacci using Trampoline
    Trampoline<Integer> fib(int n) {
        if (n <= 1) {
            return Trampoline.done(n);
        }
        return Trampoline.more(() ->
                fib(n - 1).flatMap(a ->
                        fib(n - 2).map(b -> a + b)));
    }

    @Test
    void testFibOf0() {
        int result = fib(0).run();
        assertEquals(0, result); // fib(0) = 0
    }

    @Test
    void testFibOf1() {
        int result = fib(1).run();
        assertEquals(1, result); // fib(1) = 1
    }

    @Test
    void testFibOf5() {
        int result = fib(5).run();
        assertEquals(5, result); // fib(5) = 5
    }

    @Test
    void testFibOf10() {
        int result = fib(10).run();
        assertEquals(55, result); // fib(10) = 55
    }

    @Test
    void testStackSafetyWithGrowingInputs() {
        // Test that trampoline prevents stack overflow on increasingly deep inputs
        for (int i = 5; i <= 35; i++) {
            int result = fib(i).run();
            System.out.printf("fib(%d) = %d%n", i, result);
            assertTrue(result >= 0); // Basic sanity check
        }
    }

    @Test
    void testMapFunctionality() {
        int result = fib(7)
                .map(n -> n + 3)
                .run();
        assertEquals(13 + 3, result); // fib(7) = 13
    }

    @Test
    void testFlatMapFunctionality() {
        int result = fib(6)
                .flatMap(n -> Trampoline.done(n * 2))
                .run();
        assertEquals(16, result); // fib(6) = 8 → 8 * 2 = 16
    }
}
