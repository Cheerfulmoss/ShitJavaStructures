package shitstructures;

import java.util.*;

public class ShitList<T> extends ArrayList<T> {

}

//public class ShitList<T> implements List<T> {
//    private T[] data;
//    private int size;
//
//    ShitList() {}
//
//    ShitList(Collection<T> input) {
//        this.data = (T[]) new Object[this.nextPower(input.size())];
//        int i = 0;
//        for (T t : input) {
//            this.data[i++] = t;
//        }
//        this.size = i;
//    }
//
//    @Override
//    public int size() {
//        return this.size;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return this.size == 0;
//    }
//
//    @Override
//    public boolean contains(Object o) {
//        for (int i = 0; i < this.size; i++) {
//            if (this.data[i].equals(o)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public Iterator iterator() {
//        return null;
//    }
//
//    @Override
//    public Object[] toArray() {
//        return this.data.clone();
//    }
//
//    @Override
//    public boolean add(Object o) {
//        return false;
//    }
//
//    @Override
//    public boolean remove(Object o) {
//        return false;
//    }
//
//    @Override
//    public boolean addAll(Collection c) {
//        return false;
//    }
//
//    @Override
//    public boolean addAll(int index, Collection c) {
//        return false;
//    }
//
//    @Override
//    public void clear() {
//
//    }
//
//    @Override
//    public T get(int index) {
//        return null;
//    }
//
//    @Override
//    public Object set(int index, Object element) {
//        return null;
//    }
//
//    @Override
//    public void add(int index, Object element) {
//
//    }
//
//    @Override
//    public Object remove(int index) {
//        return null;
//    }
//
//    @Override
//    public int indexOf(Object o) {
//        return 0;
//    }
//
//    @Override
//    public int lastIndexOf(Object o) {
//        return 0;
//    }
//
//    @Override
//    public ListIterator listIterator() {
//        return null;
//    }
//
//    @Override
//    public ListIterator listIterator(int index) {
//        return null;
//    }
//
//    @Override
//    public List subList(int fromIndex, int toIndex) {
//        return List.of();
//    }
//
//    @Override
//    public boolean retainAll(Collection c) {
//        return false;
//    }
//
//    @Override
//    public boolean removeAll(Collection c) {
//        return false;
//    }
//
//    @Override
//    public boolean containsAll(Collection c) {
//        return false;
//    }
//
//    @Override
//    public Object[] toArray(Object[] a) {
//        return new Object[0];
//    }
//
//    private int nextPower(int number) {
//        return (number << 1) & ~number;
//    }
//}
