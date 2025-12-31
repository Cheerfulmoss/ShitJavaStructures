package shitstructures.functional;

import java.util.function.*;

public final class Sync<T> {
    private final SyncLock lock;
    private final T value;

    private Sync(SyncLock lock, Supplier<T> supplier) {
        this.lock = lock;
        synchronized (lock.lock()) {
            this.value = supplier.get();
        }
    }

    public static <T> Sync<T> of(Object lock, Supplier<T> supplier) {
        SyncLock newLock = new SyncLock(lock);
        return new Sync<>(newLock, supplier);
    }

    public static <T> Sync<T> of(Supplier<T> supplier) {
        SyncLock newLock = new SyncLock();
        return new Sync<>(newLock, supplier);
    }

    public <R> Sync<R> transform(Function<T, R> mapper) {
        synchronized (lock.get()) {
            R result = mapper.apply(value);
            return new Sync<>(lock, () -> result);
        }
    }

    public <R> Sync<R> transform(Object lock, Function<T, R> mapper) {
        SyncLock newLock = new SyncLock(lock);

        synchronized (newLock.get()) {
            R result = mapper.apply(value);
            return new Sync<>(newLock, () -> result);
        }
    }

    public <R> Sync<R> compose(Function<T, Sync<R>> binder) {
        synchronized (lock.get()) {
            Sync<R> resultSync = binder.apply(value);
            R result = resultSync.get();
            return new Sync<>(lock, () -> result);
        }
    }

    public <R> Sync<R> compose(Object lock, Function<T, Sync<R>> binder) {
        SyncLock newLock = new SyncLock(lock);

        synchronized (newLock.get()) {
            Sync<R> syncResult = binder.apply(value);
            R result = syncResult.get();
            return new Sync<>(newLock, () -> result);
        }
    }

    public T get() {
        synchronized (lock.get()) {
            return value;
        }
    }

    @Override
    public String toString() {
        return "Sync[" + get() + "]";
    }

    private record SyncLock(Object lock) {
        public SyncLock() {
                this(new Object());
            }
        public Object get() {
            return lock;
        }
    }
}
