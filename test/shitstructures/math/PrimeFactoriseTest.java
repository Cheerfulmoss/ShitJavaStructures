package shitstructures.math;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;
import java.util.*;

public class PrimeFactoriseTest {
    private final Random random = new Random();
    private final int testCount = 1000;

    private int randomNumber() {
        return random.nextInt(1_000_000_000);
    }

    @BeforeEach
    public void setUp() {
        PrimeFactorise.setCacheSize(Integer.MAX_VALUE);
        PrimeFactorise.clearCaches();
    }

    @RepeatedTest(testCount)
    public void testPrimeFactorise() {
        PrimeFactorise.setCacheSize(Integer.MAX_VALUE);
        int value = randomNumber();
        Map<Integer, Integer> fact = PrimeFactorise.factorise(value);
        int reconstruct = 1;
        for (Map.Entry<Integer, Integer> entry : fact.entrySet()) {
            reconstruct *= (int)Math.pow(entry.getKey(), entry.getValue()) ;
        }
        System.out.printf("%s -> %d%n", fact, reconstruct);
        System.out.println(PrimeFactorise.peekPrimesCache());
        System.out.println(PrimeFactorise.peekFactorisesCache());
        assertEquals(value, reconstruct);
    }

    @RepeatedTest(testCount)
    public void testPrimeFactoriseLimitedCache() {
        PrimeFactorise.setCacheSize(10);
        int value = randomNumber();
        Map<Integer, Integer> fact = PrimeFactorise.factorise(value);
        int reconstruct = 1;
        for (Map.Entry<Integer, Integer> entry : fact.entrySet()) {
            reconstruct *= (int)Math.pow(entry.getKey(), entry.getValue()) ;
        }
        System.out.printf("%s -> %d%n", fact, reconstruct);
        System.out.println(PrimeFactorise.peekPrimesCache());
        System.out.println(PrimeFactorise.peekFactorisesCache());
        assertEquals(value, reconstruct);
    }

    @Test
    public void testPrimeFactoriseManyWithCache() {
        for (int i = 0; i < testCount; i++) {
            testPrimeFactorise();
        }
    }

    @Test
    public void testPrimeFactoriseManyWithLimitedCache() {
        for (int i = 0; i < testCount; i++) {
            testPrimeFactoriseLimitedCache();
        }
    }
}
