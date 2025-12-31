package shitstructures.functional;

import java.util.function.*;

public final class Lazy<T> {
    private final Supplier<T> supplier;
    private T value;
    private boolean evaluated = false;
    private final boolean memoize;

    private Lazy(Supplier<T> supplier, boolean memoize) {
        if (supplier == null) {
            throw new NullPointerException("supplier is null");
        }

        this.supplier = supplier;
        this.memoize = memoize;
    }

    /** Create a Lazy that caches the value after the first computation. */
    public static <T> Lazy<T> memoize(Supplier<T> supplier) {
        return new Lazy<>(supplier, true);
    }

    /** Create a Lazy that runs the supplier every time (no caching). */
    public static <T> Lazy<T> defer(Supplier<T> supplier) {
        return new Lazy<>(supplier, false);
    }

    /** Returns the memoized value, or computes it once if not evaluated yet. */
    public T get() {
        if (!memoize) {
            return supplier.get();
        }

        if (!evaluated) {
            value = supplier.get();
            evaluated = true;
        }
        return value;
    }

    public T run() {
        return get();
    }

    public <R> Lazy<R> map(Function<T, R> mapper) {
        return new Lazy<>(() -> mapper.apply(get()), memoize);
    }

    public <R> Lazy<R> flatMap(Function<T, Lazy<R>> binder) {
        return new Lazy<>(() -> binder.apply(get()).get(), memoize);
    }

    @Override
    public String toString() {
        return "Lazy[" + (evaluated ? value : "?") + "]";
    }
}
