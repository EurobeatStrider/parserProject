import java.util.ArrayList;

class parser {
    private int indent = 0;
    private int index = 0;
    private ArrayList parserTokens = new ArrayList<Token>();

    public void parseProgram(ArrayList<Token> list){
        if(list.get(index).get().equals("ID") || list.get(index).get().equals("read") || list.get(index).get().equals("write")
        || list.get(index).get().equals("$$")){
            System.out.println(getIndent(indent) +"<Program>");
            parseStmtList(list);
            System.out.println(getIndent(indent) +"</Program>");
        }
        else {
            System.out.println("ERROR: LIST WAS TAMPERED WITH");
        }
    }

    public void parseStmtList(ArrayList<Token> list){
        indent++;
        System.out.println(getIndent(indent) +"<Stmt_List>");
        if(list.get(index).get().equals("ID") || list.get(index).get().equals("read") || list.get(index).get().equals("write")){
            parseStmt(list);
            parseStmtList(list);
        }
        else if(list.get(index).get().equals("$$")){
            //skip
        }
        else {
            //End of Valid List.
        }
        System.out.println(getIndent(indent) +"<Stmt_List>");
        indent--;
    }

    public void parseStmt(ArrayList<Token> list){
        indent++;
        System.out.println(getIndent(indent) +"<Stmt>");
        indent++;
        if(list.get(index).get().equals("ID")){ //Form must be 'id' 'assign' <expr>
            parseID(list); //Parse ID
            if(raiseIndex(index, list)) {
                if(list.get(index).get().equals("assign")) { //Checks to make sure the input is right
                    System.out.println(getIndent(indent) + "<Assign>");
                    indent++;
                    System.out.println(getIndent(indent) + list.get(index).get()); //Prints the "="
                    indent--;
                    System.out.println(getIndent(indent) + "</Assign>");
                    indent--;
                    if(raiseIndex(index,list))
                        parseExpr(list); //Advances to expr parser.
                    else
                        System.out.println("ERROR: END OF TOKENS/EXPECTED EXPR");
                }
            }
        }
        else if(list.get(index).get().equals("read")){ //Form must be 'read' <id>
            System.out.println(getIndent(indent) + "<"+list.get(index).get()+">");
            indent++;
            System.out.println(getIndent(indent) + list.get(index).get());
            indent--;
            System.out.println(getIndent(indent) + "</"+list.get(index).get()+">");
            indent--;
            if(raiseIndex(index,list)){
                parseID(list); //ID Check
                raiseIndex(index,list);
            }
            else{
                System.out.println("ERROR: END OF TOKENS/EXPECTED ID");
            }
        }
        else if(list.get(index).get().equals("write")){ //Form must be 'write' <expr>
            System.out.println(getIndent(indent) + "<"+list.get(index).get()+">");
            indent++;
            System.out.println(getIndent(indent) + list.get(index).get());
            indent--;
            System.out.println(getIndent(indent) + "</"+list.get(index).get()+">");
            indent--;
            if(raiseIndex(index,list))
                parseExpr(list); //Parse Expression
            else
                System.out.println("ERROR: END OF TOKENS/EXPECTED EXPR");
        }
        else{
            System.out.println("ERROR: INVALID TOKEN IN PARSESTATEMENT");
        }
        System.out.println(getIndent(indent) + "</Stmt>");
        indent--;
    }

    public void parseExpr(ArrayList<Token> list){
        indent++;
        System.out.println(getIndent(indent) + "<expr>");
        if(list.get(index).get().equals("ID") || list.get(index).get().equals("DIGIT") || list.get(index).get().equals("lparen")){
            parseTerm(list);
            parseTermTail(list);
            System.out.println(getIndent(indent) + "</expr>");
            indent--;
        }
        else{
            System.out.println("ERROR: PARSE EXPR TOSSED INVALID");
        }
    }

    public void parseTermTail(ArrayList<Token> list){
        indent++;
        System.out.println(getIndent(indent) + "<term_tail>");
        if(list.get(index).get().equals("plus") || list.get(index).get().equals("minus")) {
            parseAddOp(list);
            parseTerm(list);
            parseTermTail(list);
        }
        else if(list.get(index).get().equals("rparen") || list.get(index).get().equals("ID") || list.get(index).get().equals("read")
        || list.get(index).get().equals("write") || list.get(index).get().equals("$$")){
            //skip
        }
        else{
            System.out.println("ERROR: TERMTAIL GIVEN BAD");
        }
        System.out.println(getIndent(indent) + "</term_tail>");
        indent--;
    }

    public void parseTerm(ArrayList<Token> list){
        indent++;
        System.out.println(getIndent(indent) + "<term>");
        if(list.get(index).get().equals("ID") || list.get(index).get().equals("DIGIT") || list.get(index).get().equals("lparen")){
            parseFactor(list);
            parseFactTail(list);
        }
        else{
            System.out.println("ERROR: PARSETERM PASSED BAD VALUE");
        }
        System.out.println(getIndent(indent) + "</term>");
        indent--;
    }

