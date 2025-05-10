package shitstructures;

public enum HeapType {
    MIN(-1),
    MAX(1);

    private final int value;
    HeapType(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
}
