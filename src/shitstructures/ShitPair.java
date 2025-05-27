package shitstructures;

public class ShitPair<T1, T2> {
    private final T1 first;
    private final T2 second;

    ShitPair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }

    public T1 getFirst() {
        return first;
    }

    public T2 getSecond() {
        return second;
    }

    public String toString() {
        return "{2}(" + first + ", " + second + ")";
    }
}
