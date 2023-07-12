import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class A_Instruction {
    private static HashMap<String, String> symbolTable;
    private static StringBuilder cInstructions;

    public static void main(String[] args) {
        symbolTable = new HashMap<>();
        cInstructions = new StringBuilder();

        loadSymbolTable("/home/shrisharanyan/3_College/EOC/Assembler/files/Symbol_Table.txt");
        processInstructions("/home/shrisharanyan/3_College/EOC/Assembler/files/nowhitespaceRect.asm");
    }

    private static void loadSymbolTable(String symbolTablePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(symbolTablePath))) {
            String line;
            StringBuilder content = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            // symbolTable = content;
            symbolTable = parseDictionary(content.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static HashMap<String, String> parseDictionary(String dictionaryString) {
        dictionaryString = dictionaryString.replace("{", "").replace("}", "").replace("'", "");
        HashMap<String, String> dictionary = new HashMap<>();
        String[] entries = dictionaryString.split(",");
        for (String entry : entries) {
            String[] keyValue = entry.trim().split(":");
            String key = keyValue[0].trim();
            String value = keyValue[1].trim();
            dictionary.put(key, value);
        }

        return dictionary;
    }

    private static boolean isAInstruction(String string) {
        return string.startsWith("@") || string.startsWith("(");
    }

    private static boolean isCInstruction(String string) {
        return !isAInstruction(string) && !Character.isDigit(string.charAt(1));
    }

    private static String cleanLine(String line) {
        return line.replace("\n", "").replace("@", "").replace("(", "").replace(")", "");
    }

    private static void processInstructions(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                AInstruction(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void AInstruction(String line) {
        if (isAInstruction(line) && !line.startsWith("(")) {
            line = cleanLine(line);
            String binaryLine;
            if (symbolTable.containsKey(line)) {
                String stringLine = symbolTable.get(line);
                binaryLine = String.format("%16s", Integer.toBinaryString(Integer.parseInt(stringLine))).replace(' ', '0');
            } else {
                binaryLine = String.format("%16s", Integer.toBinaryString(Integer.parseInt(line))).replace(' ', '0');
            }
            System.out.println(binaryLine);
        } else if (isCInstruction(line)) {
            cInstructions.append(line.replace("\n", ""));
        }
    }
}