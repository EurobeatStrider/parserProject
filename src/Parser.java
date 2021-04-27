import java.util.ArrayList;

public class Parser {
    private int indent = 0;
    private final int index = 0;
    private final String output = "";
    private final ArrayList parserTokens = new ArrayList<Token>();

    public void parseProgram(ArrayList<Token> list) {
        System.out.println(getIndent(indent) + "<Program>");
        indent++;
        parseStmtList(list);
        indent--;
        System.out.println(getIndent(indent) + "</Program>");
    }

    public void parseStmtList(ArrayList<Token> list) {
        if (raiseIndex(index, list)) {
            System.out.println(getIndent(indent) + "<Stmt_List>");
            indent++;
            parseStmt(list);
            parseStmtList(list);
            indent--;
            System.out.println(getIndent(indent) + "<Stmt_List>");
        } else {
            //End of Valid List.
        }
    }

    public void parseStmt(ArrayList<Token> list) {
        if (raiseIndex(index, list)) {
            System.out.println(getIndent(indent) + "<Stmt>");
            indent++;
            if (list.get(index).equals("id")) { //Form must be 'id' 'assign' <expr>
                parseID(list); //Parse ID
                if (raiseIndex(index, list)) {
                    if (list.get(index).equals("=")) { //Checks to make sure the input is right
                        System.out.println(getIndent(indent) + "<Assign>");
                        indent++;
                        System.out.println(getIndent(indent) + list.get(index).getType()); //Prints the "="
                        indent--;
                        System.out.println(getIndent(indent) + "</Assign>");
                        parseExpr(list); //Advances to expr parser.
                        indent--;
                        System.out.println(getIndent(indent) + "</Stmt>");
                    }
                }
            } else if (list.get(index).equals("read")) { //Form must be 'read' <id>
                System.out.println(getIndent(indent) + "<" + list.get(index).getType() + ">");
                indent++;
                System.out.println(getIndent(indent) + list.get(index).getType());
                indent--;
                System.out.println(getIndent(indent) + "</" + list.get(index).getType() + ">");
                parseID(list); //ID Check
                indent--;
                System.out.println(getIndent(indent) + "</Stmt>");
            } else if (list.get(index).equals("write")) { //Form must be 'write' <expr>
                System.out.println(getIndent(indent) + "<" + list.get(index).getType() + ">");
                indent++;
                System.out.println(getIndent(indent) + list.get(index).getType());
                indent--;
                System.out.println(getIndent(indent) + "</" + list.get(index).getType() + ">");
                parseExpr(list); //Parse Expression
                indent--;
                System.out.println(getIndent(indent) + "</Stmt>");
            } else {
                //Toss an error here.
            }
        }
    }

    public void parseExpr(ArrayList<Token> list) {
        if (raiseIndex(index, list)) {
            parseTerm(list);
            parseTermTail(list);
        } else {
            //toss error.
        }
    }

    public void parseTermTail(ArrayList<Token> list) {
        if (raiseIndex(index, list)) {
            parseAddOp(list);
            parseFactor(list);
            parseTermTail(list);
        } else {
            //end of valid list found.
        }
    }

    public void parseTerm(ArrayList<Token> list) {
        if (raiseIndex(index, list)) {
            parseFactor(list);
            parseFactTail(list);
        } else {
            //toss error
        }
    }

    public void parseFactTail(ArrayList<Token> list) {
        if (raiseIndex(index, list)) {
            parseMultOp(list);
            parseFactor(list);
            parseFactTail(list);
        } else {
            //end of valid list found.
        }
    }

    public void parseFactor(ArrayList<Token> list) {
        //TODO
    }

    public void parseAddOp(ArrayList<Token> list) {
        String operator = "+";
        if (list.get(index).getType().equals("minus"))
            operator = "-";
        if (raiseIndex(index, list)) {
            if (list.get(index).getType().equals("minus") || list.get(index).getType().equals("plus")) {
                indent++;
                System.out.println(getIndent(indent) + "<AddOp>");
                indent++;
                System.out.println(getIndent(indent) + "<" + list.get(index).getType() + ">");
                indent++;
                System.out.println(getIndent(indent) + operator);
                indent--;
                System.out.println(getIndent(indent) + "</" + list.get(index).getType() + ">");
                indent--;
                System.out.println(getIndent(indent) + "</AddOp>");
                indent--;
            } else {
                //toss error
            }
        } else {
            //toss error
        }
    }

    public void parseMultOp(ArrayList<Token> list) {
        //TODO
    }

    public void parseID(ArrayList<Token> list) {
        if (raiseIndex(index, list)) {
            if (list.get(index).getType() == "id") {
                indent++;
                System.out.println(getIndent(indent) + "<ID>");
                indent++;
                System.out.println(getIndent(indent) + list.get(index).getId());
                indent--;
                System.out.println(getIndent(indent) + "</ID>");
                indent--;
            } else {
                //Toss error!
            }
        }
    }

    private String getIndent(int indent) {
        String out = "";
        for (int i = 0; i < indent; i++) {
            out += "\t";
        }
        return out;
    }

    private boolean raiseIndex(int index, ArrayList<Token> list) {
        if (index < list.size()) {
            index++;
            return true;
        } else
            return false;
    }

}
