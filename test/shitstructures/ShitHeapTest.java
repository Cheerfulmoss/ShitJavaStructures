package shitstructures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class ShitHeapTest {
    private final int TEST_COUNT = 100;

    private boolean is_heap_ordered(ShitHeap heap) {
        List<Integer> outputList = heap.toList();
        HeapType type = heap.getType();
        for (int i = 0; i < outputList.size(); i++) {
            for (int j = i + 1; j < i; j++) {
                int comparison = outputList.get(i).compareTo(outputList.get(j));
                if (comparison != 0 && comparison != type.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }
    
    @Test
    void testInitialisation() {
        Random rand = new Random();

        for (int test = 0; test < TEST_COUNT; test++) {
            int listLength = rand.nextInt(20) + test + 1;

            List<Integer> inputList = new ArrayList<>();
            for (int i = 0; i < listLength; i++) {
                inputList.add(rand.nextInt(100 + test));
            }

            ShitHeap<Integer> heap = new ShitHeap<>(inputList);
            assertEquals(inputList.size(), heap.size());
            assertTrue(is_heap_ordered(heap));
        }
    }

    @Test
    void heapOrdering() {
        Random rand = new Random();

        for (int test = 0; test < TEST_COUNT; test++) {
            ShitHeap<Integer> heap = new ShitHeap<>();



        }
    }
}
