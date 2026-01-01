package shitstructures;

import java.util.*;

// Implemented as a min heap, smallest priorities are first
public class AdaptivePQ<T> {

    private Entry<T>[] heap;
    private int size = 0;
    private final Map<T, Entry<T>> map = new HashMap<>();

    @SuppressWarnings("unchecked")
    public AdaptivePQ() {
        heap = (Entry<T>[]) new Entry[4];
    }

    public int size() {
        return size;
    }

    public boolean insert(T value, int priority) {
        if (map.containsKey(value)) {
            return false;
        }
        Entry<T> entry = new Entry<>(value, priority);
        map.put(value, entry);
        return insertHeap(entry);
    }

    public T removeMin() {
        if (size == 0) {
            return null;
        }

        swap(0, size - 1);
        Entry<T> min = heap[size - 1];
        heap[size - 1] = null;
        size--;
        map.remove(min.value);

        downHeap(0);
        return min.value;
    }

    public T min() {
        if (size == 0) {
            return null;
        }
        return heap[0].value;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean updatePriority(T value, int priority) {
        if (!map.containsKey(value)) {
            return false;
        }
        Entry<T> entry = map.get(value);
        if (entry == null) {
            return false;
        }
        int oldPrio = entry.prio;
        entry.prio = priority;
        if (oldPrio > priority) {
            upHeap(entry.index);
        } else {
            downHeap(entry.index);
        }
        return true;
    }

    public Set<T> values() {
        return map.keySet();
    }

    public Optional<Integer> getPriority(T value) {
        return Optional.ofNullable(map.get(value))
                .map(x -> x.prio);
    }

    @SuppressWarnings("unchecked")
    public void clear() {
        size = 0;
        heap = (Entry<T>[]) new Entry[4];
        map.clear();
    }

    @SuppressWarnings("unchecked")
    private boolean insertHeap(Entry<T> entry) {
        if (heap.length == size) {
            Entry<T>[] oldHeap = heap;
            heap = (Entry<T>[]) new Entry[oldHeap.length * 2 + 1];
            System.arraycopy(oldHeap, 0, heap, 0, oldHeap.length);
        }
        heap[size++] = entry;
        heap[size - 1].index = size - 1;
        upHeap(size - 1);
        return true;
    }

    private void upHeap(int index) {
        if (heap[index] == null) {
            return;  // Nothing to do, duh
        }

        while (index > 0) {
            int parent = (index - 1) / 2;
            if (heap[index].compareTo(heap[parent]) < 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    private void downHeap(int index) {
        while (true) {
            int lChild = 2 * index + 1;
            int rChild = 2 * index + 2;
            int smallest = index;

            if (inHeap(lChild) && heap[lChild].compareTo(heap[smallest]) < 0) {
                smallest = lChild;
            }
            if (inHeap(rChild) && heap[rChild].compareTo(heap[smallest]) < 0) {
                smallest = rChild;
            }
            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    private boolean inHeap(int index) {
        return index < size && heap[index] != null;
    }

    private void swap(int x, int y) {
        Entry<T> temp = heap[x];
        heap[x] = heap[y];
        heap[y] = temp;

        // Maintain bookkeeping
        heap[y].index = y;
        heap[x].index = x;
    }

    @Override
    public String toString() {
        return Arrays.toString(heap);
    }

    private static class Entry<K> implements Comparable<Entry<K>> {
        final K value;
        int prio;
        int index;

        Entry(K value, int prio) {
            this.value = value;
            this.prio = prio;
        }

        // -1 => this < other
        //  0 => this = other
        //  1 => this > other
        @Override
        public int compareTo(Entry<K> o) {
            return Integer.compare(this.prio, o.prio);
        }

        @Override
        public String toString() {
            return "Entry(" +  value + ", " + prio + ")";
        }
    }
}
