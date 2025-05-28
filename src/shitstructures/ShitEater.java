package shitstructures;

public class ShitEater<L, R> {
    private final L iLeft;
    private final R iRight;

    ShitEater(L left, R right) {
        this.iLeft = left;
        this.iRight = right;
    }

    public static <L, R> ShitEater<L, R> left(L left) {
        return new ShitEater<>(left, null);
    }

    public static <L, R> ShitEater<L, R> right(R right) {
        return new ShitEater<>(null, right);
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