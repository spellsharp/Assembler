import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
public class whitespace {
    public static void main(String[] args) {
        try {
            FileReader fr = new FileReader("/home/shrisharanyan/3_College/EOC/Assembler/files/Rect.asm");
            BufferedReader br = new BufferedReader(fr); 

            File nowhitespace = new File("/home/shrisharanyan/3_College/EOC/Assembler/files/nowhitespaceRect.asm");
            FileWriter fw = new FileWriter(nowhitespace);
            BufferedWriter bw = new BufferedWriter(fw);

            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("")) continue;
                if (!(line.startsWith("//"))) {
                    if (line.contains("//")) {
                        int commentIndex = line.indexOf("//");
                        line = line.substring(0, commentIndex);
                        line = line.trim();
                    }
                    bw.write(line + '\n');
                    System.out.println(line);
                }
            }
            bw.close();
            br.close();

        } catch (IOException e) {
                System.out.print(e.getMessage());
            }
    }
}