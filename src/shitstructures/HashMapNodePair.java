package shitstructures;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HashMapNodePair<K, V> implements HashMapNode<K, V> {
    private ShitPair<K, V> pair;

    HashMapNodePair(K key, V value) {
        pair = new ShitPair<>(key, value);
    }

    @Override
    public V get(K key) {
        if (key.equals(pair.getFirst())) {
            return pair.getSecond();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (key.equals(pair.getFirst())) {
            V oldValue = pair.getSecond();
            pair = new ShitPair<>(key, value);
            return oldValue;
        }
        return null;
    }

    @Override
    public V remove(K key) {
        if (!pair.getFirst().equals(key)) {
            return null;
        }
        V oldValue = pair.getSecond();
        pair = null;
        return oldValue;
    }

    @Override
    public int size() {
        return 1;
    }

    @Override
    public void clear() {
        pair = null;
    }

    @Override
    public Set<K> keySet() {
        return Set.of(pair.getFirst());
    }

    @Override
    public Collection<V> values() {
        return List.of(pair.getSecond());
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return Set.of(this);
    }

    @Override
    public boolean isEmpty() {
        return pair == null;
    }

    @Override
    public K getKey() {
        return pair.getFirst();
    }

    @Override
    public V getValue() {
        return pair.getSecond();
    }

    @Override
    public V setValue(V value) {
        V oldValue;
        oldValue = pair.getSecond();
        pair = new ShitPair<>(pair.getFirst(), value);
        return oldValue;
    }

    public String toString() {
        return pair.toString();
    }

    public String prettyToString() {
        return pair.toString();
    }
}
