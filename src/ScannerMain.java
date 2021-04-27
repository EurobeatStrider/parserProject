/*
 * Project 2 parse v1.0
 *
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ScannerMain {
    public static void main(String[] args) throws IOException {
        ScanLogic newScan = new ScanLogic();
        StringBuilder rawInput = new StringBuilder();   //content of file
        Scanner reader = new Scanner(new File("inputFile.txt"));

        while (reader.hasNext()) {
            rawInput.append(reader.nextLine());
            rawInput.append("\n");
        }

        reader.close();
        newScan.scan(rawInput.toString());

        newScan.printTokens();
    }
}

