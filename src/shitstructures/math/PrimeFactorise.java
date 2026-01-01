package shitstructures.math;

import shitstructures.AdaptivePQ;
import java.util.*;

public class PrimeFactorise {
    private static final AdaptivePQ<Integer> primes = new AdaptivePQ<>();
    private static final AdaptivePQ<Pair<Integer, Map<Integer, Integer>>> factorises = new AdaptivePQ<>();
    private static int maxCacheSize = Integer.MAX_VALUE;

    public static void setCacheSize(int cacheSize) {
        maxCacheSize = cacheSize;
    }

    public static void clearCaches() {
        primes.clear();
        factorises.clear();
    }

    private static boolean isPrime(int n) {
        if (primes.values().contains(n)) {
            return true;
        }

        if (n == 2) {
            return true;
        }

        if (n <= 1 || n % 2 == 0) {
            return false;
        }

        for (Integer p : primes.values()) {
            if (n % p == 0) {
                return false;
            }
        }

        int limit = (int)Math.sqrt(n);
        for (int i = 3; i <= limit; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static Map<Integer, Integer> factorise(int n) {
        Map<Integer, Integer> factors = new HashMap<>();

        int remaining = n;

        // Go through previous numbers we've factored and use those
        for (Pair<Integer, Map<Integer, Integer>> pFacs : factorises.values()) {
            while (remaining % pFacs.left == 0) {
                remaining /= pFacs.left;
                pFacs.right.forEach((factor, count) -> {
                    factors.merge(factor, count, Integer::sum);
                });
                factorises.updatePriority(pFacs,
                        factorises.getPriority(pFacs)
                                .map(x -> x + 1)
                                .orElse(0));
            }
        }

        // Go through cached primes
        for (Integer p : primes.values()) {
            while (remaining % p == 0) {
                remaining /= p;
                factors.merge(p, 1, Integer::sum);
                primes.updatePriority(p,
                        primes.getPriority(p)
                                .map(x -> x + 1)
                                .orElse(0));
            }
        }

        // Handle the special prime 2
        if (remaining % 2 == 0) {
            primes.insert(2, 1);
        }
        while (remaining % 2 == 0) {
            factors.merge(2, 1, Integer::sum);
            remaining /= 2;
            primes.updatePriority(2,
                    primes.getPriority(2)
                            .map(x -> x + 1)
                            .orElse(0));
        }

        // Find new primes
        for (int i = 3; i * i <= remaining; i += 2) {
            if (isPrime(i) && remaining % i == 0) {
                primes.insert(i, 0);

                while (remaining % i == 0) {
                    factors.merge(i, 1, Integer::sum);
                    remaining /= i;
                    primes.updatePriority(i,
                            primes.getPriority(i)
                                    .map(x -> x + 1)
                                    .orElse(0));
                }
            }
        }

        // OMG the remaining was a prime itself!
        if (remaining > 1) {
            factors.merge(remaining, 1, Integer::sum);
            if (!primes.insert(remaining, 0)) {
                primes.updatePriority(remaining,
                        primes.getPriority(remaining)
                                .map(x -> x + 1)
                                .orElse(0));
            }
        }

        factorises.insert(new Pair<>(n, factors), 0);

        while (primes.size() > maxCacheSize) {
            primes.removeMin();
        }
        while (factorises.size() > maxCacheSize) {
            factorises.removeMin();
        }

        return factors;
    }

    public static AdaptivePQ<Pair<Integer, Map<Integer, Integer>>> peekFactorisesCache() {
        return factorises;
    }

    public static AdaptivePQ<Integer> peekPrimesCache() {
        return primes;
    }

    public record Pair<L, R>(L left, R right) {}
}
