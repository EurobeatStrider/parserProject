/*
* Project 2 parse v1.0
*
 */

import java.io.IOException;
import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class scanner
{
    public static void main(String[] args) throws IOException
    {
        scanLogic newScan = new scanLogic();
        StringBuilder rawInput = new StringBuilder();   //content of file
        File inputFile = new File("inputFile.txt");
        Scanner reader = new Scanner(inputFile);


        while(reader.hasNext())
            rawInput.append(reader.nextLine());


        reader.close();



        /*for(int i = 0; i < rawInput.length(); i++)
        {
            input[i] = rawInput.charAt(i);
            System.out.println("Char found as " + input[i]);
        }*/

        newScan.scan(rawInput.toString());
    }
}

class scanLogic
{

    public static final Dictionary<String, String> tokenTypes;                          //Holds pair for tokens <actual token, associator >
    private final ArrayList<Token> tokens;

    private boolean tossError;
    private int pos;

    private final Pattern whiteSpacePattern = Pattern.compile("[ \\t\\n]");             //any new line, tab space or whitespace character
    private final Pattern digitPattern = Pattern.compile("^\\.?\\d+\\.?(?:\\d+)?$");    //any digit or float
    private final Pattern wordPattern = Pattern.compile("\\w");                         //any word character
    private final Pattern operatorPattern = Pattern.compile("[+*\\-()]");               //any operator  { +, -, (, ) }

    private Matcher match;  //may not needed


    static {
        tokenTypes = new Hashtable<>();
        tokenTypes.put(":=", "assign");
        tokenTypes.put("+", "plus");
        tokenTypes.put("-", "minus");
        tokenTypes.put("*", "times");
        tokenTypes.put("(", "lparen");
        tokenTypes.put(")", "rparen");
    }

    public scanLogic() {
        tokens = new ArrayList<>();
        tossError = false;
    }

    //Global Scannner
    public void scan(String input)
    {
		for(pos = 0; pos < input.length() && !tossError; pos++) {
            //System.out.println("Checking character " + input[pos]);
            //if (input.charAt(pos) == ' ' || input.charAt(pos) == '\n' || input.charAt(pos) == '\t')

            //check for new line, tab and whitespace characters
            if(checkMatch(input.charAt(pos), whiteSpacePattern)) {
                System.out.println("Whitespace lul");
                continue;
            }

            //checks for id
            if (checkMatch(input.charAt(pos), wordPattern))
                detID(input);

            //check for comment
            else if (input.charAt(pos) == '/')
                divCom(input);

            //check for plus, minus, times, lparen, rparen
            else if(checkMatch(input.charAt(pos), operatorPattern)) //((operatorPattern.matcher(Character.toString(input.charAt(pos)))).find())
                //pushToken(tokenTypes.get(Character.toString(input.charAt((pos)))));
                pushToken(new Token(tokenTypes.get(Character.toString(input.charAt(pos))), null));

            //check for assign
            else if (input.charAt(pos) == ':')
                detAssign(input);

            //check for digit or float type number
            //if a period is spotted then check if next character is a digit, else toss error
            else if(checkMatch(input.charAt(pos), digitPattern) || input.charAt(pos) == '.') {
                detDigit(input);
            }

            else

        }

        if(tossError)
        {
            System.out.println("An error occurred. Please remember to close comments and to make sure : properly assigns.");
            System.out.printf("Invalid syntax at %d\n", pos);
        }
            else
            getTokens();

    }

    //Checks for comment given that current position variable (pos), is positioned a forwards slash(/)
    void divCom(String input)
    {
        String divType = null;
        System.out.println("Checking for div or comment");


        if(pos < input.length() - 1)            //prevents index out of bounds error
            divType = input.substring(pos, pos+1);

        //checks if substring equals '/*'
        if(divType != null && divType.equals("/*")) {
            int prev = pos; //for debugging
            pos += 2;
            while (pos < input.length() - 1 && !input.substring(pos, pos + 1).equals("*/"))
                pos++;
            pos++; // only increment one because scan function loop will handle the rest

            System.out.printf("Comment found from from %d to %d", prev, pos);
            return;
        }

        if(divType.equals("//")) {
            System.out.printf("Div operator found at %d\n", pos);
            pos += 2;
            while (input.charAt(pos) != '\n')
                pos++;
            return;
        }

        pushToken(new Token(tokenTypes.get(Character.toString(input.charAt(pos))), null));
    }

