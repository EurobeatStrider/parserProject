import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.regex.Pattern;

public class ScanLogic {

    public static final Dictionary<String, String> TokenTypes;                          //Holds pair for tokens <token identifier, parse tree association>
    private final ArrayList<Token> tokens;

    private boolean tossError;
    private int pos;

    private final Pattern whiteSpacePattern = Pattern.compile("[ \\t\\n]");             //any new line, tab space or whitespace character
    private final Pattern digitPattern = Pattern.compile("^\\.?\\d+\\.?(?:\\d+)?$");    //any digit or float
    private final Pattern IDPattern = Pattern.compile("^[a-zA-Z_]+(?:\\w|-)*$");              //any word character
    private final Pattern operatorPattern = Pattern.compile("[+*\\-()]");               //any operator  { +, -, (, ) }

    static {
        TokenTypes = new Hashtable<>();
        TokenTypes.put(":=", "assign");
        TokenTypes.put("+", "plus");
        TokenTypes.put("-", "minus");
        TokenTypes.put("*", "times");
        TokenTypes.put("/", "div");
        TokenTypes.put("(", "lparen");
        TokenTypes.put(")", "rparen");
        TokenTypes.put("id", "ID");
        TokenTypes.put("digit", "DIGIT");
        TokenTypes.put("read", "read");
        TokenTypes.put("write", "write");

    }

    public ScanLogic() {
        tokens = new ArrayList<>();
        tossError = false;
    }

    //Global Scannner
    public void scan(String input) {
        for (pos = 0; pos < input.length() && !tossError; pos++) {

            //check for new line, tab and whitespace characters
            if (checkMatch(input.charAt(pos), whiteSpacePattern)) {
                continue;
            }

            //checks for id
            if (checkMatch(input.charAt(pos), IDPattern)) {
                detID(input);
            }

            //check for comment
            else if (input.charAt(pos) == '/')
                divCom(input);

                //check for plus, minus, times, lparen, rparen
            else if (checkMatch(input.charAt(pos), operatorPattern)) //((operatorPattern.matcher(Character.toString(input.charAt(pos)))).find())
                pushToken(new Token(
                        TokenTypes.get(Character.toString(input.charAt(pos))),
                        Character.toString(input.charAt(pos))
                ));

                //check for assign
            else if (input.charAt(pos) == ':') {
                detAssign(input);
            }

            //check for digit or float type number
            //if a period is spotted then check if next character is a digit, else toss error
            else if (checkMatch(input.charAt(pos), digitPattern) || input.charAt(pos) == '.') {
                detDigit(input);

            } else
                tossError = true;
        }

        if (tossError) {
            System.out.println("An error occurred. Please remember to close comments and to make sure : properly assigns.");
            System.out.printf("Invalid syntax at %d '%c' \n", pos, input.charAt(pos));
        }


    }

    //Checks for comment given that current position variable (pos), is positioned a forwards slash(/)
    void divCom(String input) {
        String divType = null;

        if (pos < input.length() - 1)            //prevents index out of bounds error
            divType = input.substring(pos, pos + 2);

        //checks if substring equals '/*'
        if (divType != null && divType.equals("/*")) {
            pos += 2;
            while (pos < input.length() - 1 && !input.substring(pos, pos + 2).equals("*/"))
                pos++;
            pos += 2; // only increment one because scan function loop will handle the rest

            return;
        }

        assert divType != null;
        if (divType.equals("//")) {
            System.out.printf("Div operator found at %d\n", pos);
            pos += 2;
            while (input.charAt(pos) != '\n')
                pos++;
            return;
        }

        pushToken(new Token(
                TokenTypes.get(Character.toString(input.charAt(pos))),
                Character.toString(input.charAt(pos))
        ));
    }

    private boolean checkMatch(char operator, Pattern regexPattern) {
        return regexPattern.matcher(Character.toString(operator)).find();
    }

    //Checks for assign tokens
    void detAssign(String input) {
        String subStr = null;
        if (pos < input.length() - 1)           //prevent index out of bounds error
            subStr = input.substring(pos, pos + 2);

        if (subStr != null && subStr.equals(":=")) {
            //pushToken(operatorTypes.get(subStr));
            pushToken(new Token(
                    TokenTypes.get(subStr), subStr
            ));

            pos++;
            return;
        }
        tossError = true;
    }

    //Check for digits
    void detDigit(String input) {
        int beginning = pos;
        if (input.charAt(pos) == '.')
            pos += 2;

        pos++;
        for (; pos < input.length(); pos++) {
            if (!digitPattern.matcher(input.substring(beginning, pos)).find())
                break;
        }

        if (digitPattern.matcher(input.substring(beginning, --pos)).find()) {
            pushToken(new Token(TokenTypes.get("digit"), input.substring(beginning, pos)));
            pos--;
            return;
        }

        tossError = true;
    }

    void detID(String input) {
        String read, write;
        int beginning = pos;
        boolean isValid;

        //check for I/O (read write)
        if (pos + 4 < input.length()) {

            read = input.substring(pos, pos + 4);
            write = input.substring(pos, pos + 5);

            if (read.equalsIgnoreCase("read") || write.equalsIgnoreCase("write")) {
                if (read.equalsIgnoreCase("read")) {
                    pushToken(new Token(TokenTypes.get(read.toLowerCase()), read));
                    pos += 3;
                } else {
                    pushToken(new Token(TokenTypes.get(write.toLowerCase()), write));
                    pos += 4;
                }
                return;
            }
        }

        //while regex pattern upholds and index is less then length of string, move forward
        pos++;
        for (; pos < input.length() - 1; pos++) {
            if (!IDPattern.matcher(input.substring(beginning, pos)).find())
                break;
        }

        isValid = IDPattern.matcher(input.substring(beginning, --pos)).find();

        if (isValid) {
            pushToken(new Token(TokenTypes.get("id"), input.substring(beginning, pos)));
            return;
        }

        tossError = true;

    }

    public void printTokens() {
        StringBuilder list = new StringBuilder();
        list.append("Token List\n");
        list.append("{\n");
        for (Token t : tokens)
            list.append(String.format("\t%s\n", t.toString()));
        list.append("}\n");
        System.out.println(list);
    }

    private void pushToken(Token token) {
        if (token != null)
            tokens.add(token);
    }
}