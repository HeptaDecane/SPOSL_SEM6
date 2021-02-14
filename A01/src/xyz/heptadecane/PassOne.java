package xyz.heptadecane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class PassOne {
    private final Map<String, Integer> symbolTable;
    private final Map<String, Integer> literalTable;
    private final ArrayList<Integer> poolTable;
    private final String file;
    private String code;
    private int locationCounter;
    private int poolPointer;

    public PassOne(String file) {
        this.file = file;
        symbolTable = new LinkedHashMap<>();
        literalTable = new LinkedHashMap<>();
        poolTable = new ArrayList<>();
        poolPointer = 0;
        code = "";
    }

    private void initializeLocationCounter() throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        String[] tokens = line.split("\\s+");
        locationCounter = Integer.parseInt(tokens[2]);
        reader.close();
    }

    public void parseFile() throws Exception {
        initializeLocationCounter();
        String line;
        BufferedReader reader = new BufferedReader(new FileReader(file));

        while ((line=reader.readLine())!=null){
            String[] tokens = line.split("\\s+");
            if(!tokens[0].isBlank()){
                String label = tokens[0];
                symbolTable.put(label,locationCounter);
            }

            if(tokens[1].equalsIgnoreCase("LTORG")){
                poolTable.add(poolPointer);
                updateLiteralTable(false);
            }

            if(tokens[1].equalsIgnoreCase("START")){
                code = code + String.format("(AD,01)\t(C,%s)\n",locationCounter);
            }

            if(tokens[1].equalsIgnoreCase("ORIGIN")){
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

            if(tokens[1].equalsIgnoreCase("EQU")){
                String label = tokens[0];
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

            if(tokens[1].equalsIgnoreCase("DC")){
                int constant = Integer.parseInt(tokens[2].replace("'",""));
                code = code + String.format("(DL,02)\t(C,%s)\n",constant);
                locationCounter++;
            }

            if(tokens[1].equalsIgnoreCase("DS")){
                int size = Integer.parseInt(tokens[2]);
                code = code + String.format("(DL,01)\t(C,%s)\n",size);
                locationCounter = locationCounter+size;
            }

            if(InstructionTable.getInstructionType(tokens[1]).equals("IS")){
                code = code + String.format("(IS,%02d)\t",InstructionTable.getOpCode(tokens[1]));
                for(int i=2; i<tokens.length; i++){
                    tokens[i] = tokens[i].replace("'","").replace(",","");
                    if(InstructionTable.getInstructionType(tokens[i]).equals("RG")){
                        code = code + String.format("(RG,%02d)\t",InstructionTable.getOpCode(tokens[i]));
                    } else {
                        if(tokens[i].contains("=")){
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
            }

            if(tokens[1].equals("END")){
                poolTable.add(poolPointer);
                updateLiteralTable(true);
                code = code + "(AD,02)\n";
            }

        }
        reader.close();
        System.out.println(code);
//        for (String literal: literalTable.keySet())
//            System.out.println(literal+", "+literalTable.get(literal));

        for (String literal: symbolTable.keySet())
            System.out.println(literal+", "+symbolTable.get(literal));
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
                return index;
            index++;
        }
        return -1;
    }

}
