package shitstructures;

public class StringMachine {
    public static int countOccurrences(String str, String pattern) {
        if (str == null || pattern == null) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(pattern, index)) != -1) {
            count++;
            index += pattern.length();
        }
        return count;
    }
}
