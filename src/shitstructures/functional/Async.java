package shitstructures.functional;

import java.util.concurrent.*;
import java.util.function.*;

public final class Async<T> {
    private final ExecutorService executor;
    private final Callable<T> task;

    public Async(ExecutorService executor, Callable<T> task) {
        this.executor = executor;
        this.task = task;
    }

    public static <T> Async<T> of(ExecutorService executor, Callable<T> task) {
        return new Async<>(executor, task);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public Future<Try<T>> run() {
        return executor.submit(() -> Try.of(task::call));
    }

    public Try<T> await() {
        return Try.of(this::run)
                .map(Future::get)
                .flatMap(Function.identity());
    }

    public <U> Async<U> map(ThrowingFunction<? super T, ? extends U> mapper) {
        return new Async<>(executor, () -> {
            T value = task.call(); // If this fails, Try catches it during `run()`
            return mapper.apply(value);
        });
    }

    public <U> Async<U> flatMap(Function<? super T, Async<U>> binder) {
        return new Async<>(executor, () -> {
            T value = task.call();
            return binder.apply(value).task.call();
        });
    }
}
