package shitstructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ShitHeapTest {
    private final int test_count = 100;

    private List<Integer> randomIntegerList(int size) {
        Random random = new Random();
        List<Integer> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(random.nextInt());
        }
        return list;
    }

    private <T extends Comparable<T>> boolean isHeapOrdered(ShitHeap<T> heap) {
        List<T> outputList = heap.toList();
        HeapType type = heap.getType();

        for (int i = 0; i < outputList.size(); i++) {
            int leftIndex = 2 * i + 1;
            int rightIndex = 2 * i + 2;

            if (leftIndex < outputList.size()) {
                int cmp = outputList.get(i).compareTo(outputList.get(leftIndex));
                if (cmp != 0 && cmp != type.getValue()) {
                    return false;
                }
            }

            if (rightIndex < outputList.size()) {
                int cmp = outputList.get(i).compareTo(outputList.get(rightIndex));
                if (cmp != 0 && cmp != type.getValue()) {
                    return false;
                }
            }

        }
        return true;
    }

    private <T extends Comparable<T>> boolean doElementsInteresect(ShitHeap<T> heap, List<T> items) {
        List<T> outputList = heap.toList();
        for (T item : items) {
            if (!outputList.contains(item)) {
                return false;
            }
        }
        return true;
    }
    
    @Test
    void testInitialisation() {
        Random rand = new Random();

        for (int test = 0; test < test_count; test++) {
            int listLength = rand.nextInt(20) + test + 1;
            List<Integer> inputList = this.randomIntegerList(listLength);

            ShitHeap<Integer> heap = new ShitHeap<>(inputList);

            assertEquals(inputList.size(), heap.size());
            assertTrue(isHeapOrdered(heap));
            assertTrue(doElementsInteresect(heap, inputList));
        }
    }

    @Test
    void heapOrdering() {
        Random rand = new Random();

        for (int test = 0; test < test_count; test++) {
            List<Integer> testInputs = this.randomIntegerList(rand.nextInt(20) + test + 1);
            ShitHeap<Integer> heap = new ShitHeap<>();

            for (Integer testInput : testInputs) {
                heap.add(testInput);
                assertTrue(isHeapOrdered(heap));
            }
            assertTrue(doElementsInteresect(heap, testInputs));
        }
    }

    @Test
    void heapRemove() {
        Random rand = new Random();

        for (int test = 0; test < test_count; test++) {
            List<Integer> testInputs = this.randomIntegerList(rand.nextInt(20) + test + 1);
            ShitHeap<Integer> heap = new ShitHeap<>(testInputs);
            int removeCount = rand.nextInt(testInputs.size() - 1);
            for (int i = 0; i < removeCount; i++) {
                Integer toRemove = testInputs.get(rand.nextInt(testInputs.size()));
                testInputs.remove(toRemove);

                assertTrue(heap.remove(toRemove));
                assertTrue(isHeapOrdered(heap));
                assertTrue(doElementsInteresect(heap, testInputs));
            }
        }
    }
}
