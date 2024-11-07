import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HammingCodeReceiver {

    // Reads the channel data from the file
    public static String readChannel() {
        StringBuilder data = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("Channel.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data.toString();
    }

    // Detects errors in the received Hamming code
    public static int detectError(String arr, int nr) {
        int n = arr.length();
        int res = 0;

        for (int i = 0; i < nr; i++) {
            int val = 0;
            for (int j = 1; j <= n; j++) {
                if ((j & (1 << i)) != 0) {
                    val = val ^ Character.getNumericValue(arr.charAt(n - j));
                }
            }
            res += val * (int) Math.pow(10, i);
        }

        return Integer.parseInt(Integer.toString(res), 2);
    }

    // Corrects the error in the Hamming code at the specified position
    public static String correctError(String arr, int pos) {
        // Ensure position is within bounds before correcting
        if (pos > 0 && pos <= arr.length()) {
            int index = arr.length() - pos;
            char correctedBit = (arr.charAt(index) == '0') ? '1' : '0';
            arr = arr.substring(0, index) + correctedBit + arr.substring(index + 1);
        } else {
            System.out.println("Error position out of bounds: " + pos);
        }
        return arr;
    }

    // Removes redundant bits from the corrected Hamming code
    public static String removeRedundantBits(String arr, int r) {
        StringBuilder res = new StringBuilder();
        int n = arr.length();
        int j = 0;

        for (int i = 1; i <= n; i++) {
            if (i != Math.pow(2, j)) {
                res.append(arr.charAt(n - i));
            } else {
                j++;
            }
        }

        return res.reverse().toString();
    }

    // Converts binary string data to text
    public static String binaryToText(String binaryData) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < binaryData.length(); i += 8) {
            String byteStr = binaryData.substring(i, Math.min(i + 8, binaryData.length()));
            int charCode = Integer.parseInt(byteStr, 2);
            text.append((char) charCode);
        }
        return text.toString();
    }

    // Calculates the number of redundant bits needed
    public static int calculateRedundantBits(int m) {
        for (int i = 0; ; i++) {
            if (Math.pow(2, i) >= m + i + 1) {
                return i;
            }
        }
    }

    // Receiver logic to detect, correct errors, and convert the received binary data to text
    public static void receiver() {
        String hammingData = readChannel();
        int nr = calculateRedundantBits(hammingData.length());
        int errorPos = detectError(hammingData, nr);

        if (errorPos == 0) {
            System.out.println("No error detected.");
        } else {
            System.out.println("Error detected at position: " + errorPos);
        }

        String correctData = correctError(hammingData, errorPos);
        String dataWithoutRedundantBits = removeRedundantBits(correctData, nr);
        String asciiText = binaryToText(dataWithoutRedundantBits);
        System.out.println("Received text: " + asciiText);
    }

    public static void main(String[] args) {
        receiver();
    }
}
