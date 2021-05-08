package xyz.heptadecane;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Pass2 {
   public static ArrayList<String> mdt;
   public  static HashMap<String,String> mnt,actualParams,formalParams;


public  static void main(String[] args){

    try {
        String[][] output_pass2=new String[100][4];
        int output_count=0;
        File myObj = new File("MNT.txt");
        Scanner sc1 = new Scanner(myObj);
        mnt=new HashMap<>();
        while(sc1.hasNextLine()){
            String s=sc1.nextLine();
            String[] tokens=s.split("\t");
            mnt.put(tokens[1],tokens[2]);
        }
        File myObj1 = new File("MDT.txt");
        Scanner sc2 = new Scanner(myObj1);
       mdt=new ArrayList<>();
        while(sc2.hasNextLine()){
            String s=sc2.nextLine();
            String[] tokens=s.split("\t");
            int index=Integer.parseInt(tokens[0]);
            if(tokens.length>2){
               // System.out.println(tokens[1]+" "+tokens[2]);
                mdt.add(index,tokens[1]+"\t"+tokens[2]);
            }else{
               // System.out.println(tokens[1]);
                mdt.add(index,tokens[1]);
            }
        }
        File myObj2=new File("Output.txt");
        Scanner sc3=new Scanner(myObj2);
        while (sc3.hasNextLine()){
            String s=sc3.nextLine();
            String[] tokens=s.split("\t");
            //if its a macro call
            if(mnt.containsKey(tokens[0])){
                //create actual parameters map
                actualParams=new HashMap<>();
                formalParams=new HashMap<>();
                String[] actualP=tokens[1].split(",");
                for(int i=1;i<=actualP.length;i++){
                    if(actualP[i-1].contains("=")){
                        String[] ans=actualP[i-1].split("=");
                        actualParams.put("#"+String.valueOf(i),ans[1]);

                    }else {
                        actualParams.put("#" + String.valueOf(i), actualP[i-1]);
                    }
                   // System.out.println(actualParams.get("#"+String.valueOf(i)));
                }
                int mdt_ptr=Integer.parseInt(mnt.get(tokens[0]));
                while(true){
                    if(mdt.get(mdt_ptr).contains("MEND")){
                        actualParams.clear();
                        formalParams.clear();
                        output_count--;
                        break;
                    }else{

                        if(mdt.get(mdt_ptr).contains(tokens[0])){
                            //create formal parameters
                            String exp=mdt.get(mdt_ptr);
                            String[] split_words=exp.split("\t");
                            if(split_words.length>1){
                                String[] params=split_words[1].split(",");
                                for(int i=1;i<=params.length;i++){
                                    if(params[i-1].contains("=")){
                                        String[] ans=params[i-1].split("=");
                                        formalParams.put("#"+String.valueOf(i),ans[1]);
                                    }else {
                                        formalParams.put("#" + String.valueOf(i),params[i-1]);
                                    }
                                }

                            }
                        }else {
                            String exp = mdt.get(mdt_ptr);
                            String[] split_words = exp.split("\t");
                            output_pass2[output_count][0] = "+"+split_words[0];
                            if (split_words.length > 1) {
                                String[] params = split_words[1].split(",");
                                String a = "";
                                for (int i = 0; i < params.length; i++) {
                                    if (actualParams.containsKey(params[i])) {
                                        a += actualParams.get(params[i]);
                                    } else {
                                        a += formalParams.get(params[i]);
                                    }
                                    if (i != params.length - 1) {
                                        a += ",";
                                    }
                                }
                                output_pass2[output_count][1] = a;
                            }
                            output_count++;
                        }

                    }
                    mdt_ptr++;

                }
            }else{
                for(int i=0;i<tokens.length;i++){
                    output_pass2[output_count][i]=tokens[i];
                }
            }
            output_count++;
        }
        FileWriter myWriter = new FileWriter("Output_pass2.txt");
        for(int i=0;output_pass2[i][0]!=null;i++){
            for(int j=0;output_pass2[i][j]!=null;j++){
                System.out.print(output_pass2[i][j]+"\t");
                myWriter.write(output_pass2[i][j]+"\t");
            }
            System.out.println();
            myWriter.write("\n");
        }

    myWriter.close();

    }catch(Exception e){
        e.printStackTrace();
    }
}
}
