package shitstructures.functional;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.function.Supplier;

public final class LockedSession<T> {
    private final Supplier<T> supplier;

    private LockedSession(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> LockedChain<T> start(Supplier<T> supplier) {
        ReentrantLock newLock = new ReentrantLock();
        newLock.lock(); // acquire lock immediately on start

        return new LockedChain<>(newLock, supplier);
    }

    public static <T> LockedChain<T> start(ReentrantLock lock, Supplier<T> supplier) {
        lock.lock(); // acquire lock immediately on start
        return new LockedChain<>(lock, supplier);
    }

    @Override
    public String toString() {
        return "LockedSession[" + supplier.get() + "]";
    }

    public static class LockedChain<T> {
        private final ReentrantLock lock;
        private final Supplier<T> supplier;

        private LockedChain(ReentrantLock lock, Supplier<T> supplier) {
            this.lock = lock;
            this.supplier = supplier;
        }

        public <R> LockedChain<R> transformHold(Function<T, R> mapper) {
            T value = supplier.get();     // already inside locked context
            R result = mapper.apply(value);
            return new LockedChain<>(lock, () -> result);
        }

        public <R> LockedChain<R> transformChangeBefore(ReentrantLock lock, Function<T, R> mapper) {
            // Acquiring the new lock before releasing old prevents gaps but can cause increased
            //     risk of deadlocks.
            lock.lock();
            this.lock.unlock();

            T value = supplier.get();
            R result = mapper.apply(value);
            return new LockedChain<>(lock, () -> result);
        }

        public <R> LockedChain<R> transformChangeAfter(ReentrantLock lock, Function<T, R> mapper) {
            // We release the lock first, then acquire the new one. Has a very small gap but less
            //     risk of deadlocks.
            this.lock.unlock();
            lock.lock();

            T value = supplier.get();
            R result = mapper.apply(value);
            return new LockedChain<>(lock, () -> result);
        }

        public T transformEnd() {
            try {
                return supplier.get();
            } finally {
                lock.unlock();
            }
        }

        public T get() {
            return transformEnd();
        }
    }
}
