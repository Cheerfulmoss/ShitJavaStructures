package shitstructures.functional;

@FunctionalInterface
public interface ThrowingRunnable {
    void run() throws Exception;
}
