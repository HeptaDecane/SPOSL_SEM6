package xyz.heptadecane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Assembler {
    private final Map<String, Integer> symbolTable;
    private final Map<String, Integer> literalTable;
    private final ArrayList<Integer> poolTable;
    private String file;
    private String code;
    private int locationCounter;
    private int poolPointer;

    public Assembler(){
        this(null);
    }

    public Assembler(String file) {
        this.file = file;
        symbolTable = new LinkedHashMap<>();
        literalTable = new LinkedHashMap<>();
        poolTable = new ArrayList<>();
        poolPointer = 0;
        code = "";
    }

    public void setFile(String file) {
        this.file = file;
    }

    private void initializeLocationCounter() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        String[] tokens = line.split("\\s+");
        locationCounter = Integer.parseInt(tokens[2]);
        reader.close();
    }

    private void interpret(String line) throws Exception {
        String[] tokens = line.split("\\s+");
        String label = tokens[0];
        String instruction = tokens[1].toUpperCase();
        String instructionType = InstructionTable.getInstructionType(instruction);

        if(!label.isBlank()){
            symbolTable.put(label,locationCounter);
        }

        switch (instructionType){
            case "AD":
                if(instruction.equals("START")){
                    code = code + String.format("(AD,01)\t(C,%s)\n",locationCounter);
                }
                else if(instruction.equals("END")){
                    poolTable.add(poolPointer+1);
                    updateLiteralTable(true);
                    code = code + "(AD,02)\n";
                }
                else if(instruction.equals("ORIGIN")){
                    String expression = tokens[2];
                    if(expression.contains("+")){
                        String[] parts = expression.split("\\+");
                        code = code + String.format("(AD,03)\t(S,%02d)+%s\n",getTableIndex(parts[0],symbolTable),parts[1]);
                    }
                    else if(expression.contains("-")){
                        String[] parts = expression.split("-");
                        code = code + String.format("(AD,03)\t(S,%02d)-%s\n",getTableIndex(parts[0],symbolTable),parts[1]);
                    }
                    else {
                        try{
                            Integer.parseInt(expression);
                            code = code + String.format("(AD,03)\t(C,%s)\n",expression);
                        } catch (NumberFormatException e){
                            code = code + String.format("(AD,03)\t(S,%02d)\n",getTableIndex(expression,symbolTable));
                        }
                    }
                    locationCounter = evaluate(expression);
                }
                else if(instruction.equals("EQU")){
                    String expression = tokens[2];
                    if(expression.contains("+")){
                        String[] parts = expression.split("\\+");
                        code = code + String.format("(AD,04)\t(S,%02d)+%s\n",getTableIndex(parts[0],symbolTable),parts[1]);
                    }
                    else if(expression.contains("-")){
                        String[] parts = expression.split("-");
                        code = code + String.format("(AD,04)\t(S,%02d)-%s\n",getTableIndex(parts[0],symbolTable),parts[1]);
                    }
                    else {
                        try{
                            Integer.parseInt(expression);
                            code = code + String.format("(AD,04)\t(C,%s)\n",expression);
                        } catch (NumberFormatException e){
                            code = code + String.format("(AD,04)\t(S,%02d)\n",getTableIndex(expression,symbolTable));
                        }
                    }
                    symbolTable.put(label,evaluate(expression));
                }
                else if(instruction.equals("LTORG")){
                    poolTable.add(poolPointer+1);
                    updateLiteralTable(false);
                }
            break;
            case "DL":
                code = code + String.format("(DL,%02d)\t",InstructionTable.getOpCode(instruction));
                if(instruction.equals("DC")){
                    int constant = Integer.parseInt(tokens[2].replace("'",""));
                    code = code + String.format("(C,%s)\n",constant);
                    locationCounter++;
                }
                else if(instruction.equals("DS")){
                    int size = Integer.parseInt(tokens[2]);
                    code = code + String.format("(C,%s)\n",size);
                    locationCounter = locationCounter+size;
                }
            break;
            case "IS":
                code = code + String.format("(IS,%02d)\t",InstructionTable.getOpCode(instruction));
                for(int i=2; i<tokens.length; i++){
                    tokens[i] = tokens[i].replace("'","").replace(",","");
                    if(InstructionTable.getInstructionType(tokens[i]).equals("RG")){
                        code = code + String.format("(RG,%02d)\t",InstructionTable.getOpCode(tokens[i]));
                    }else if(InstructionTable.getInstructionType(tokens[i]).equals("CC")){
                        code = code + String.format("(CC,%02d)\t",InstructionTable.getOpCode(tokens[i]));
                    } else {
                        if(tokens[i].contains("=")){
                            tokens[i] = tokens[i].replace("=","");
                            literalTable.put(tokens[i],-1);
                            code = code + String.format("(L,%02d)\t",getTableIndex(tokens[i],literalTable));
                        }
                        else if(symbolTable.containsKey(tokens[i])){
                            code = code + String.format("(S,%02d)\t",getTableIndex(tokens[i],symbolTable));
                        }
                        else {
                            symbolTable.put(tokens[i],-1);
                            code = code + String.format("(S,%02d)\t",getTableIndex(tokens[i],symbolTable));
                        }
                    }
                }
                code = code + "\n";
                locationCounter++;
            break;
            default:
                throw new Exception("Invalid Instruction Type");
        }
    }

    public void passOne() throws Exception {
        initializeLocationCounter();

        String line;
        BufferedReader reader = new BufferedReader(new FileReader(file));

        while ((line=reader.readLine())!=null){
            interpret(line);
        }
        reader.close();
        System.out.println(code);
        for (String literal: literalTable.keySet())
            System.out.println(literal+", "+literalTable.get(literal));
        System.out.println();
        for (String literal: symbolTable.keySet())
            System.out.println(literal+", "+symbolTable.get(literal));
        System.out.println();
        for(int i: poolTable)
            System.out.println(i);
    }

    void updateLiteralTable(boolean end){
        int index = 0;
        for(String literal : literalTable.keySet()){
            if(poolPointer == index){
                literalTable.put(literal, locationCounter);
                if(!end)
                    code = code + String.format("(AD,05)\t(DL,02)\t(C,%s)\n",literal);
                else
                    code = code + String.format("(DL,02)\t(C,%s)\n",literal);
                poolPointer++;
                locationCounter++;
            }
            index++;
        }
    }

    int evaluate(String expression){
        if(expression.contains("+")){
            String[] tokens = expression.split("\\+");
            return symbolTable.get(tokens[0]) + Integer.parseInt(tokens[1]);
        }
        else if(expression.contains("-")){
            String[] tokens = expression.split("-");
            return symbolTable.get(tokens[0]) - Integer.parseInt(tokens[1]);
        }
        else {
            try{
                return Integer.parseInt(expression);
            } catch (NumberFormatException e){
                return symbolTable.get(expression);
            }
        }
    }

    int getTableIndex(String entry, Map<String, Integer> table){
        int index = 0;
        for(String key : table.keySet()){
            if(key.equals(entry))
                return index+1;
            index++;
        }
        return -1;
    }

}
