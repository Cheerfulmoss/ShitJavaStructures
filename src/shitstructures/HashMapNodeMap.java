package shitstructures;

import java.util.*;
import java.util.stream.Collectors;

public class HashMapNodeMap<K, V> implements HashMapNode<K, V> {
    private HashMapNode<K, V>[] kvs;
    private final int capacity;
    private final int salt;

    HashMapNodeMap(int capacity) {
        this.capacity = capacity;
        kvs = new HashMapNode[capacity];
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
                kvs[idealLocation] = new HashMapNodePair<>(key, value);
                return null;
            }
            case HashMapNodeMap<K, V> nodeMap -> {
                return nodeMap.put(key, value);
            }
            case HashMapNodePair<K, V> nodePair -> {
                V result = nodePair.put(key, value);
                if (result == null) {
                    kvs[idealLocation] = new HashMapNodeMap<>(capacity);
                    kvs[idealLocation].put(nodePair.getKey(), nodePair.getValue());
                    return kvs[idealLocation].put(key, value);
                } else {
                    return result;
                }
            }
            default -> {return null;}
        }
    }

    @Override
    public V remove(K key) {
        int idealLocation = saltedHash(key) % capacity;

        switch (kvs[idealLocation]) {
            case HashMapNodePair<K, V> nodePair -> {
                V value = nodePair.remove(key);
                if (value != null) {
                    kvs[idealLocation] = null;
                }
                return value;
            }
            case HashMapNodeMap<K, V> nodeMap -> {
                V result = nodeMap.remove(key);
                if (nodeMap.isEmpty()) {
                    kvs[idealLocation] = null;
                }
                return result;
            }
            default -> {return null;}
        }
    }

    @Override
    public int size() {
        return Arrays.stream(kvs)
                .filter(Objects::nonNull)
                .mapToInt(HashMapNode::size)
                .sum();
    }

    @Override
    public void clear() {
        kvs = new HashMapNode[capacity];
    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (HashMapNode<K, V> node : kvs) {
            if (node == null) {
                continue;
            }
            keys.addAll(node.keySet());
        }
        return keys;
    }

    @Override
    public Collection<V>values() {
        List<V> values = new ArrayList<>();
        for (HashMapNode<K, V> node : kvs) {
            if (node == null) {
                continue;
            }
            values.addAll(node.values());
        }
        return values;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        Set<Map.Entry<K, V>> entries = new HashSet<>();
        for (HashMapNode<K, V> node : kvs) {
            if (node == null) {
                continue;
            }
            entries.addAll(node.entrySet());
        }
        return entries;
    }

    @Override
    public boolean isEmpty() {
        return Arrays.stream(kvs).allMatch(Objects::isNull);
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
        for (HashMapNode<K, V> se : kvs) {
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
        for (HashMapNode<K, V> se : kvs) {
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
