import java.io.IOException;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

//another comment test2
//

public class scanner
{
    public static void main(String args[]) throws IOException
    {
        scanLogic newScan = new scanLogic();
        String rawInput = "";
        File inputFile = new File("inputFile.txt");
        Scanner reader = new Scanner(inputFile);

        //
        while(reader.hasNext())
        {
            rawInput += reader.nextLine();
        }
        rawInput += "$$";
        reader.close();


        char input[] = new char[rawInput.length()];

        for(int i = 0; i < rawInput.length(); i++)
        {
            input[i] = rawInput.charAt(i);
            System.out.println("Char found as " + input[i]);
        }
        System.out.println(rawInput);
        newScan.scan(input);
    }
}

class scanLogic
{
    private boolean tossError = false;
    private boolean foundEnd = false;
    private int indent = 0;
    ArrayList<token> tokens = new ArrayList<token>();
    private int pos = 0;

    private final Pattern intPat = Pattern.compile("\\d");
    private final Pattern wordPat = Pattern.compile("\\w");
    private Matcher match;

    public void scan(char input[])
    {
		for(int pos = 0; pos < input.length && !tossError; pos++) {
            System.out.println("Checking character " + input[pos]);
            if (input[pos] == ' ' || input[pos] == '\n' || input[pos] == '\t')
                System.out.println("Whitespace lul");
            else if (input[pos] == '\\')
                divCom(input);
            else if (input[pos] == '*' || input[pos] == '+' || input[pos] == '-' || input[pos] == '(' || input[pos] == ')')
                opFour(input);
            else if (input[pos] == ':' || input[pos] == '=')
                detAssign(input);
            else if (supportNumber(input[pos]))
                detDigit(input);
            else if (input[pos] != '$')
                idDetector(input);
            else if (input[pos] == '$' && input[pos + 1] == '$') {
                pushToken("$$");
            }
        }

        if(tossError)
            System.out.println("An error occurred. Please remember to close comments and to make sure : properly assigns.");
        else
            getTokens();

    }

    //Checks for comments in code
    void divCom(char input[])
    {
        System.out.println("Checking as \\");
        if(input[pos+1] == '*')
        {
            pos+=2;
            while((input[pos] != '*' && input[pos+1] != '\\'))
            {
                pos++;
            }
            pos+=2;
        }

        else if(input[pos+1] == '\\')
        {
            pos+=2;
            while((input[pos+1] == '\n'))
            {
                pos++;
            }
            pos+=2;
        }
        else
        {
            pushToken("\\");
        }
    }

    //Checks for specified tokenn
    void opFour(char input[])
    {
        System.out.println("Found operator");
        //an operator had to be tossed here
        if(input[pos] == '+')
            pushToken("plus");
        if(input[pos] == '-')
            pushToken("minus");
        if(input[pos] == '*')
            pushToken("times,");
        if(input[pos] == '(')
            pushToken("(");
        if(input[pos] == ')')
            pushToken(")");
    }

    //Checks for assign tokens
    boolean detAssign(char input[])
    {
        System.out.println("Found assign");
        if(input[pos+1] == '=')
        {
            pos++;
            pushToken("=");
            return false;
        }
        else
            return true;
    }

    //Cheeckw for digits
    void detDigit(char input[])
    {
        System.out.println("Found number");
        String insert = "";
        insert += input[pos];
        boolean decFound = false;
        if(input[pos] == '.')
            decFound = true;
        while(supportNumber(input[pos+1]) && (input[pos+1] != '*' || !decFound))
        {
            insert += input[pos+1];
            if(input[pos+1] == '*')
                decFound = true;
            pos++;
        }
        pushToken(insert);
    }

    void idDetector(char input[])
    {
        System.out.println("Finding ID");
        String current = "";
        current += input[pos];
        while(supportLetter(input[pos+1]) && input[pos+1] != '!')
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
        if(current.equals("read") == false && current.equals("write") == false)
            pushIDToken("id", current);
    }

    void pushToken(String type)
    {
        token newToken = new token();
        newToken.set(type);
        tokens.add(newToken);
    }

    void pushIDToken(String type, String id) {
        token idToken = new token();
        idToken.setID(type, id);
        tokens.add(idToken);
    }

    void getTokens() //Scope of changes for Project 2
    {
        //We need the parser logic

        for(token current : tokens)
        {
            System.out.print(current.get() + " ");
        }
    }

    boolean supportNumber(char input)
    {
        match = intPat.matcher(Character.toString(input));
        return match.find();
    }
    boolean supportLetter(char input)
    {
        match = wordPat.matcher(Character.toString(input));
        return match.find();
    }
}

class token
{
    String type;
    String id;

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
    private ArrayList parserTokens = new ArrayList<token>();

    public void parseProgram(token current){
        pushToken("<Program>");
        indent++;
        parseStmtList(current);
        indent--;
        pushToken("</Program>");
    }

    public void parseStmtList(token current){
        pushToken("<Stmt_List>");
        indent++;
        if(current != null) {
            parseStmt(current);
            parseStmtList(current);
        }
        indent--;
        pushToken("<Stmt_List>");
    }

    public void parseStmt(token current){
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

    public void parseExpr(token current){
    }

    public void parseTermTail(token current){
    }

    public void parseTerm(token current){
    }

    public void parseFactTail(token current){
    }

    public void parseFactor(token current){
    }

    public void parseAddOp(token current){
    }

    public void parseMultOp(token current){
    }

    void pushToken(String type)
    {
        token newToken = new token();
        newToken.set(type);
        parserTokens.add(newToken);
    }


}