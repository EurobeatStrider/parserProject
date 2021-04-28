import java.nio.channels.ClosedChannelException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Scanner;

public class Parser {
    private final int index = 0;
    private int indent = 0;
    private boolean errorState = false;

    private final String output = "";
    private final String OpenTag = "<%s>\n";
    private final String OpenTagIndent = "%s<%s>\n";
    private final String CloseTag = "</%s>\n";
    private final String CloseTagIndent = "%s</%s>\n";

    //parse list begin
    public void parseProgram(ArrayList<Token> list) {
        System.out.println(getIndent(indent) + "<Program>");
        indent++;
        parseStmtList(list);
        indent--;
        System.out.println(getIndent(indent) + "</Program>");

    }

    public void parseStmtList(ArrayList<Token> list) {
        String type;
        if(list.size() == 0) {
            errorState = true;
            return;
        }

        type = list.get(index).getType();

        if(type.equals("read") || type.equals("write") || type.equals("id")) {
            System.out.printf(OpenTagIndent, getIndent(indent), "stmt_list");
            indent++;
            parseStmt(list);
            parseStmtList(list);
            indent--;
            System.out.printf(CloseTagIndent, getIndent(indent), "stmt_list");
            return;
        }
        errorState = true;
    }



    public void parseStmt(ArrayList<Token> list) {

        System.out.format(OpenTagIndent, getIndent(indent), "stmt");
        indent++;
        String type = list.get(index).getType();//TODO: remove

        if(type.equals("id")){
            System.out.print(tagBlock(indent, type, list.get(index).getId()));
            if(raiseIndex(index, list) && list.get(index).getType().equals("assign")) //if has next token and next token is assign
            {
                System.out.print(tagBlock(indent, list.get(index).getType(), list.get(index).getId()));
                indent++;
                parseExpr(list);
            }
            return;
        }

        if (type.equals("read"))
        {
            if(raiseIndex(index, list) && list.get(index).getType().equals("assign")) //if has next token and next token is assign
            {
                System.out.print(tagBlock(indent, list.get(index).getType(), list.get(index).getId()));
                return;
            }
        }

        if(type.equals("write"))
        {
                System.out.print(tagBlock(indent, list.get(index).getType(), list.get(index).getId()));
                indent++;
                parseExpr(list);
                return;
        }

        errorState = true;
    }

    //parse expression
    private void parseExpr(ArrayList<Token> list) {
        System.out.format(OpenTagIndent, getIndent(indent), "expr");
        String type = list.get(index).getType();

        if((type.equals("id") || type.equals("digit")) && raiseIndex(index, list)) {
            System.out.printf(OpenTagIndent, indent, "expr");
            indent++;
            parseTerm(list);

            if(raiseIndex(index, list))
                parseTermTail(list);

            indent--;
            System.out.printf(CloseTagIndent, indent, "expr");
            return;
        }
        errorState = true;
    }

    public void parseTermTail(ArrayList<Token> list) {
        System.out.format(OpenTagIndent, getIndent(indent), "term_trail");
        String type = list.get(index).getType();


        if(raiseIndex(index, list)) {
            type = list.get(index).getType();
            if (type.equals("plus") || type.equals("minus")) {
                System.out.printf(OpenTagIndent, indent, "expr");
                indent++;
                parseAddOp(list);
                parseTerm(list);
                parseTermTail(list);

                indent--;
                System.out.printf(CloseTagIndent, indent, "expr");
                return;
            }

            if(type.equals("rparen") || type.equals("id") || type.equals("read") || type.equals("write")) {
                System.out.printf(CloseTagIndent, indent, "expr");
                return;
            }

        }
        errorState = true;
    }

    public void parseTerm(ArrayList<Token> list) {
        System.out.printf(OpenTagIndent, getIndent(indent), "term");
        String type = list.get(index).getType();

        if((type.equals("id") || type.equals("digit")) && raiseIndex(index, list)) {
            indent++;
            parseFactor(list);
            parseFactTail(list);
            indent--;

            System.out.printf(CloseTagIndent, getIndent(indent), "term");
            return;
        }
        errorState = true;
    }

