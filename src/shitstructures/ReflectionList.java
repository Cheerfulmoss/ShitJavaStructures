package shitstructures;

import java.util.List;

public interface ReflectionList<T> extends List<T> {
    int lastRealIndexOf(Object o);
}
