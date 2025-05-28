package shitstructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ShitHashMap<K, V> implements Map<K, V> {
    private ShitHashMapNode<K, V> root;

    ShitHashMap(int capacity) {
        root = new ShitHashMapNodeMap<>(capacity);
    }

    @Override
    public int size() {
        return root.size();
    }

    @Override
    public boolean isEmpty() {
        return root.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        return root.get((K) key);
    }

    @Override
    public V put(K key, V value) {
        return root.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
    }

    @Override
    public void clear() {
        root.clear();
    }

    @Override
    public Set<K> keySet() {
        return Set.of();
    }

    @Override
    public Collection<V> values() {
        return List.of();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Set.of();
    }

    public String toString() {
        return root.toString();
    }

    public String prettyToString() {
        return root.prettyToString();
    }

    public static void main(String[] args) {
        ShitHashMap<Integer, Character> shm = new ShitHashMap<>(10);
        for (int i = 0; i < 10; i++) {
            shm.put(i, (char) (('a' + (i % 26))));
            System.out.printf("ShitHashMap: %s%n", shm);
        }
        System.out.println(shm.prettyToString());

        System.out.printf("Me when %d -> %c%n", 1, shm.get(1));
        System.out.printf("Me when %d -> %c%n", 2, shm.get(2));
        System.out.printf("Me when %d -> %c%n", 3, shm.get(3));
        System.out.printf("Me when %d -> %c%n", 4, shm.get(4));
        System.out.printf("Me when %d -> %c%n", 5, shm.get(5));
        System.out.printf("Me when %s -> %c%n", "a", shm.get("a"));
        System.out.printf("Size: %d%n", shm.size());
    }
}
