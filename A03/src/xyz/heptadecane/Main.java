package xyz.heptadecane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        BufferedReader bufferedReader = null;
        int index;
        String file,line;
        MacroProcessor macroProcessor = new MacroProcessor();

        System.out.print("File Path: ");
        file = scanner.nextLine();
        macroProcessor.setFile(file);

        try{
            macroProcessor.passOne();

            bufferedReader = new BufferedReader(new FileReader("MNT.txt"));
            System.out.println("\nMacro Name Table:");
            while ((line=bufferedReader.readLine())!=null)
                System.out.println(line);
            bufferedReader.close();

            System.out.println();

            bufferedReader = new BufferedReader(new FileReader("MDT.txt"));
            System.out.println("\nMacro Definition Table:");
            while ((line=bufferedReader.readLine())!=null)
                System.out.println(line);
            bufferedReader.close();

            System.out.println();

            bufferedReader = new BufferedReader(new FileReader("DEFAULT_ARGUMENTS.txt"));
            System.out.println("\nDefault Arguments:");
            while ((line=bufferedReader.readLine())!=null)
                System.out.println(line);
            bufferedReader.close();

            System.out.println();

            bufferedReader = new BufferedReader(new FileReader("CODE.txt"));
            System.out.println("\nCODE:");
            while ((line=bufferedReader.readLine())!=null)
                System.out.println(line);
            bufferedReader.close();

        }catch (Exception e){
            System.out.println(e);
        }
    }
}
