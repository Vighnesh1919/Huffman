import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanCoding {

    // Build the Huffman Tree
    private static HuffmanNode buildTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>(new MyComparator());

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            HuffmanNode node = new HuffmanNode();
            node.character = entry.getKey();
            node.frequency = entry.getValue();
            node.left = null;
            node.right = null;
            priorityQueue.add(node);
        }

        HuffmanNode root = null;

        while (priorityQueue.size() > 1) {
            HuffmanNode x = priorityQueue.poll();
            HuffmanNode y = priorityQueue.poll();

            HuffmanNode f = new HuffmanNode();
            f.frequency = x.frequency + y.frequency;
            f.character = '-';
            f.left = x;
            f.right = y;

            root = f;
            priorityQueue.add(f);
        }

        return root;
    }

    // Generate Huffman Codes
    private static void generateCodes(HuffmanNode root, String code, Map<Character, String> huffmanCode) {
        if (root == null) {
            return;
        }
        if (root.left == null && root.right == null) {
            huffmanCode.put(root.character, code);
            return;
        }
        generateCodes(root.left, code + "0", huffmanCode);
        generateCodes(root.right, code + "1", huffmanCode);
    }

    // Compress the input text using Huffman Coding
    public static String compress(String text, HuffmanNode root) {
        Map<Character, String> huffmanCode = new HashMap<>();
        generateCodes(root, "", huffmanCode);

        StringBuilder encodedString = new StringBuilder();
        for (char c : text.toCharArray()) {
            encodedString.append(huffmanCode.get(c));
        }

        return encodedString.toString();
    }

    // Decompress the encoded text using Huffman Tree
    public static String decompress(String encodedText, HuffmanNode root) {
        StringBuilder decodedString = new StringBuilder();
        HuffmanNode current = root;

        for (int i = 0; i < encodedText.length(); i++) {
            if (encodedText.charAt(i) == '0') {
                current = current.left;
            } else {
                current = current.right;
            }

            if (current.left == null && current.right == null) {
                decodedString.append(current.character);
                current = root;
            }
        }

        return decodedString.toString();
    }

    // Save the compressed content to a file
    public static void saveToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        }
    }

    // Load content from a file
    public static String loadFromFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }

    public static void main(String[] args) throws IOException {
        String inputFilePath = "input.txt";
        String compressedFilePath = "compressed.txt";
        String decompressedFilePath = "decompressed.txt";

        // Load the text file
        String text = loadFromFile(inputFilePath);

        // Calculate frequency map
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }

        // Build Huffman Tree
        HuffmanNode root = buildTree(frequencyMap);

        // Compress the text
        String compressedText = compress(text, root);
        saveToFile(compressedFilePath, compressedText);

        // Decompress the text
        String decompressedText = decompress(compressedText, root);
        saveToFile(decompressedFilePath, decompressedText);

        System.out.println("Original text: " + text);
        System.out.println("Compressed text: " + compressedText);
        System.out.println("Decompressed text: " + decompressedText);
    }
}
