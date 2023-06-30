import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.*;

public class symbol_table {
    public static void main(String[] args) {
        Map<String, String> symbolTable = new HashMap<>();
        symbolTable.put("R0", "0");
        symbolTable.put("R1", "1");
        symbolTable.put("R2", "2");
        symbolTable.put("R3", "3");
        symbolTable.put("R4", "4");
        symbolTable.put("R5", "5");
        symbolTable.put("R6", "6");
        symbolTable.put("R7", "7");
        symbolTable.put("R8", "8");
        symbolTable.put("R9", "9");
        symbolTable.put("R10", "10");
        symbolTable.put("R11", "11");
        symbolTable.put("R12", "12");
        symbolTable.put("R13", "13");
        symbolTable.put("R14", "14");
        symbolTable.put("R15", "15");
        symbolTable.put("SP", "0");
        symbolTable.put("LCL", "1");
        symbolTable.put("ARG", "2");
        symbolTable.put("THIS", "3");
        symbolTable.put("THAT", "4");
        symbolTable.put("SCREEN", "16384");
        symbolTable.put("KBD", "24576");

        List<String> symbolList = new ArrayList<>();
        List<String> labelList = new ArrayList<>();

        for (String key : symbolTable.keySet()) {
            symbolList.add(key);
        }

        try {
            FileReader fileReader = new FileReader("/home/shrisharanyan/3_College/EOC/Assembler/files/nowhitespaceRect.asm");
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            File outputFile = new File("/home/shrisharanyan/3_College/EOC/Assembler/files/Symbol_Table.txt");
            FileWriter fileWriter = new FileWriter(outputFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String line;
            int count = -1;
            int variableValue = count + 16;
            while ((line = bufferedReader.readLine()) != null) {
                count++;

                if (line.startsWith("(")) {
                    String label = line.substring(1, line.length() - 1);
                    if (!symbolTable.containsKey(label) && !symbolList.contains(label)) {
                        labelList.add(label);
                        symbolTable.put(label, String.valueOf(count));
                        count--;
                    }
                }

                char[] charsArray = line.toCharArray();
                if (line.startsWith("@") && !Character.isDigit(charsArray[1])) {
                    String variable = line.substring(1, line.length());
                    System.out.println(variable);
                    if (!symbolTable.containsKey(variable) && !symbolList.contains(variable) && !labelList.contains(variable)) {
                        symbolTable.put(variable, String.valueOf(variableValue));
                        variableValue++;
                    }
                }

                String sub = line.substring(1, line.length());
                if (line.startsWith("(") && !labelList.contains(sub)) {
                    sub = line.substring(1, line.length() - 1);
                    symbolTable.remove(sub);
                    labelList.add(sub);
                    symbolTable.put(sub, String.valueOf(count));
                }

                String builtIn = line.substring(1, line.length() - 1);
                if (line.startsWith("@") && symbolTable.containsKey(builtIn) && symbolList.contains(builtIn) && !labelList.contains(builtIn)) {
                    continue;
                }
            }
            bufferedWriter.write("{");
            for (Map.Entry<String, String> entry : symbolTable.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                bufferedWriter.write("'"+key +"'"+ ": " + "'"+value+"'" + ',');
            }
            bufferedWriter.write("}");

            bufferedWriter.close();
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
