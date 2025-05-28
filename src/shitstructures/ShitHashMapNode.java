package shitstructures;

import java.util.Map;
import java.util.Set;

interface ShitHashMapNode<K, V> extends Map.Entry<K, V> {
    V get(K key);
    V put(K key, V value);
    V remove(K key);
    int size();
    void clear();
    Set<K> keySet();
    Set<V> values();
    Set<Map.Entry<K, V>> entrySet();
    boolean isEmpty();
    String toString();
    String prettyToString();
}