    private boolean checkMatch(char operator, Pattern regexPattern)
    {
        return regexPattern.matcher(Character.toString(operator)).find();
    }

    //function unnecessary as of dictionary implementati on
    void opFour(String input[])
    {
        //an operator had to be tossed here
        if(input[pos] == "+")
            pushToken("plus");
        if(input[pos] == "-")
            pushToken("minus");
        if(input[pos] == "*")
            pushToken("times,");
        if(input[pos] == "(")
            pushToken("(");
        if(input[pos] == ")")
            pushToken(")");
    }

    //Checks for assign tokens
    boolean detAssign(String input)
    {
        String subStr = null;
        if (pos < input.length() - 1)           //prevent index out of bounds error
            subStr= input.substring(pos, pos+1);

        if(subStr != null && subStr.equals(":="))
        {
            //pushToken(operatorTypes.get(subStr));
            pushToken(new Token(tokenTypes.get(subStr), null));
            return true;
        }
        tossError = true;
        return tossError;
    }

    //Cheeckw for digits
    void detDigit(char input[])
    {
        System.out.println("Found number");
        String insert = "";
        insert += input[pos];
        boolean decFound = false;
        if(input[pos] == ".")
            decFound = true;
        while(supportNumber(input[pos+1]) && (input[pos+1] != "*" || !decFound))
        {
            insert += input[pos+1];
            if(input[pos+1] == "*")
                decFound = true;
            pos++;
        }
        pushToken(insert);
    }

    void detID(String input)
    {
        System.out.println("Finding ID");
        String current = "";
        current += input[pos];
        while(supportLetter(input[pos+1]) && input[pos+1] != "!")
        {
            current += input[pos+1];
            if(current.equals("read"))
            {
                pushToken("read");
            }
            else if(current.equals("write"))
            {
                pushToken("write");
            }
            pos++;
        }
        if(!current.equals("read") && !current.equals("write"))
            pushIDToken("id", current);
    }

    //TODO
    void pushToken(Token token)
    {
        if(token != null)
            tokens.add(token);
    }
    void

    void pushIDToken(String type, String id) {
        Token idToken = new Token();
        idToken.setID(type, id);
        tokens.add(idToken);
    }

    void getTokens() //Scope of changes for Project 2
    {
        //We need the parser logic

        for(Token current : tokens)
        {
            System.out.print(current.get() + " ");
        }
    }

    public static Dictionary<String, String> getTokenTypes() {
        return tokenTypes;
    }

    boolean supportNumber(String input)
    {
        match = digitPattern.matcher(input);
        return match.find();
    }
    boolean supportLetter(String input)
    {
        match = wordPattern.matcher(input);
        return match.find();
    }
}

class Token
{
    private String type;
    private String id;
    private String value;

    public Token() {
        type = null;
        id = null;
        value = null;
    }

    public Token(String type, String value) {
        this.type = type;
        this.value = value;
        id = null;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void set(String type)
    {
        this.type = type;
    }
    public void setID(String type, String id) {
        this.type = type;
        this.id = id;
    }
    public String get()
    {
        return type;
    }
    public String getID()
    {
        return id;
    }
}

class parser {
    private int indent = 0;
    private String output = "";
    private ArrayList parserTokens = new ArrayList<Token>();

    public void parseProgram(Token current){
        pushToken("<Program>");
        indent++;
        parseStmtList(current);
        indent--;
        pushToken("</Program>");
    }

    public void parseStmtList(Token current){
        pushToken("<Stmt_List>");
        indent++;
        if(current != null) {
            parseStmt(current);
            parseStmtList(current);
        }
        indent--;
        pushToken("<Stmt_List>");
    }

    public void parseStmt(Token current){
        pushToken("<Stmt>");
        indent++;
        if(current.get().equals("id")){
            pushToken("<ID>");
            indent++;
            pushToken(current.getID());
            indent--;
            pushToken("/ID");
        }
        if(current.get().equals("read")){

        }
        else{
            //Toss an error here.
        }

        indent--;
        pushToken("</Stmt>");
    }

    public void parseExpr(Token current){
    }

    public void parseTermTail(Token current){
    }

    public void parseTerm(Token current){
    }

    public void parseFactTail(Token current){
    }

    public void parseFactor(Token current){
    }

    public void parseAddOp(Token current){
    }

    public void parseMultOp(Token current){
    }

    void pushToken(String type)
    {
        Token newToken = new Token();
        newToken.set(type);
        parserTokens.add(newToken);
    }


}