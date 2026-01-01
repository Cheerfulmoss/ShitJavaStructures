package shitstructures.functional;

import java.util.function.*;

// TODO: Rewrite this entire thing so that it can be memoized
// TODO: Make not shit :3
public abstract class Trampoline<T> {

    public abstract T run();

    public abstract boolean isComplete();

    public abstract Trampoline<T> bounce();

    public <R> Trampoline<R> map(Function<? super T, ? extends R> mapper) {
        return flatMap(t -> done(mapper.apply(t)));
    }

    public <R> Trampoline<R> flatMap(Function<? super T, Trampoline<R>> mapper) {
        return new More<>(() -> bounce().flatMap(mapper));
    }

    public static <T> Trampoline<T> done(T result) {
        return new Done<>(result);
    }

    public static <T> Trampoline<T> more(Supplier<Trampoline<T>> next) {
        return new More<>(next);
    }

    private static final class Done<T> extends Trampoline<T> {
        private final T result;

        Done(T result) {
            this.result = result;
        }

        @Override
        public T run() {
            return result;
        }

        @Override
        public boolean isComplete() {
            return true;
        }

        @Override
        public <R> Trampoline<R> flatMap(Function<? super T, Trampoline<R>> mapper) {
            return mapper.apply(result);
        }

        @Override
        public <R> Trampoline<R> map(Function<? super T, ? extends R> mapper) {
            return done(mapper.apply(result));
        }

        @Override
        public Trampoline<T> bounce() {
            return this;
        }
    }

    private static final class More<T> extends Trampoline<T> {
        private final Supplier<Trampoline<T>> next;

        More(Supplier<Trampoline<T>> next) {
            this.next = next;
        }

        @Override
        public T run() {
            Trampoline<T> current = this;
            while (!current.isComplete()) {
                current = current.bounce();
            }
            return current.run();
        }

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public Trampoline<T> bounce() {
            return next.get();
        }
    }
}
