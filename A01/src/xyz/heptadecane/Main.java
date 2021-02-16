package xyz.heptadecane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        String line;
        BufferedReader bufferedReader = null;
        Assembler assembler = new Assembler();

        System.out.print("File Path: ");
        String file = scanner.nextLine();
        assembler.setFile(file);

        try {
            assembler.passOne();

            bufferedReader = new BufferedReader(new FileReader("INTERMEDIATE_CODE.txt"));
            System.out.println("\nIntermediate Code:");
            while ((line=bufferedReader.readLine())!=null)
                System.out.println(line);
            bufferedReader.close();

            bufferedReader = new BufferedReader(new FileReader("SYMBOL_TABLE.txt"));
            System.out.println("\nSymbol Table:");
            while ((line=bufferedReader.readLine())!=null)
                System.out.println(line);
            bufferedReader.close();

            bufferedReader = new BufferedReader(new FileReader("LITERAL_TABLE.txt"));
            System.out.println("\nLiteral Table:");
            while ((line=bufferedReader.readLine())!=null)
                System.out.println(line);
            bufferedReader.close();

            bufferedReader = new BufferedReader(new FileReader("POOL_TABLE.txt"));
            System.out.println("\nPool Table:");
            while ((line=bufferedReader.readLine())!=null)
                System.out.println(line);
            bufferedReader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
