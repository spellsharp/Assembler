import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Symbols {
    public static void main(String[] args) {
        String filePath = "nowhitespaceRect.asm";
        String outputFilePath = "Symbols.txt";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("(")) {
                    writer.write(line);
                    writer.newLine();
                }
                if (line.startsWith("@")) {
                    if (Character.isDigit(line.charAt(1))) {
                        continue;
                    } else {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }

            reader.close();
            writer.close();
            System.out.println("Conversion successful!");
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}
