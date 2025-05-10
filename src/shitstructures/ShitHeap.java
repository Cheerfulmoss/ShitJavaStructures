package shitstructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class ShitHeap<T extends Comparable<T>> {
    private ArrayList<T> heap;
    private HeapType type = HeapType.MIN;

    ShitHeap() {
        heap = new ArrayList<>();
    }

    ShitHeap(HeapType type) {
        this.type = type;
        heap = new ArrayList<>();
    }

    ShitHeap(List<T> input) {
        heap = new ArrayList<>(input);
        this.heapify();
    }

    ShitHeap(List<T> input, HeapType type) {
        this.type = type;
        heap = new ArrayList<>(input);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('\\');
        for (int i = 0; i < heap.size(); i++) {
            sb.append(heap.get(i));
            if (i != heap.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append('/');
        return sb.toString();
    }

    public int size() {
        return heap.size();
    }

    public void push(T element) {
        this.heap.add(element);
        this.upHeap(this.size() - 1);
    }

    public List<T> toList() {
        return Collections.unmodifiableList(heap);
    }

    public HeapType getType() {
        return type;
    }

    private void heapify() {
        int startIndex = this.parentIndex(this.size() - 1);
        for (int i = startIndex; i >= 0; i--) {
            this.downHeap(i);
        }
    }

    private void swap(int i, int j) {
        T temp = this.heap.get(i);
        this.heap.set(i, this.heap.get(j));
        this.heap.set(j, temp);
    }

    private int parentIndex(int i) {
        return (i - 1) / 2;
    }

    private int leftChildIndex(int i) {
        return 2 * i + 1;
    }

    private int rightChildIndex(int i) {
        return 2 * i + 2;
    }
    private void upHeap(int index) {
        int parentIndex = this.parentIndex(index);
        if (parentIndex == this.type.getValue()) {
            return;
        }
        if (this.heap.get(index).compareTo(this.heap.get(parentIndex)) == this.type.getValue()) {
            this.swap(index, parentIndex);
            this.upHeap(parentIndex);
        }
    }

    private void downHeap(int index) {
        int leftIndex = this.leftChildIndex(index);
        int rightIndex = this.rightChildIndex(index);

        int minIndex;
        if (rightIndex >= this.size() && leftIndex < this.size()) {
            minIndex = leftIndex;
        } else if (leftIndex >= this.size()) {
            return;  // Node does not have a child so stop down heap.
        } else if (this.heap.get(leftIndex).compareTo(this.heap.get(rightIndex)) == this.type.getValue()) {
            minIndex = leftIndex;
        } else {
            minIndex = rightIndex;
        }

        if (this.heap.get(minIndex).compareTo(this.heap.get(index)) == this.type.getValue()) {
            this.swap(index, minIndex);
            this.downHeap(minIndex);
        }
    }

    public static void main(String[] args) {
        ShitHeap<Integer> heap = new ShitHeap<>(List.of(5, 3, 8, 1, 10));
        System.out.println(heap);
        heap.push(0);
        System.out.println(heap);
    }
}
