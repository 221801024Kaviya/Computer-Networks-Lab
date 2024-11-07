import java.io.FileWriter;
import java.io.IOException;

public class HammingCodeSender {
    
    // Converts text to a binary string
    public static String textToBinary(String text) {
        StringBuilder binary = new StringBuilder();
        for (char c : text.toCharArray()) {
            // Convert each character to binary using Integer.toBinaryString
            // Ensure each byte is represented with 8 bits by padding with leading zeros
            binary.append(String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'));
        }
        return binary.toString();
    }

    public static int calculateRedundantBits(int m) {
        for (int i = 0; ; i++) {
            if (Math.pow(2, i) >= m + i + 1) {
                return i;
            }
        }
    }

    public static String positionRedundantBits(String data, int r) {
        int j = 0, k = 0;
        int m = data.length();
        StringBuilder res = new StringBuilder();

        for (int i = 1; i <= m + r; i++) {
            if (i == Math.pow(2, j)) {
                res.append('0');
                j++;
            } else {
                res.append(data.charAt(k));
                k++;
            }
        }
        return res.toString();
    }

    public static String calculateParityBits(String arr, int r) {
        int n = arr.length();
        char[] arrChar = arr.toCharArray();

        for (int i = 0; i < r; i++) {
            int val = 0;
            for (int j = 1; j <= n; j++) {
                if ((j & (1 << i)) != 0) {
                    val ^= Character.getNumericValue(arrChar[n - j]);
                }
            }
            arrChar[n - (1 << i)] = (char) (val + '0');
        }
        return new String(arrChar);
    }

    public static String hammingCode(String data) {
        int m = data.length();
        int r = calculateRedundantBits(m);
        String arr = positionRedundantBits(data, r);
        arr = calculateParityBits(arr, r);
        return arr;
    }

    public static void sender(String text) {
        String binaryData = textToBinary(text);
        String hammingData = hammingCode(binaryData);
        try (FileWriter fw = new FileWriter("Channel.txt")) {
            fw.write(hammingData);
            System.out.println("Data sent: " + hammingData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String text = "Message Received";
        sender(text);
    }
}
