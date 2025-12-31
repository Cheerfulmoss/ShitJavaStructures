package shitstructures.functional;

import java.util.concurrent.Future;
import java.util.function.*;

public final class LazyAsync<T> {
    private final Lazy<Async<T>> lazy;

    private LazyAsync(Lazy<Async<T>> lazy) {
        this.lazy = lazy;
    }

    public static <T> LazyAsync<T> of(Supplier<Async<T>> supplier) {
        return new LazyAsync<>(Lazy.memoize(supplier));
    }

    public Async<T> createAsyncTask() {
        return lazy.get();
    }

    public Future<Try<T>> runAsync() {
        return createAsyncTask().run();
    }

    public Try<T> await() {
        return Try.of(this::createAsyncTask)
                .map(Async::await)
                .flatMap(Function.identity());
    }

    public <U> LazyAsync<U> map(ThrowingFunction<? super T, ? extends U> fn) {
        return new LazyAsync<>(lazy.map(async -> async.map(fn)));
    }

    public <U> LazyAsync<U> flatMap(Function<? super T, LazyAsync<U>> fn) {
        return new LazyAsync<>(lazy.flatMap(async ->
                Lazy.memoize(() -> {
                    Try<T> result = Try.of(() -> async.run().get()).flatMap(Function.identity());

                    if (result.isFailure()) {
                        Exception ex = result.getCause();
                        return Async.of(async.getExecutor(), () -> {throw ex;});
                    }
                    return fn.apply(result.get()).createAsyncTask();
                })
        ));
    }
}
