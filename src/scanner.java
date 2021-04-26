import java.io.IOException;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

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