    public void parseFactTail(ArrayList<Token> list) {
        String type = list.get(index).getType();

        String tokenType = list.get(index).getType();
        System.out.printf(OpenTagIndent, getIndent(indent), "fact_trail");

        if(raiseIndex(index, list)) {
            indent++;
            if ((tokenType.equals("times") || tokenType.equals("div"))) {
                if(raiseIndex(index, list)) {
                    parseMultOp(list);
                    parseFactor(list);
                    parseFactTail(list);
                }

            } else if (tokenType.equals("lparen")) {
                System.out.println(tagBlock(indent, tokenType, list.get(index).getId()));
                indent++;
                parseExpr(list);
                indent--;

                tokenType = list.get(index).getType();
                if(!tokenType.equals("rparen")) {
                    errorState = true;
                    return;
                }
                System.out.println(tagBlock(indent, tokenType, list.get(index).getId()));

            }

            indent--;
            System.out.printf(CloseTagIndent, getIndent(indent), "factor");
            return;

        }

    }

    public void parseFactor(ArrayList<Token> list) {
        String tokenType = list.get(index).getType();
        System.out.printf(OpenTagIndent, getIndent(indent), "factor");

        if(raiseIndex(index, list)) {
            indent++;
            if ((tokenType.equals("id") || tokenType.equals("number"))) {
                System.out.println(tagBlock(indent, tokenType, list.get(index).getId()));

            } else if (tokenType.equals("lparen")) {
                System.out.println(tagBlock(indent, tokenType, list.get(index).getId()));
                indent++;
                parseExpr(list);
                indent--;

                tokenType = list.get(index).getType();
                if(!tokenType.equals("rparen")) {
                    errorState = true;
                    return;
                }
                System.out.println(tagBlock(indent, tokenType, list.get(index).getId()));

            }

            indent--;
            System.out.printf(CloseTagIndent, getIndent(indent), "factor");
            return;

        }

        errorState = true;
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
        String operator = "*";
        if (list.get(index).getType().equals("div"))
            operator = "/";
        if (raiseIndex(index, list)) {
            if (list.get(index).getType().equals("times") || list.get(index).getType().equals("div")) {
                indent++;
                System.out.printf(OpenTagIndent, getIndent(indent), "mult_op");
                indent++;
                System.out.printf(OpenTagIndent, getIndent(indent), list.get(index).getType());
                indent++;
                System.out.println(getIndent(indent) + operator);
                indent--;
                System.out.printf(CloseTagIndent, getIndent(indent), list.get(index).getType());
                indent--;
                System.out.printf(CloseTagIndent, getIndent(indent), "mult_op");
                indent--;
            } else {
                //toss error
            }
        } else {
            //toss error
        }
    }

    public void parseID(ArrayList<Token> list) {
        if (raiseIndex(index, list)) {
            if (list.get(index).getType().equals("id")) {
                indent++;
                System.out.printf(OpenTagIndent, getIndent(indent), "id");
                indent++;
                System.out.println(getIndent(indent) + list.get(index).getId());
                indent--;
                System.out.printf(CloseTagIndent, getIndent(indent), "id");
                indent--;
                return;
            }
        }
        errorState = true;
    }

    public String tagBlock(Integer indent, String type, String value)
    {
        StringBuilder block = new StringBuilder();
        block.append(String.format(OpenTagIndent, getIndent(indent), type));
        indent++;
        block.append(String.format("%s%s\n", getIndent(indent), value));
        indent--;
        block.append(String.format(OpenTagIndent, getIndent(indent), type));
        return block.toString();
    }

    private String getIndent(int indent) {
        return "\t".repeat(indent);
    }

    private boolean raiseIndex(Integer index, ArrayList<Token> list) {
        if (index < list.size()) {
            index++;
            return true;
        }
        return false;
    }

}
