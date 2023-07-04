import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    public List<String> tokenizeFile(String filePath) {
        List<String> tokens = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] lineTokens = line.split("\\s+|(?<=[,;().*/+])|(?=[,;:().*/+])"); // Split line into tokens based on whitespace
                for (String token : lineTokens) {
                    if (!token.isBlank()) {
                        tokens.add(token);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tokens;
    }
}
