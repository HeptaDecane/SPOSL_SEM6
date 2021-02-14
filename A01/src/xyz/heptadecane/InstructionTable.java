package xyz.heptadecane;

import java.util.HashMap;

public class InstructionTable {
    public static HashMap<String, Integer> AD, RG, IS, CC, DL;

    static {
        AD = new HashMap<>();
        RG = new HashMap<>();
        IS = new HashMap<>();
        CC = new HashMap<>();
        DL = new HashMap<>();

        DL.put("DC", 1);
        DL.put("DS", 2);

        IS.put("STOP",0);
        IS.put("ADD",1);
        IS.put("SUB",2);
        IS.put("MULT",3);
        IS.put("MOVER",4);
        IS.put("MOVEM",5);
        IS.put("COMP",6);
        IS.put("BC",7);
        IS.put("DIV",8);
        IS.put("READ",9);
        IS.put("PRINT",10);

        CC.put("LT",1);
        CC.put("LE",2);
        CC.put("EQ",3);
        CC.put("GT",4);
        CC.put("GE",5);
        CC.put("ANY",6);

        AD.put("START",1);
        AD.put("END",2);
        AD.put("ORIGIN",3);
        AD.put("EQU",4);
        AD.put("LTORG",5);

        RG.put("AREG",1);
        RG.put("BREG",2);
        RG.put("CREG",3);
        RG.put("DREG",4);
    }

    public static String getInstructionType(String instruction) {
        instruction = instruction.toUpperCase();
        if(AD.containsKey(instruction)) return "AD";
        if(RG.containsKey(instruction)) return "RG";
        if(IS.containsKey(instruction)) return "IS";
        if(CC.containsKey(instruction)) return "CC";
        if(DL.containsKey(instruction))return "DL";
        return "NULL";
    }

    public static int getOpCode(String instruction) {
        instruction = instruction.toUpperCase();
        if(AD.containsKey(instruction)) return AD.get(instruction);
        if(RG.containsKey(instruction)) return RG.get(instruction);
        if(IS.containsKey(instruction)) return IS.get(instruction);
        if(CC.containsKey(instruction)) return CC.get(instruction);
        if(DL.containsKey(instruction)) return DL.get(instruction);
        return -1;
    }

}
