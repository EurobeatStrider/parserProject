import java.util.ArrayList;

class parser {
    private int indent = 0;
    private int index = 0;
    private String output = "";
    private ArrayList parserTokens = new ArrayList<token>();

    public void parseProgram(ArrayList<token> list){
        System.out.println(getIndent(indent) +"<Program>");
        indent++;
        parseStmtList(list);
        indent--;
        System.out.println(getIndent(indent) +"</Program>");
    }

    public void parseStmtList(ArrayList<token> list){
        System.out.println(getIndent(indent) +"<Stmt_List>");
        indent++;
        if(index <= list.size()) { //Possible error point.
            parseStmt(list);
            parseStmtList(list);
        }
        indent--;
        System.out.println(getIndent(indent) +"<Stmt_List>");
    }

    public void parseStmt(ArrayList<token> list){
        System.out.println(getIndent(indent) +"<Stmt>");
        indent++;
        if(list.get(index).equals("id")){ //Form must be 'id' 'assign' <expr>
            parseID(list); //Parse ID
            index++; //advances to next token.
            if(list.get(index).equals("=")) { //Checks to make sure the input is right
                System.out.println(getIndent(indent) + "<Assign>");
                index++;
                System.out.println(getIndent(indent) + list.get(index).get()); //Prints the "="
                index--;
                System.out.println(getIndent(indent) + "</Assign>");
                parseExpr(list); //Advances to expr parser.
            }
            else{
                //toss error here
            }
        }
        if(list.get(index).equals("read")){ //Form must be 'read' <id>
            System.out.println(getIndent(indent) + "<"+list.get(index).get()+">");
            indent++;
            System.out.println(getIndent(indent) + list.get(index).get());
            indent--;
            System.out.println(getIndent(indent) + "</"+list.get(index).get()+">");
            index++; //Advance index.
            parseID(list); //ID Check
        }
        if(list.get(index).equals("write")){ //Form must be 'write' <expr>
            System.out.println(getIndent(indent) + "<"+list.get(index).get()+">");
            indent++;
            System.out.println(getIndent(indent) + list.get(index).get());
            indent--;
            System.out.println(getIndent(indent) + "</"+list.get(index).get()+">");
            index++; //Advance index.
            parseExpr(list); //Parse Expression
        }
        else{
            //Toss an error here.
        }
        indent--;
        System.out.println(getIndent(indent) + "</Stmt>");
    }

    public void parseExpr(ArrayList<token> list){
        parseTerm(list);
        index++;
        parseTermTail(list);
    }

    public void parseTermTail(ArrayList<token> list){
    }

    public void parseTerm(ArrayList<token> list){
    }

    public void parseFactTail(ArrayList<token> list){
    }

    public void parseFactor(ArrayList<token> list){
    }

    public void parseAddOp(ArrayList<token> list){
    }

    public void parseMultOp(ArrayList<token> list){
    }

    public void parseID(ArrayList<token> list){
        if(list.get(index).get() == "id"){
            System.out.println(getIndent(indent) + "<ID>");
            indent++;
            System.out.println(getIndent(indent) + list.get(index).getID());
            indent--;
            System.out.println(getIndent(indent) + "</ID>");
        }
        else {
            //Toss error!
        }
    }

    private String getIndent(int indent) {
        String out = "";
        for(int i = 0; i < indent; i++) {
            out += "\t";
        }
        return out;
    }

}
