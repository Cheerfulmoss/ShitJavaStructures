package shitstructures;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

public class AdaptivePQTest {
    private final String[] words = {
            "apple", "banana", "cherry", "date", "elderberry", "fig", "grape", "honey",
            "ice", "juice", "kiwi", "lemon", "mango", "nectarine", "orange", "papaya",
            "quince", "raspberry", "strawberry", "tomato", "ugli", "vanilla",
            "watermelon", "xylophone", "yam", "zucchini",

            "amber", "breeze", "cloud", "dawn", "echo", "flame", "glow", "harbor",
            "island", "jungle", "kernel", "lantern", "meadow", "night", "ocean",
            "pebble", "quartz", "river", "shadow", "thunder", "umbrella", "valley",
            "whisper", "xenon", "youth", "zenith",

            "anchor", "bridge", "canyon", "desert", "engine", "forest", "galaxy",
            "hill", "iron", "jewel", "knight", "ladder", "mirror", "nebula", "orbit",
            "planet", "quest", "road", "stone", "tower", "unity", "voyage", "wind",

            "axis", "balance", "circle", "depth", "energy", "focus", "gravity",
            "horizon", "idea", "journey", "knowledge", "logic", "motion", "nature",
            "order", "pattern", "quality", "reason", "system", "theory", "value",
            "wisdom", "year", "zone", "alpha"
    };

    @Test
    public void testInsertAndPrint() {
        AdaptivePQ<String> apq = new AdaptivePQ<>();
        for (String word : words) {
            apq.insert(word, word.length());
        }
        System.out.println(apq);
        List<Integer> lengths = new ArrayList<>();
        Arrays.stream(words).forEach(
                wrd -> lengths.add(wrd.length())
        );
        Collections.sort(lengths);

        String val = apq.removeMin();
        int i = 0;
        while (val != null) {
            assertEquals(lengths.get(i), val.length());
            val = apq.removeMin();
            i++;
        }
        assertEquals(i, lengths.size());
        assertNull(apq.removeMin());
        assertEquals(0, apq.size());
    }

    @Test
    public void testUpdatePriority() {
        AdaptivePQ<String> apq = new AdaptivePQ<>();
        for (String word : words) {
            apq.insert(word, word.length());
        }

        List<String> words =  new ArrayList<>(List.of(this.words));
        Collections.shuffle(words);
        for (String word : words) {
            apq.updatePriority(word, -1);
            assertEquals(word, apq.removeMin());
        }
    }
}
