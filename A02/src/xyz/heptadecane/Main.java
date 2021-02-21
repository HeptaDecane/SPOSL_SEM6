package xyz.heptadecane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        String line,file;
        BufferedReader bufferedReader = null;
        Assembler assembler = new Assembler();

        System.out.print("Intermediate Code: ");
        file = scanner.nextLine();
        assembler.setIntermediateCodeFile(file);

        System.out.print("Symbol Table: ");
        file = scanner.nextLine();
        assembler.setSymbolTableFile(file);

        System.out.print("Literal Table: ");
        file = scanner.nextLine();
        assembler.setLiteralTableFile(file);

        try {
            assembler.passTwo();
            bufferedReader = new BufferedReader(new FileReader("OBJECT_CODE.txt"));
            System.out.println("\nObject Code:");
            while ((line=bufferedReader.readLine())!=null)
                System.out.println(line);
            bufferedReader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
