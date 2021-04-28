/*
 * Project 2 parse v1.0
 *
 */
//Meremikwu and Nee
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

public class scanner
{
    public static void main(String[] args) throws IOException
    {
        ScanLogic newScan = new ScanLogic();
        parser newParser = new parser();
        StringBuilder rawInput = new StringBuilder();   //content of file
        Scanner reader = new Scanner(new File(args[0]));

        while(reader.hasNext()) {
            rawInput.append(reader.nextLine());
            rawInput.append(" \n ");
        }

        reader.close();

        //System.out.println(rawInput.toString().substring(1,2));

        newScan.scan(rawInput.toString());

        newScan.printTokens();
        newScan.pushToken(new Token("$$","$$"));
        newParser.parseProgram(newScan.getTokens());
    }
}

