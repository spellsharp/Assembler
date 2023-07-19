import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.HashMap;

public class Assembler {
    private static final HashMap<String, String> symbolTable = new HashMap<>();
    private static final HashMap<String, HashMap<String, String>> compTable = new HashMap<>();
    private static final HashMap<String, String> destTable = new HashMap<>();
    private static final HashMap<String, String> jumpTable = new HashMap<>();

    // Make the Symbol table
    static {
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

        // Make the comp table
        HashMap<String, String> compTable0 = new HashMap<>();
        compTable0.put("0", "101010");
        compTable0.put("1", "111111");
        compTable0.put("-1", "111010");
        compTable0.put("D", "001100");
        compTable0.put("A", "110000");
        compTable0.put("!D", "001101");
        compTable0.put("!A", "110001");
        compTable0.put("-D", "001111");
        compTable0.put("-A", "110011");
        compTable0.put("D+1", "011111");
        compTable0.put("A+1", "110111");
        compTable0.put("D-1", "001110");
        compTable0.put("A-1", "110010");
        compTable0.put("D+A", "000010");
        compTable0.put("D-A", "010011");
        compTable0.put("A-D", "000111");
        compTable0.put("D&A", "000000");
        compTable0.put("D|A", "010101");

        HashMap<String, String> compTable1 = new HashMap<>();
        compTable1.put("M", "110000");
        compTable1.put("!M", "110001");
        compTable1.put("-M", "110011");
        compTable1.put("M+1", "110111");
        compTable1.put("M-1", "110010");
        compTable1.put("D+M", "000010");
        compTable1.put("D-M", "010011");
        compTable1.put("M-D", "000111");
        compTable1.put("D&M", "000000");
        compTable1.put("D|M", "010101");

        compTable.put("0", compTable0);
        compTable.put("1", compTable1);

        // Make the dest table
        destTable.put(null, "000");
        destTable.put("M", "001");
        destTable.put("D", "010");
        destTable.put("MD", "011");
        destTable.put("A", "100");
        destTable.put("AM", "101");
        destTable.put("AD", "110");
        destTable.put("AMD", "111");

        // Make the jump table
        jumpTable.put(null, "000");
        jumpTable.put("JGT", "001");
        jumpTable.put("JEQ", "010");
        jumpTable.put("JGE", "011");
        jumpTable.put("JLT", "100");
        jumpTable.put("JNE", "101");
        jumpTable.put("JLE", "110");
        jumpTable.put("JMP", "111");
    }

    public static void main(String[] args) {
        String filename = "/home/shrisharanyan/3_College/EOC/Assembler/files/Rect.asm";

        try {
            FileReader fr = new FileReader(filename);
            BufferedReader br = new BufferedReader(fr);

            File output = new File("/home/shrisharanyan/3_College/EOC/Assembler/files/Output.hack");
            FileWriter fw = new FileWriter(output);
            BufferedWriter bw = new BufferedWriter(fw);

            // Read file line by line
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("")) {
                    continue;
                }
                if (!line.startsWith("//")) {
                    String binaryInstruction = parseInstruction(line);
                    if (binaryInstruction != null) {
                        bw.write(binaryInstruction);
                        bw.newLine();
                    }
                }
            }

            bw.close();
            br.close();
            System.out.println("Successful!");
        } catch (IOException e) {
            System.out.print(e.getMessage());
        }
    }

    // Classify the instructions and parse through them
    private static String parseInstruction(String line) {
        if (isAInstruction(line)) {
            line = cleanLines(line);
            return parseAInstruction(line);
        } else if (isCInstruction(line)) {
            return parseCInstruction(line);
        }
        return null;
    }

    private static boolean isAInstruction(String line) {
        return line.startsWith("@") || line.startsWith("(");
    }

    private static boolean isCInstruction(String line) {
        return !isAInstruction(line) && !Character.isDigit(line.charAt(1));
    }

    private static String cleanLines(String line) {
        return line.replace("\n", "").trim().substring(1);
    }

    // convert string value to binary
    private static String toBinaryString(String value) {
        int intValue;
        try {
            intValue = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid value: " + value);
        }
        return String.format("%16s", Integer.toBinaryString(intValue)).replace(' ', '0');
    }

    // A Instruction to Binary Code
    private static String parseAInstruction(String line) {
        if (symbolTable.containsKey(line)) {
            return toBinaryString(symbolTable.get(line));
        } else {
            try {
                int value = Integer.parseInt(line);
                return toBinaryString(String.valueOf(value));
            } catch (NumberFormatException e) {
                symbolTable.put(line, Integer.toString(symbolTable.size() + 16));
                return toBinaryString(symbolTable.get(line));
            }
        }
    }

    // C Instruction to Binary Code
    private static String parseCInstruction(String line) {
        String dest = null;
        String comp;
        String jump = null;

        if (line.contains("=") && line.contains(";")) {
            int equalIndex = line.indexOf("=");
            int semicolonIndex = line.indexOf(";");

            dest = line.substring(0, equalIndex);
            comp = line.substring(equalIndex + 1, semicolonIndex);
            jump = line.substring(semicolonIndex + 1);
        } else if (line.contains("=")) {
            int equalIndex = line.indexOf("=");

            dest = line.substring(0, equalIndex);
            comp = line.substring(equalIndex + 1);
        } else {
            int semicolonIndex = line.indexOf(";");

            comp = line.substring(0, semicolonIndex);
            jump = line.substring(semicolonIndex + 1);
        }

        String binaryComp = getBinaryComp(comp);
        String binaryDest = getBinaryDest(dest);
        String binaryJump = getBinaryJump(jump);

        return "111" + binaryComp + binaryDest + binaryJump;
    }

    // comp to binary
    private static String getBinaryComp(String comp) {
        boolean isAInstruction = comp.contains("M");
        HashMap<String, String> compTableEntry = compTable.get(isAInstruction ? "1" : "0");
        if (isAInstruction) {
            return "1" + compTableEntry.get(comp);
        } else {
            return "0" + compTableEntry.get(comp);
        }
        
    }

    // dest to binary
    private static String getBinaryDest(String dest) {
        return destTable.get(dest);
    }

    // jump to binary
    private static String getBinaryJump(String jump) {
        return jumpTable.get(jump);
    }

}
