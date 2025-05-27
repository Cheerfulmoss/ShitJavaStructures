package shitstructures;

public class ShitTriple<T1, T2, T3> {
    private final T1 first;
    private final T2 second;
    private final T3 third;

    ShitTriple(T1 first, T2 second, T3 third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    T1 getFirst() {
        return first;
    }

    T2 getSecond() {
        return second;
    }

    T3 getThird() {
        return third;
    }

    public String toString() {
        return "{3}(" + first + ", " + second + ", " + third + ")";
    }
}
