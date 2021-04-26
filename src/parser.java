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
        if(raiseIndex(index, list)){
            System.out.println(getIndent(indent) +"<Stmt_List>");
            indent++;
            parseStmt(list);
            parseStmtList(list);
            indent--;
            System.out.println(getIndent(indent) +"<Stmt_List>");
        }
        else {
            //End of Valid List.
        }
    }

    public void parseStmt(ArrayList<token> list){
        if(raiseIndex(index, list)) {
            System.out.println(getIndent(indent) +"<Stmt>");
            indent++;
            if(list.get(index).equals("id")){ //Form must be 'id' 'assign' <expr>
                parseID(list); //Parse ID
                if(raiseIndex(index, list)) {
                    if(list.get(index).equals("=")) { //Checks to make sure the input is right
                        System.out.println(getIndent(indent) + "<Assign>");
                        indent++;
                        System.out.println(getIndent(indent) + list.get(index).get()); //Prints the "="
                        indent--;
                        System.out.println(getIndent(indent) + "</Assign>");
                        parseExpr(list); //Advances to expr parser.
                        indent--;
                        System.out.println(getIndent(indent) + "</Stmt>");
                    }
                }
            }
            else if(list.get(index).equals("read")){ //Form must be 'read' <id>
                System.out.println(getIndent(indent) + "<"+list.get(index).get()+">");
                indent++;
                System.out.println(getIndent(indent) + list.get(index).get());
                indent--;
                System.out.println(getIndent(indent) + "</"+list.get(index).get()+">");
                parseID(list); //ID Check
                indent--;
                System.out.println(getIndent(indent) + "</Stmt>");
            }
            else if(list.get(index).equals("write")){ //Form must be 'write' <expr>
                System.out.println(getIndent(indent) + "<"+list.get(index).get()+">");
                indent++;
                System.out.println(getIndent(indent) + list.get(index).get());
                indent--;
                System.out.println(getIndent(indent) + "</"+list.get(index).get()+">");
                parseExpr(list); //Parse Expression
                indent--;
                System.out.println(getIndent(indent) + "</Stmt>");
            }
            else{
                //Toss an error here.
            }
        }
    }

    public void parseExpr(ArrayList<token> list){
        if(raiseIndex(index, list)) {
            parseTerm(list);
            parseTermTail(list);
        }
        else {
            //toss error.
        }
    }

    public void parseTermTail(ArrayList<token> list){
        if(raiseIndex(index, list)) {
            parseAddOp(list);
            parseFactor(list);
            parseTermTail(list);
        }
        else{
            //end of valid list found.
        }
    }

    public void parseTerm(ArrayList<token> list){
        if(raiseIndex(index,list)){
            parseFactor(list);
            parseFactTail(list);
        }
    }

    public void parseFactTail(ArrayList<token> list){
        if(raiseIndex(index,list)){
            parseMultOp(list);
            parseFactor(list);
            parseFactTail(list);
        }
        else {
            //end of valid list found.
        }
    }

    public void parseFactor(ArrayList<token> list){
    }

    public void parseAddOp(ArrayList<token> list){
    }

    public void parseMultOp(ArrayList<token> list){
    }

    public void parseID(ArrayList<token> list){
        if(raiseIndex(index, list)) {
            if (list.get(index).get() == "id") {
                indent++;
                System.out.println(getIndent(indent) + "<ID>");
                indent++;
                System.out.println(getIndent(indent) + list.get(index).getID());
                indent--;
                System.out.println(getIndent(indent) + "</ID>");
                indent--;
            }
            else {
                //Toss error!
            }
        }
    }

    private String getIndent(int indent) {
        String out = "";
        for(int i = 0; i < indent; i++) {
            out += "\t";
        }
        return out;
    }

    private boolean raiseIndex(int index, ArrayList<token> list){
        if(index < list.size()){
            index++;
            return true;
        }
        else
            return false;
    }

}
