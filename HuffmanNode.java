import java.util.Comparator;

public class HuffmanNode {
    int frequency;
    char character;

    HuffmanNode left;
    HuffmanNode right;
}

// Comparator for the priority queue
class MyComparator implements Comparator<HuffmanNode> {
    public int compare(HuffmanNode x, HuffmanNode y) {
        return x.frequency - y.frequency;
    }
}
