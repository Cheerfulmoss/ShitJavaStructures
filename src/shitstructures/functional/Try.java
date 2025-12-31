package shitstructures.functional;

import java.util.*;
import java.util.function.*;

public final class Try<T> {
    private final Either<Exception, T> either;

    private Try(Either<Exception, T> either) {
        this.either = either;
    }

    private static <T> Try<T> attempt(ThrowingSupplier<T> supplier) {
        try {
            return success(supplier.get());
        } catch (Exception e) {
            return failure(e);
        }
    }

    public static <T> Try<T> success(T value) {
        return new Try<>(Either.right(value));
    }

    public static <T> Try<T> failure(Exception e) {
        return new Try<>(Either.left(e));
    }

    public static <T> Try<T> of(ThrowingSupplier<T> supplier) {
        return attempt(supplier);
    }

    public static Try<Void> run(ThrowingRunnable runnable) {
        return Try.of(() -> {
            runnable.run();
            return null;
        });
    }

    public static <T, U> Function<T, Try<U>> lift(ThrowingFunction<T, U> fn) {
        return t -> attempt(() -> fn.apply(t));
    }

    public boolean isSuccess() {
        return either.isRight();
    }

    public boolean isFailure() {
        return either.isLeft();
    }

    public T get() throws RuntimeException {
        return either.fold(
                ex -> { throw new RuntimeException("No value present, this is a failure", ex); },
                Function.identity()
        );
    }

    public Exception getCause() throws NoSuchElementException {
        return either.fold(
                Function.identity(),
                _ -> { throw new NoSuchElementException("No exception present, this is a success"); }
        );
    }

    public <U> Try<U> map(ThrowingFunction<? super T, ? extends U> mapper) {
        if (isFailure()) {
            return failure(getCause());
        }
        return attempt(() -> mapper.apply(get()));
    }

    public <U> Try<U> flatMap(Function<? super T, Try<U>> mapper) {
        if (isFailure()) {
            return failure(getCause());
        }
        return mapper.apply(get());
    }

    public Try<T> mapFailure(Function<? super Exception, ? extends Exception> mapper) {
        if (isSuccess()) {
            return success(get());
        }
        return failure(mapper.apply(getCause()));
    }

    public Try<T> flatMapFailure(Function<? super Exception, Try<T>> mapper) {
        if (isSuccess()) {
            return success(get());
        }
        return mapper.apply(getCause());
    }

    public <U> Try<U> bimap(Function<? super Exception, ? extends Exception> lMapper,
                            ThrowingFunction<? super T, ? extends U> rMapper) {
        return isSuccess()
                ? map(rMapper)
                : failure(lMapper.apply(getCause()));
    }

    public <U> Try<U> biFlatMap(Function<? super Exception, Try<U>> lMapper,
                            Function<? super T, Try<U>> rMapper) {
        return isSuccess()
                ? rMapper.apply(get())
                : lMapper.apply(getCause());
    }

    public <E extends Throwable> T getOrElseThrow(Function<? super Throwable, ? extends E> mapper) throws E {
        if (isSuccess()) return get();
        throw mapper.apply(getCause());
    }

    public T getOrElseThrow() throws Exception {
        if (isSuccess()) return get();
        throw getCause();
    }

    public <E extends Throwable> T wrappingGetOrElseThrow(Class<E> exceptionClass) throws E {
        if (isSuccess()) return get();

        Throwable cause = getCause();
        if (exceptionClass.isInstance(cause)) {
            throw exceptionClass.cast(cause);
        } else {
            // Wrap in the exceptionClass, if possible, else fallback to RuntimeException
            try {
                // Try to create a new instance of E with cause as parameter
                throw exceptionClass
                        .getConstructor(String.class, Throwable.class)
                        .newInstance("Wrapped exception", cause);
            } catch (ReflectiveOperationException | RuntimeException reflectionEx) {
                // If we can't construct it, fallback to RuntimeException
                throw new RuntimeException("Failed to wrap exception", cause);
            }
        }
    }

    public <E extends Throwable> T getOrElseThrow(Class<E> exceptionClass) throws E {
        if (isSuccess()) return get();
        Throwable cause = getCause();
        if (exceptionClass.isInstance(cause)) {
            throw exceptionClass.cast(cause);
        } else {
            throw new RuntimeException("Unexpected exception type: " + cause.getClass(), cause);
        }
    }

    public T getOrElse(T fallback) {
        return either.fold(
                _ -> fallback,
                Function.identity()
        );
    }

    public Try<T> filter(Predicate<? super T> predicate, Supplier<Exception> ifFailed) {
        if (isFailure()) {
            return failure(getCause());
        }
        return predicate.test(get()) ? success(get()) : failure(ifFailed.get());
    }

    public <U> U fold(Function<? super Exception, U> onFailure, Function<? super T, U> onSuccess) {
        return either.fold(onFailure, onSuccess);
    }

    public Try<T> recover(ThrowingFunction<? super Exception, ? extends T> recoveryFn) {
        if (isSuccess()) {
            return success(get());
        }
        return attempt(() -> recoveryFn.apply(getCause()));
    }

    public Try<T> recoverWith(Function<? super Exception, Try<T>> recoveryFn) {
        if (isSuccess()) return success(get());
        try {
            return recoveryFn.apply(getCause());
        } catch (Exception e) {
            return failure(e);
        }
    }

    /* Sadly this is non-functional :( but as it's useful it's here.
     * This allows us to "terminate" a chain with a side effect.
     */
    public void match(Consumer<? super Exception> onFailure, Consumer<? super T> onSuccess) {
        either.fold(
                ex -> { onFailure.accept(ex); return null; },
                val -> { onSuccess.accept(val); return null; }
        );
    }

    /* Returns "this" because it's supposed to have "no effect".
     * This is sadly a non-functional thing because action is a side effect.
     * */
    public Try<T> onMatch(Consumer<? super Exception> onFailure, Consumer<? super T> onSuccess) {
        match(onFailure, onSuccess);
        return this;
    }

    /* Returns "this" because it's supposed to have "no effect".
    * This is sadly a non-functional thing because action is a side effect.
    * */
    public Try<T> onSuccess(Consumer<? super T> action) {
        if (isSuccess()) action.accept(get());
        return this;
    }

    /* Returns "this" because it's supposed to have "no effect".
     * This is sadly a non-functional thing because action is a side effect.
     * */
    public Try<T> onFailure(Consumer<? super Exception> action) {
        if (isFailure()) action.accept(getCause());
        return this;
    }

    public Optional<T> toOptional() {
        return either.toOptional();
    }

    public Either<Exception, T> toEither() {
        return either;
    }

    @Override
    public String toString() {
        return isSuccess() ? ("Success(" + get() + ")") : ("Failure(" + getCause() + ")");
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Try<?> other)) return false;
        return either.equals(other.either);
    }

    @Override
    public int hashCode() {
        return either.hashCode();
    }
}
