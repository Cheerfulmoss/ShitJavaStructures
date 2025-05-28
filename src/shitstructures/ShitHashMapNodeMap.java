package shitstructures;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ShitHashMapNodeMap<K, V> implements ShitHashMapNode<K, V> {
    private ShitHashMapNode<K, V>[] kvs;
    private int capacity;
    private int size;
    private int salt;

    ShitHashMapNodeMap(int capacity) {
        this.capacity = capacity;
        kvs = (ShitHashMapNode<K, V>[]) new ShitHashMapNode[capacity];
        salt = ((Integer)System.identityHashCode(this)).hashCode();
    }

    @Override
    public V get(K key) {
        int idealLocation = saltedHash(key) % capacity;
        if (kvs[idealLocation] == null) {
            return null;
        }
        return kvs[idealLocation].get(key);
    }

    @Override
    public V put(K key, V value) {
        int idealLocation = saltedHash(key) % capacity;

        switch (kvs[idealLocation]) {
            case null -> {
                kvs[idealLocation] = new ShitHashMapNodePair<>(key, value);
                return null;
            }
            case ShitHashMapNodeMap<K, V> nodeMap -> {
                return nodeMap.put(key, value);
            }
            case ShitHashMapNodePair<K, V> nodePair -> {
                V result = nodePair.put(key, value);
                if (result == null) {
                    kvs[idealLocation] = new ShitHashMapNodeMap<>(capacity);
                    kvs[idealLocation].put(nodePair.getKey(), nodePair.getValue());
                    return kvs[idealLocation].put(key, value);
                } else {
                    return result;
                }
            }
            default -> {
            }
        }
        return null;
    }

    @Override
    public V remove(K key) {
        return null;
    }

    @Override
    public int size() {
        return Arrays.stream(kvs)
                .filter(Objects::nonNull)
                .mapToInt(ShitHashMapNode::size)
                .sum();
    }

    @Override
    public void clear() {
        kvs = (ShitHashMapNode<K, V>[]) new ShitHashMapNode[capacity];
    }

    @Override
    public Set<K> keySet() {
        return Set.of();
    }

    @Override
    public Set<V> values() {
        return Set.of();
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return Set.of();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public K getKey() {
        return null;
    }

    @Override
    public V getValue() {
        return null;
    }

    @Override
    public V setValue(V value) {
        return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("|");
        for (int i = 0; i < kvs.length; i++) {
            ShitHashMapNode<K, V> se = kvs[i];
            if (se == null) {
                continue;
            }
            sb.append(se);
            sb.append(", ");
        }
        sb.delete(sb.lastIndexOf(", "), sb.lastIndexOf(", ") + ", ".length());
        sb.append("|");
        return sb.toString();
    }

    public String prettyToString() {
        StringBuilder sb = new StringBuilder();
        sb.append("|\n");
        for (int i = 0; i < kvs.length; i++) {
            ShitHashMapNode<K, V> se = kvs[i];
            if (se == null) {
                continue;
            }
            sb.append(insertLeadingTab(se.prettyToString()));
            sb.append(",\n");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("|");
        return sb.toString();
    }

    private int saltedHash(K key) {
        int hashed = saltedMix(key.hashCode(), salt);
        if (hashed < 0) {
            hashed = -hashed;
        }
        return hashed;
    }

    private static int saltedMix(int hash, int salt) {
        int mixed = hash ^ salt;
        mixed ^= (mixed >>> 16);
        mixed *= 0x85ebca6b;
        mixed ^= (mixed >>> 13);
        mixed *= 0xc2b2ae35;
        mixed ^= (mixed >>> 16);
        return mixed;
    }

    private String insertLeadingTab(String str) {
        return str.lines().map( e -> "\t" + e ).collect(Collectors.joining("\n"));
    }
}
