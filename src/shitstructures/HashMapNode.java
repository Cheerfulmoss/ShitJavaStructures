package shitstructures;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

// The recursive Map node itself does not need to actually do
// anything when the Map.Entry methods are called.
interface HashMapNode<K, V> extends Map.Entry<K, V> {
    V get(K key);
    V put(K key, V value);
    V remove(K key);
    int size();
    void clear();
    Set<K> keySet();
    Collection<V> values();
    Set<Map.Entry<K, V>> entrySet();
    boolean isEmpty();
    // This is a normal toString method.
    String toString();
    // This should show the nesting.
    String prettyToString();
}
