package shitstructures.functional;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public abstract class Either<L, R> {

    public abstract boolean isLeft();
    public abstract L getLeft() throws NoSuchElementException;
    public abstract R getRight() throws NoSuchElementException;

    /* fmapping methods */
    public abstract <U> Either<U, R> flatMapLeft(Function<? super L, Either<U, R>> mapper);
    public abstract <U, V> Either<U, V> biFlatMap(Function<? super L, Either<U, V>> lMapper,
                                                  Function<? super R, Either<U, V>> rMapper);
    public abstract <U> Either<L, U> flatMap(Function<? super R, Either<L, U>> mapper);

    /* Allow converting to optional if we need */
    public abstract Optional<R> toOptional();

    public abstract <U> U fold(Function<? super L, U> lMapper, Function<? super R, U> rMapper);

    /* Constructors */
    public static <L, R> Either<L, R> left(final L left) {
        return new Left<>(left);
    }

    public static <L, R> Either<L, R> right(final R right) {
        return new Right<>(right);
    }

    /* isRight can be derived from just isLeft so we implement here */
    public final boolean isRight() {
        return !isLeft();
    }

    /* Mapping methods, can be derived from fmap */
    public final <U> Either<U, R> mapLeft(Function<? super L, ? extends U> mapper) {
        return this.flatMapLeft(l -> left(mapper.apply(l)));
    }

    public final <U, V> Either<U, V> bimap(Function<? super L, ? extends U> lMapper,
                                              Function<? super R, ? extends V> rMapper) {
        return this.biFlatMap(l -> left(lMapper.apply(l)), r -> right(rMapper.apply(r)));
    }

    public final <U> Either<L, U> map(Function<? super R, ? extends U> mapper) {
        return this.flatMap(r -> right(mapper.apply(r)));
    }

    @Override
    public final boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || this.getClass() != obj.getClass()) return false;
        Either<?, ?> other = (Either<?, ?>) obj;

        if (isLeft() && other.isLeft()) {
            return Objects.equals(getLeft(), other.getLeft());
        } else if (isRight() && other.isRight()) {
            return Objects.equals(getRight(), other.getRight());
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return isLeft() ? Objects.hashCode(getLeft()) : Objects.hashCode(getRight());
    }

    private static final class Left<L, R> extends Either<L, R> {
        private final L value;

        Left(final L left) {
            this.value = left;
        }

        @Override
        public boolean isLeft() {
            return true;
        }

        @Override
        public L getLeft() throws NoSuchElementException {
            return value;
        }

        @Override
        public R getRight() throws NoSuchElementException {
            throw new NoSuchElementException("No right value in Left");
        }

        @Override
        public <U> Either<U, R> flatMapLeft(Function<? super L, Either<U, R>> mapper) {
            return mapper.apply(value);
        }

        @Override
        public <U, V> Either<U, V> biFlatMap(Function<? super L, Either<U, V>> lMapper, Function<? super R, Either<U, V>> rMapper) {
            return lMapper.apply(value);
        }

        @Override
        public <U> Either<L, U> flatMap(Function<? super R, Either<L, U>> mapper) {
            return new Left<>(value);
        }

        @Override
        public <U> U fold(Function<? super L, U> lMapper, Function<? super R, U> rMapper) {
            return lMapper.apply(value);
        }

        @Override
        public Optional<R> toOptional() {
            return Optional.empty();
        }

        @Override
        public String toString() {
            return "Left(" + value + ")";
        }
    }

    private static final class Right<L, R> extends Either<L, R> {
        private final R value;

        Right(final R right) {
            this.value = right;
        }

        @Override
        public boolean isLeft() {
            return false;
        }

        @Override
        public L getLeft() throws NoSuchElementException {
            throw new NoSuchElementException("No left value in Right");
        }

        @Override
        public R getRight() throws NoSuchElementException {
            return value;
        }

        @Override
        public <U> Either<U, R> flatMapLeft(Function<? super L, Either<U, R>> mapper) {
            return new Right<>(value);
        }

        @Override
        public <U, V> Either<U, V> biFlatMap(Function<? super L, Either<U, V>> lMapper, Function<? super R, Either<U, V>> rMapper) {
            return rMapper.apply(value);
        }

        @Override
        public <U> Either<L, U> flatMap(Function<? super R, Either<L, U>> mapper) {
            return mapper.apply(value);
        }

        @Override
        public <U> U fold(Function<? super L, U> lMapper, Function<? super R, U> rMapper) {
            return rMapper.apply(value);
        }

        @Override
        public Optional<R> toOptional() {
            return Optional.of(value);
        }

        @Override
        public String toString() {
            return "Right(" + value + ")";
        }
    }
}
