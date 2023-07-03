import java.util.List;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {
        String filePath = "D:\\Birzeit\\Compiler\\recursive-descentv2\\src\\TestFiles\\example5.txt";
        Tokenizer tokenizer = new Tokenizer();
        List<String> tokens = tokenizer.tokenizeFile(filePath);
        RecursiveDescentParser parser = new RecursiveDescentParser(tokens);
        parser.parse();
        System.out.println("Parsing successful!");
    }
}