    public void parseFactTail(ArrayList<Token> list){
        indent++;
        System.out.println(getIndent(indent) + "<fact_tail>");
        if(list.get(index).get().equals("times") || list.get(index).get().equals("divide")){
            parseMultOp(list);
            parseFactor(list);
            parseFactTail(list);
        }
        else if(list.get(index).get().equals("plus") || list.get(index).get().equals("minus") || list.get(index).get().equals("times") || list.get(index).get().equals("id")
         || list.get(index).get().equals("read") || list.get(index).get().equals("write") || list.get(index).get().equals("rparen") || list.get(index).get().equals("$$"))
        {
            //skip
        }
        else {
            System.out.println("ERROR: FACTTAIL PASSED INVALID TOKEN.");
        }
        System.out.println(getIndent(indent) + "</fact_tail>");
        indent--;
    }

    public void parseFactor(ArrayList<Token> list){
        indent++;
        System.out.println((getIndent(indent) + "<Factor>"));
        if(list.get(index).get().equals("lparen")){ //Form must be lparen <expr> rparen
            indent++;
            System.out.println(getIndent(indent) + "<LPAREN>");
            indent++;
            System.out.println(getIndent(indent) + "(");
            indent--;
            System.out.println(getIndent(indent) + "</LPAREN>");
            indent--;
            raiseIndex(index,list); //??
            parseExpr(list);
            if(list.get(index).get().equals("rparen")){
                indent++;
                System.out.println(getIndent(indent) + "<RPAREN>");
                indent++;
                System.out.println(getIndent(indent) + ")");
                indent--;
                System.out.println(getIndent(indent) + "</RPAREN>");
                indent--;
                raiseIndex(index,list);
            }
            else{
                System.out.println("ERROR: UNCLOSED PARENTHESIS");
            }
        }
        else if(list.get(index).get().equals("ID")){ //
            parseID(list);
            raiseIndex(index,list);
        }
        else if(list.get(index).get().equals("DIGIT")){
            indent++;
            System.out.println(getIndent(indent) + "<DIGIT>");
            indent++;
            System.out.println(getIndent(indent) + list.get(index).getID());
            indent--;
            System.out.println(getIndent(indent) + "</DIGIT>");
            indent--;
            raiseIndex(index,list);
        }
        else {
            System.out.println("ERROR: INVALID TOKEN IN PARSEFACTOR");
        }
        System.out.println((getIndent(indent) + "</Factor>"));
        indent--;
    }

    public void parseAddOp(ArrayList<Token> list){
        if(list.get(index).get().equals("minus") || list.get(index).get().equals("plus")){
            indent++;
            System.out.println(getIndent(indent) + "<AddOp>");
            indent++;
            System.out.println(getIndent(indent) + "<"+list.get(index).get()+">");
            indent++;
            System.out.println(getIndent(indent) + list.get(index).getID());
            indent--;
            System.out.println(getIndent(indent) + "</"+list.get(index).get()+">");
            indent--;
            System.out.println(getIndent(indent) + "</AddOp>");
            indent--;
            raiseIndex(index,list);
        }
        else{
            //toss error
        }
    }

    public void parseMultOp(ArrayList<Token> list){
        if(list.get(index).get().equals("times") || list.get(index).get().equals("divide")){
            indent++;
            System.out.println(getIndent(indent) + "<MultOp>");
            indent++;
            System.out.println(getIndent(indent) + "<"+list.get(index).get()+">");
            indent++;
            System.out.println(getIndent(indent) + list.get(index).getID());
            indent--;
            System.out.println(getIndent(indent) + "</"+list.get(index).get()+">");
            indent--;
            System.out.println(getIndent(indent) + "</MultOp>");
            indent--;
            raiseIndex(index,list);
        }
        else {
            //toss error
        }
    }

    public void parseID(ArrayList<Token> list){
        if (list.get(index).get().equals("ID")) {
            indent++;
            System.out.println(getIndent(indent) + "<ID>");
            indent++;
            System.out.println(getIndent(indent) + list.get(index).getID());
            indent--;
            System.out.println(getIndent(indent) + "</ID>");
            indent--;
        }
        else {
            System.out.println("ERROR: INVALID TOKEN PASSED TO PARSEID");
        }
    }

    private String getIndent(int indent) {
        String out = "";
        for(int i = 0; i < indent; i++) {
            out += "\t";
        }
        return out;
    }

    private boolean raiseIndex(int index, ArrayList<Token> list){
        if(index < list.size()){
            this.index++;
            return true;
        }
        else
            return false;
    }
}
