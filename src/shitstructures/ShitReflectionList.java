package shitstructures;

import java.util.*;

public class ShitReflectionList<T> implements ReflectionList<T> {
    private List<T> elems;

    ShitReflectionList() {
        elems = new ShitList<>();
    }

    @Override
    public int size() {
        return elems.size() * 2;
    }

    @Override
    public boolean isEmpty() {
        return elems.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return elems.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return materialiseTheImmaterial().iterator();
    }

    @Override
    public Object[] toArray() {
        return materialiseTheImmaterial().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return materialiseTheImmaterial().toArray(a);
    }

    @Override
    public boolean add(T t) {
        return elems.add(t);
    }

    @Override
    public boolean remove(Object o) {
        return elems.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return elems.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        return elems.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        return elems.addAll(reflectionToReal(index), c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return elems.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return elems.retainAll(c);
    }

    @Override
    public void clear() {
        elems.clear();
    }

    @Override
    public T get(int index) {
        if (index >= elems.size() * 2) {
            throw new IndexOutOfBoundsException();
        }
        return elems.get(reflectionToReal(index));
    }

    @Override
    public T set(int index, T element) {
        if (index >= elems.size() * 2) {
            throw new IndexOutOfBoundsException();
        }
        return elems.set(reflectionToReal(index), element);
    }

    @Override
    public void add(int index, T element) {
        if (index >= elems.size() * 2) {
            throw new IndexOutOfBoundsException();
        }
        elems.add(reflectionToReal(index), element);
    }

    @Override
    public T remove(int index) {
        if (index >= elems.size() * 2) {
            throw new IndexOutOfBoundsException();
        }
        index = reflectionToReal(index);
        return elems.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return elems.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return reflectionToImaginary(elems.indexOf(o));
    }

    public int lastRealIndexOf(Object o) {
        return elems.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        return materialiseTheImmaterial().listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        return materialiseTheImmaterial().listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        ShitReflectionList<T> badImplications = new ShitReflectionList<>();
        badImplications.addAll(materialiseTheImmaterial().subList(fromIndex, toIndex));
        return badImplications;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(elems).append(" | ").append(elems.reversed());
        sb.deleteCharAt(sb.indexOf("]"));
        sb.deleteCharAt(sb.lastIndexOf("["));
        return sb.toString();
    }

    private List<T> materialiseTheImmaterial() {
        List<T> reflectedList = new ShitList<>();
        reflectedList.addAll(elems);
        reflectedList.addAll(elems.reversed());
        return reflectedList;
    }

    private int reflectionToReal(int index) {
        if (index >= elems.size()) {
            index = (elems.size() - 1) - (index % elems.size());
        }
        return index;
    }

    private int reflectionToImaginary(int index) {
        if (index < elems.size()) {
            index = (size() - 1) - index;
        }
        return index;
    }

    public static void main(String[] args) {
        ShitReflectionList<Integer> rl = new ShitReflectionList<>();
        rl.add(2);
        rl.add(1);
        rl.add(2);
        System.out.println(rl);
        System.out.println(rl.lastIndexOf(2));
    }
}
