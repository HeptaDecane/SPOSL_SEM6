package xyz.heptadecane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        BufferedReader bufferedReader = null;
        int index;
        String codeFile,nameFile,definitionFile,line;
        MacroProcessor macroProcessor = new MacroProcessor();

        System.out.print("Code File Path: ");
        codeFile = scanner.nextLine();
        System.out.print("Name File Path: ");
        nameFile = scanner.nextLine();
        System.out.print("Definition File Path: ");
        definitionFile = scanner.nextLine();

        macroProcessor.setCodeFile(codeFile);
        macroProcessor.setNameFile(nameFile);
        macroProcessor.setDefinitionFile(definitionFile);

        try {
            macroProcessor.passTwo();
            bufferedReader = new BufferedReader(new FileReader("OUTPUT.txt"));
            System.out.println();
            while ((line=bufferedReader.readLine())!=null)
                System.out.println(line);
            bufferedReader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
