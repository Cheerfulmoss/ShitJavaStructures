package shitstructures;

public class ShitEither<L, R> {
    private final L iLeft;
    private final R iRight;

    ShitEither(L left, R right) {
        this.iLeft = left;
        this.iRight = right;
    }

    public static <L, R> ShitEither<L, R> left(L left) {
        return new ShitEither<>(left, null);
    }

    public static <L, R> ShitEither<L, R> right(R right) {
        return new ShitEither<>(null, right);
    }

    public L getLeft() {
        return iLeft;
    }

    public R getRight() {
        return iRight;
    }

    public boolean isRight() {
        return iRight != null;
    }

    public boolean isLeft() {
        return iLeft != null;
    }

    public String toString() {
        return "(L: " + iLeft + ", R: " + iRight + ")";
    }
}