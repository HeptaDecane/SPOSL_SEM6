package xyz.heptadecane;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MacroProcessor {
    private HashMap<String,Integer> nameTable;
    private ArrayList<String> definitionTable;
    private String nameFile, definitionFile, codeFile;
    private String output;

    public MacroProcessor(){
        this(null,null,null);
    }

    public MacroProcessor(String nameFile, String definitionFile, String codeFile){
        this.nameFile = nameFile;
        this.definitionFile = definitionFile;
        this.codeFile = codeFile;
        nameTable = new HashMap<>();
        definitionTable = new ArrayList<>();
        output = "";
    }

    public void setNameFile(String nameFile) {
        this.nameFile = nameFile;
    }

    public void setDefinitionFile(String definitionFile) {
        this.definitionFile = definitionFile;
    }

    public void setCodeFile(String codeFile) {
        this.codeFile = codeFile;
    }

    private void loadNameTable() throws Exception{
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(nameFile));
        while ((line=bufferedReader.readLine()) != null){
            String macroName = line.split("\\s+")[1];
            String definitionIndex = line.split("\\s+")[2];
            nameTable.put(macroName,Integer.parseInt(definitionIndex));
        }
        bufferedReader.close();
    }

    private void loadDefinitionTable() throws Exception{
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(definitionFile));
        while ((line=bufferedReader.readLine()) != null)
            definitionTable.add(line.split("\\s+",2)[1]);
        bufferedReader.close();
    }

    private HashMap<String,String> mapArguments(String call,String signature){
        String []actualParameters = call.split("\\s+")[1].split(",");
        String []formalParameters = signature.split("\\s+")[1].split(",");
        HashMap<String,String> argumentMap = new HashMap<>();
        int i;

        i=1;
        for(String parameter : formalParameters){
            if(parameter.contains("="))
                argumentMap.put("#"+i,parameter.split("=")[1]);
            i++;
        }

        i=1;
        for(String parameter : actualParameters){
            if(parameter.contains("="))
                argumentMap.put("#"+i,parameter.split("=")[1]);
            else
                argumentMap.put("#"+i,parameter);
            i++;
        }

        return argumentMap;
    }

    private void expandMacro(String call,int definitionIndex){
        String signature = definitionTable.get(definitionIndex);
        HashMap<String,String> argumentMap = mapArguments(call,signature);

        while(true){
            String line = definitionTable.get(++definitionIndex).strip();
            if(line.equalsIgnoreCase("MEND"))
                break;
            for(String key : argumentMap.keySet()){
                if(line.contains(key))
                    line = line.replace(key,argumentMap.get(key));
            }
            output = output+line+"\n";
        }
    }

    public void passTwo() throws Exception{
        if(nameFile==null || definitionFile==null || codeFile==null)
            throw new FileNotFoundException("input file(s) missing");

        loadNameTable();
        loadDefinitionTable();

        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(codeFile));
        while ((line=bufferedReader.readLine()) != null){
            String opcode = line.split("\\s+")[0];
            if(nameTable.containsKey(opcode))
                expandMacro(line, nameTable.get(opcode));
            else
                output = output+line+"\n";
        }
        bufferedReader.close();

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("OUTPUT.txt"));
        bufferedWriter.write(output);
        bufferedWriter.close();
    }
}
