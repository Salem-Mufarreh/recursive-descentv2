import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class RecursiveDescentParser {
    private List<String> tokens;
    private int index;
    private String currentToken;
    private List<String> terminals = new ArrayList<>();
    private List<String> reserved = new ArrayList<>();
    private List<String> operations = new ArrayList<>();


    public RecursiveDescentParser(List<String> tokens) {
        this.tokens = tokens;
        this.index = 0;
        this.currentToken = tokens.get(0);
    }

    public void parse() {
        init();
        projectDeclaration();
    }

    private void match(String expectedToken) {
        if (expectedToken.equals("userDefined")){
            index++;
            currentToken = tokens.get(index);
        }

        else if (currentToken.equals(expectedToken)) {
            index++;
            if (index < tokens.size()) {
                currentToken = tokens.get(index);
            }
        } else {
            throw new RuntimeException("Syntax Error: Expected '" + expectedToken + "', found '" + currentToken + "' at index: "+ index);
        }
    }

    private void projectDeclaration() {
        projectDef();
        match(".");
    }

    private void projectDef() {
        projectHeading();
        declarations();
        compoundStmt();
    }

    private void projectHeading() {
        match("project");
        nameValue();
        match(";");
    }

    private void declarations() {
        constDecl();
        varDecl();
        subroutineDecl();
    }

    private void constDecl() {
        if (currentToken.equals("const")) {
            match("const");
            while (isIdentifier(currentToken)) {
                nameValue();
                match("=");
                matchInteger();
                if (currentToken.equals(";")){
                    match(";");
                }
                else{
                    break;
                }

            }
        }
    }

    private void varDecl() {
        if (currentToken.equals("var")) {
            match("var");
            while (isIdentifier(currentToken)) {
                nameList();
                match(":");
                match("int");
                match(";");
            }
        }
    }

    private void nameList() {
        nameValue();
        while (currentToken.equals(",")) {
            match(",");
            nameValue();
        }
    }

    private void subroutineDecl() {
        if (currentToken.equals("routine")) {
            match("routine");
            nameValue();
            match(";");
            declarations();
            compoundStmt();
            match(";");
        }
    }

    private void compoundStmt() {
        match("start");
        stmtList();
        match("end");
    }

    private void stmtList() {
        while (!currentToken.equals("end")) {
            statement();
            match(";");
        }
    }

    private void statement() {
        if (isIdentifier(currentToken)) {
            nameValue();
            match(":=");
            arithExp();
        } else if (currentToken.equals("input")) {
            match("input");
            match("(");
            nameValue();
            match(")");
        } else if (currentToken.equals("output")) {
            match("output");
            match("(");
            nameValue();
            match(")");
        } else if (currentToken.equals("if")) {
            match("if");
            match("(");
            boolExp();
            match(")");
            match("then");
            statement();
            elsePart();
            match("endif");
        } else if (currentToken.equals("loop")) {
            match("loop");
            match("(");
            boolExp();
            match(")");
            match("do");
            statement();
        } else if(currentToken.equals("start")){
            match("start");
            stmtList();
            match("end");
        }
    }

    private void arithExp() {
        term();
        while (currentToken.equals("+") || currentToken.equals("-")) {
            match(currentToken);
            term();
        }
    }

    private void term() {
        factor();
        while (currentToken.equals("*") || currentToken.equals("/") || currentToken.equals("%")) {
            match(currentToken);
            factor();
        }
    }

    private void factor() {
        if (currentToken.equals("(")) {
            match("(");
            arithExp();
            match(")");
        } else{
            if (isIdentifier(currentToken) ) {
                nameValue();
            }
            if (isInteger_Value(currentToken)){
                matchInteger();
            }
        }
    }

    private void nameValue() {
        match(currentToken);
    }

    private void boolExp() {
        nameValue();
        relationalOper();
        nameValue();
    }

    private void relationalOper() {

        if(operations.contains(currentToken)){
            match(currentToken);
        }
        else{
            throw new RuntimeException("Syntax Error: not valid user defined name, at '" + currentToken + "'");
        }

    }

    private void elsePart() {
        if (currentToken.equals("else")) {
            match("else");
            statement();
        }
    }

    private void matchIdentifier() {
        if (isIdentifier(currentToken)) {
            index++;
            if (index < tokens.size()) {
                currentToken = tokens.get(index);
            }
        } else {
            throw new RuntimeException("Syntax Error: not valid user defined name, at '" + currentToken + "'");

        }
    }
    private void matchInteger(){
        if (isInteger_Value(currentToken)) {
            index++;
            if (index < tokens.size()) {
                currentToken = tokens.get(index);
            }
        } else {
            throw new RuntimeException("Syntax Error: not valid integer, at '" + currentToken + "'");

        }
    }
    private boolean isIdentifier(String token) {
        if (reserved.contains(token) || terminals.contains(token)){
            return false;
        }
        // Check if the token is a valid identifier
        if (token.length() == 0 || !Character.isLetter(token.charAt(0)) && token.charAt(0) != '_') {
            return false;
        }

        for (int i = 1; i < token.length(); i++) {
            char ch = token.charAt(i);
            if (!Character.isLetterOrDigit(ch) && ch != '_') {
                return false;
            }
        }

        return true;
    }
    private Boolean isInteger_Value(String token){
        if (reserved.contains(token) || terminals.contains(token)){
            return false;
        }
        try {
            int value = Integer.parseInt(token);
            return true;
        }
        catch (Exception ex){
            return false;
        }
    }

    private void init(){
        String[] arr = {"project", "name", ";", "const", "=", "integer-value", "var", ":", "int",
                "name-list", "routine", "start", "end", "stmt-list", "statement", "ass-stmt",
                "arith-exp", "term", "factor", "(", ")", "add-sign", "mul-sign", "input",
                "output", "if", "then", "else", "endif", "loop", "do", "bool-exp", "relational-oper",
                "=", "<>", "<", "<=", ">", ">="};
        for (String token:arr) {
            terminals.add(token);
        }
        String[] reserve = {"project", "const","var", "int","routine", "start","end","input","output",
                "if","then","endif","else","loop","do"};
        for(String rs : reserve){
            reserved.add(rs);
        }
        String oper[] = {"=", "<>", "<", "<=", ">", ">="};
        for (String token:oper) {
            operations.add(token);
        }

    }
}
