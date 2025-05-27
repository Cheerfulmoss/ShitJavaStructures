package shitstructures;

import java.util.*;
import java.util.stream.Collectors;

public class ShitHashMapNode<K, V> {
    private int capacity;
    private ShitEither<ShitPair<K, V>, ShitHashMapNode<K, V>>[] kvs;
    private Integer salt;

    ShitHashMapNode(int capacity) {
        this.capacity = capacity;
        kvs = (ShitEither<ShitPair<K, V>, ShitHashMapNode<K, V>>[]) new ShitEither[capacity];
        salt = ((Integer)System.identityHashCode(this)).hashCode();
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isFull() {
        return Arrays.stream(kvs).noneMatch(Objects::isNull);
    }

    public boolean isEmpty() {
        return Arrays.stream(kvs).filter(Objects::isNull).count() == capacity;
    }

    public V put(K key, V value) {
        int idealLocation = saltedHash(key) % capacity;
        ShitEither<ShitPair<K, V>, ShitHashMapNode<K, V>> se = kvs[idealLocation];

        // Nothing there, so we add :3.
        if (se == null) {
            kvs[idealLocation] = ShitEither.left(new ShitPair<>(key, value));
            return null;
        }
        // If they same key, replace da value.
        if (se.isLeft() && se.getLeft().getFirst().equals(key)) {
            V old = se.getLeft().getSecond();
            kvs[idealLocation] = ShitEither.left(new ShitPair<>(key, value));
            return old;
        }
        // If they not same key, make into a node and put into that!
        if (se.isLeft()) {
            ShitHashMapNode<K, V> node = new ShitHashMapNode<>(capacity);
            node.put(se.getLeft().getFirst(), se.getLeft().getSecond());
            node.put(key, value);
            kvs[idealLocation] = ShitEither.right(node);
            return null;
        }
        // A map is there! :D
        if (se.isRight()) {
            return kvs[idealLocation].getRight().put(key, value);
        }
        return null;
    }

    public V get(K key) {
        ShitEither<ShitPair<K, V>, ShitHashMapNode<K, V>> location = kvs[saltedHash(key) % capacity];
        if (location == null) {
            return null;
        }
        if (location.isLeft()) {
            if (location.getLeft().getFirst().equals(key)) {
                return location.getLeft().getSecond();
            } else {
                return null;
            }
        }
        return location.getRight().get(key);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("|");
        for (int i = 0; i < kvs.length; i++) {
            ShitEither<ShitPair<K, V>, ShitHashMapNode<K, V>> se = kvs[i];
            if (se == null) {
                continue;
            }
            sb.append(se);
            if (i < kvs.length - 1 && kvs[i + 1] != null) {
                sb.append(", ");
            }
        }
        sb.append("|");
        return sb.toString();
    }

    public String prettyToString() {
        StringBuilder sb = new StringBuilder();
        int count = 0;
        int total = (int) Arrays.stream(kvs).filter(Objects::nonNull).count();
        sb.append("|\n");
        for (int i = 0; i < kvs.length; i++) {
            ShitEither<ShitPair<K, V>, ShitHashMapNode<K, V>> se = kvs[i];
            if (se == null) {
                continue;
            }
            count++;
            if (se.isLeft()) {
                sb.append("\t").append(se.getLeft());
            } else if (se.isRight()) {
                String formattedNode = se.getRight().prettyToString();
                sb.append(insertLeadingTab(formattedNode));
            }
            if (count < total) {
                sb.append(",\n");
            } else {
                sb.append("\n");
            }
        }
        sb.append("|");
        return sb.toString();
    }

    public int size() {
        int total = 0;
        for (ShitEither<ShitPair<K, V>, ShitHashMapNode<K, V>> se : kvs) {
            if (se == null) {
                continue;
            }
            if (se.isLeft()) {
                total += 1;
            } else if (se.isRight()) {
                total += se.getRight().size();
            }
        }
        return total;
    }

    public boolean containsValue(V value) {
        return Arrays.stream(kvs)
                .filter( e -> e != null && e.isLeft() )
                .map(ShitEither::getLeft)
                .anyMatch( e -> e.getSecond().equals(value) );
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
