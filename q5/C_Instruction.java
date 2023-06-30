import java.util.HashMap;
import java.util.Map;
import java.io.*;
public class C_Instruction {
    private static Map<String, Map<String, String>> compTable;
    private static Map<String, String> destTable;
    private static Map<String, String> jumpTable;
    private static StringBuilder cInstructions;

    public static void main(String[] args) {
        compTable = new HashMap<>();
        destTable = new HashMap<>();
        jumpTable = new HashMap<>();
        cInstructions = new StringBuilder();

        loadCompTable();
        loadDestTable();
        loadJumpTable();
        processInstructions("/home/shrisharanyan/3_College/EOC/Assembler/files/nowhitespaceRect.asm");
    }

    private static void loadCompTable() {
        compTable.put("0", createCompMap1("101010", "111111", "111010", "001100", "110000", "001101", "110001",
                "001111", "110011", "011111", "110111", "001110", "110010", "000010", "010011", "000111", "000000", "010101"));
        compTable.put("1", createCompMap2("110000", "110001", "110011", "110111", "110010", "000010", "010011",
                "000111", "000000", "010101"));
    }

    private static Map<String, String> createCompMap1(String... values) {
        Map<String, String> compMap = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            compMap.put(getCompKey1(i), values[i]);
        }
        return compMap;
    }

    private static Map<String, String> createCompMap2(String... values) {
        Map<String, String> compMap = new HashMap<>();
        for (int i = 0; i < values.length; i++) {
            compMap.put(getCompKey2(i), values[i]);
        }
        return compMap;
    }



    private static String getCompKey1(int index) {
        String[] keys = {"0", "1", "-1", "D", "A", "!D", "!A", "-D", "-A", "D+1", "A+1", "D-1", "A-1", "D+A", "D-A", "A-D", "D&A", "D|A"};
        if (index >= 0 && index < keys.length) {
            return keys[index];
        }
        return null;
    }

    private static String getCompKey2(int index) {
        String[] keys = { "M", "!M", "-M", "M+1", "M-1", "D+M", "D-M", "M-D", "D&M", "D|M"};
        if (index >= 0 && index < keys.length) {
            return keys[index];
        }
        return null;
    }



    private static void loadDestTable() {
        destTable.put(null, "000");
        destTable.put("M", "001");
        destTable.put("D", "010");
        destTable.put("MD", "011");
        destTable.put("A", "100");
        destTable.put("AM", "101");
        destTable.put("AD", "110");
        destTable.put("AMD", "111");
    }

    private static void loadJumpTable() {
        jumpTable.put(null, "000");
        jumpTable.put("JGT", "001");
        jumpTable.put("JEQ", "010");
        jumpTable.put("JGE", "011");
        jumpTable.put("JLT", "100");
        jumpTable.put("JNE", "101");
        jumpTable.put("JLE", "110");
        jumpTable.put("JMP", "111");
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
                if (isCInstruction(line)) {
                    line = cleanLine(line);
                    cInstructions.append(line+'\n');
                    
                }

            }
            // System.out.println(cInstructions);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Process C-instructions
        String[] instructions = cInstructions.toString().split("\n");
        for (String instruct : instructions) {;
            processCInstruction(instruct);
        }
    }

    private static void processCInstruction(String instruct) {
        String dest = null;
        String comp;
        String jump = null;

        if (instruct.contains("=") && !instruct.contains(";")) {
            // System.out.println(instruct);
            // System.out.println("----------------");
            int destIndex = instruct.indexOf("=");
            dest = instruct.substring(0, destIndex);
            comp = instruct.substring(destIndex + 1);
        } else if (instruct.contains(";") && !instruct.contains("=")) {
            // System.out.println(instruct);
            int jumpIndex = instruct.indexOf(";");
            // System.out.println("----------------");
            comp = instruct.substring(0, jumpIndex);
            jump = instruct.substring(jumpIndex + 1);
        } else if (instruct.contains("=") && instruct.contains(";")) {
            // System.out.println(instruct);
            // System.out.println("----------------");
            int destIndex = instruct.indexOf("=");
            int jumpIndex = instruct.indexOf(";");
            dest = instruct.substring(0, destIndex);
            comp = instruct.substring(destIndex + 1, jumpIndex);
            jump = instruct.substring(jumpIndex + 1);
        } else {
            return;
        }

        String binaryComp;
        String binaryDest;
        String binaryJump;

        if (compTable.get("0").containsKey(comp)) {
            binaryComp = compTable.get("0").get(comp);
            binaryDest = destTable.get(dest);
            binaryJump = jumpTable.get(jump);
            String binaryInstruction = "1110" + binaryComp + binaryDest + binaryJump;
            System.out.println(binaryInstruction);
        } else if (compTable.get("1").containsKey(comp)) {
            binaryComp = compTable.get("1").get(comp);
            binaryDest = destTable.get(dest);
            binaryJump = jumpTable.get(jump);
            String binaryInstruction = "1111" + binaryComp + binaryDest + binaryJump;
            System.out.println(binaryInstruction);
        } else {
            System.out.println("Invalid comp value: " + comp);
        }
    }
}
