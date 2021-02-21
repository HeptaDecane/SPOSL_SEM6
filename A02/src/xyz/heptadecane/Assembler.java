package xyz.heptadecane;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Assembler {
    private final Map<String,Integer> symbolTable;
    private final Map<String,Integer> literalTable;
    private String intermediateCodeFile, symbolTableFile, literalTableFile;
    private final ArrayList<Integer> opCode, register, address;

    public Assembler(){
        this(null,null,null);
    }

    public Assembler(String intermediateCodeFile, String symbolTableFile, String literalTableFile){
        this.intermediateCodeFile = intermediateCodeFile;
        this.symbolTableFile = symbolTableFile;
        this.literalTableFile = literalTableFile;
        symbolTable = new LinkedHashMap<>();
        literalTable = new LinkedHashMap<>();
        opCode = new ArrayList<>();
        register = new ArrayList<>();
        address = new ArrayList<>();
    }

    public void setIntermediateCodeFile(String intermediateCodeFile) {
        this.intermediateCodeFile = intermediateCodeFile;
    }

    public void setSymbolTableFile(String symbolTableFile) {
        this.symbolTableFile = symbolTableFile;
    }

    public void setLiteralTableFile(String literalTableFile) {
        this.literalTableFile = literalTableFile;
    }

    private void loadTables() throws Exception{
        if(symbolTableFile == null)
            throw new FileNotFoundException("symbol table file not found");
        if(literalTableFile == null)
            throw new FileNotFoundException("literal table file not found");

        BufferedReader bufferedReader = null;
        String line;
        String[] tokens;

        bufferedReader = new BufferedReader(new FileReader(symbolTableFile));
        while ((line=bufferedReader.readLine())!=null){
            tokens = line.split("\\s+");
            symbolTable.put(tokens[1],Integer.parseInt(tokens[2]));
        }
        bufferedReader.close();

        bufferedReader = new BufferedReader(new FileReader(literalTableFile));
        while ((line=bufferedReader.readLine())!=null){
            tokens = line.split("\\s+");
            literalTable.put(tokens[1],Integer.parseInt(tokens[2]));
        }
        bufferedReader.close();
    }

    private int evaluateAddress(String expression, Map<String,Integer> table){
        if(expression.contains("+")){
            String[] parts = expression.split("\\+");
            int index = Integer.parseInt(parts[0]);
            int offset = Integer.parseInt(parts[1]);
            return getAddress(index,table) + offset;
        }
        else if(expression.contains("-")){
            String[] parts = expression.split("-");
            int index = Integer.parseInt(parts[0]);
            int offset = Integer.parseInt(parts[1]);
            return getAddress(index,table) - offset;
        }
        else {
            int index = Integer.parseInt(expression);
            return getAddress(index,table);
        }
    }

    private int getAddress(int index, Map<String,Integer> table){
        int i=1;
        for(String key : table.keySet()){
            if(i==index)
                return table.get(key);
            i++;
        }
        return 0;
    }


    public void passTwo() throws Exception {
        if(intermediateCodeFile == null)
            throw new FileNotFoundException("intermediate code file not found");
        loadTables();

        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(intermediateCodeFile));

        while ((line=bufferedReader.readLine()) != null){
            line = line.replace("(31)","(RG,01)");
            line = line.replace("(32)","(RG,02)");
            line = line.replace("(33)","(RG,03)");
            line = line.replace("(34)","(RG,04)");
            line = line.replace("(","");
            line = line.replace(")","");

            interpret(line);
        }
        bufferedReader.close();
        generateOutput();
    }

    private void interpret(String line){
        String[] tokens = line.split("\\s");
        int index = opCode.size();
        opCode.add(0);
        register.add(0);
        address.add(0);
        address.add(0);
        for(int i=0; i<tokens.length; i++){
            String key = tokens[i].split(",")[0];
            String value = tokens[i].split(",")[1];
            switch (key){
                case "IS":
                    if(!value.equals("00"))
                        opCode.set(index,Integer.parseInt(value));
                break;
                case "DL":
                    if(value.equals("01")) {
                        key = tokens[i + 1].split(",")[0];
                        value = tokens[i + 1].split(",")[1];
                        address.set(index, Integer.parseInt(value));
                    }
                break;
                case "L":
                    address.set(index,evaluateAddress(value,literalTable));
                break;
                case "S":
                    address.set(index,evaluateAddress(value,symbolTable));
                break;
                case "RG":
                    register.set(index,Integer.parseInt(value)+30);
                break;
            }
        }
        if(opCode.get(index)==0 && register.get(index)==0 && address.get(index)==0){
            opCode.remove(index);
            register.remove(index);
            address.remove(index);
        }
    }

    private void generateOutput() throws Exception{
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("OBJECT_CODE.txt"));
        for(int i=0;i<opCode.size();i++)
            bufferedWriter.write(String.format("(%02d)\t(%02d)\t(%03d)\n",opCode.get(i),register.get(i),address.get(i)));
        bufferedWriter.close();

    }
}
