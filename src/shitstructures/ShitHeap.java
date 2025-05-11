package shitstructures;

import java.util.*;

public class ShitHeap<T extends Comparable<T>> implements Queue<T> {
    private final ArrayList<T> heap;
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

    @Override
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

    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public boolean isEmpty() {
        return this.heap.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.heap.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return this.heap.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.heap.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return this.heap.toArray(a);
    }

    public List<T> toList() {
        return new ArrayList<>(this.heap);
    }

    public HeapType getType() {
        return type;
    }

    @Override
    public boolean add(T element) {
        boolean changed = this.heap.add(element);
        if (changed) {
            this.upHeap(this.size() - 1);
        }
        return changed;
    }

    @Override
    public boolean remove(Object element) {
        int index = this.heap.indexOf(element);
        if (index == -1) {
            return false;
        }
        this.swap(index, this.size() - 1);
        this.heap.remove(this.size() - 1);

        if (index < this.size()) {
            this.downHeap(index);
            this.upHeap(index);
        }
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.heap.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean changed = this.heap.addAll(c);
        if (changed) {
            this.heapify();
        }
        return changed;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean changed = this.heap.removeAll(c);
        if (changed) {
            this.heapify();
        }
        return changed;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean changed = this.heap.retainAll(c);
        if (changed) {
            this.heapify();
        }
        return changed;
    }

    @Override
    public void clear() {
        this.heap.clear();
    }

    @Override
    public boolean offer(T element) {
        return this.add(element);
    }

    @Override
    public T remove() {
        T head = this.poll();
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head;
    }

    @Override
    public T poll() {
        if (this.isEmpty()) {
            return null;
        }
        this.swap(0, this.size() - 1);
        T element = this.heap.remove(this.size() - 1);
        this.downHeap(0);
        return element;
    }

    @Override
    public T element() {
        T head = this.peek();
        if (head == null) {
            throw new NoSuchElementException();
        }
        return head;
    }

    @Override
    public T peek() {
        if (this.isEmpty()) {
            return null;
        }
        return this.heap.getFirst();
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
}
