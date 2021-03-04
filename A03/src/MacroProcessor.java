import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MacroProcessor {
    private final ArrayList<String> defaultParameters;
    private final Map<String,String> argumentList;
    private final ArrayList<String> definitionTable;
    private final Map<String,Integer> nameTable;
    private Mode mode;
    private String code;
    private enum Mode {DECLARE,DEFINE,MEND,START}

    private String file;

    public MacroProcessor(){
        this(null);
    }

    public MacroProcessor(String file) {
        this.file = file;
        defaultParameters = new ArrayList<>();
        argumentList = new HashMap<>();
        definitionTable = new ArrayList<>();
        nameTable = new LinkedHashMap<>();
        mode = Mode.START;
        code = "";
    }

    public void setFile(String file){
        this.file = file;
    }

    public void passOne() throws Exception{
        if(file == null)
            throw new FileNotFoundException("no input file(s) set");

        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        while ((line=bufferedReader.readLine()) != null){
            if(line.equalsIgnoreCase("MACRO")) {
                mode = Mode.DECLARE;
                continue;
            }
            else if(line.equalsIgnoreCase("MEND"))
                mode = Mode.MEND;
            else if(line.contains("START"))
                mode = Mode.START;

            interpret(line);
        }
        bufferedReader.close();

        generateOutput();
    }

    private void interpret(String line){
        switch (mode){
            case DECLARE:
                definitionTable.add(line);
                String name = line.split("\\s+")[0];
                nameTable.put(name,definitionTable.size()-1);
                try{
                    processParameters(line);
                } catch (Exception ignored) {

                }
                mode = Mode.DEFINE;
            break;

            case DEFINE:
                for(String argument : argumentList.keySet())
                    line = line.replace(argument, argumentList.get(argument));
                definitionTable.add(line);
            break;

            case MEND:
                definitionTable.add(line);
                argumentList.clear();
            break;

            case START:
                code = code+line+"\n";
            break;
        }
    }

    private void processParameters(String line){
        String paramString = line.split("\\s+")[1];
        String []parameters = paramString.split(",\\s*");

        int position = 1;
        for(String parameter : parameters){
            if(parameter.contains("=")) {
                String defaultValue = parameter.split("=")[1];
                defaultParameters.add(nameTable.size()-1+"\t#"+position+"\t"+defaultValue);
            }
            parameter = parameter.split("=")[0];
            argumentList.put(parameter,"#"+position);
            position++;
        }

    }

    private void generateOutput() throws Exception{
        BufferedWriter bufferedWriter = null;
        int index;

        bufferedWriter = new BufferedWriter(new FileWriter("MNT.txt"));
        index = 0;
        for (String name : nameTable.keySet()) {
            bufferedWriter.write(index + "\t" + name + "\t" + nameTable.get(name) + "\n");
            index++;
        }
        bufferedWriter.close();

        bufferedWriter = new BufferedWriter(new FileWriter("DEFAULT_ARGUMENTS.txt"));
        for (String row : defaultParameters)
            bufferedWriter.write(row + "\n");
        bufferedWriter.close();

        bufferedWriter = new BufferedWriter(new FileWriter("MDT.txt"));
        index = 0;
        for (String definition : definitionTable) {
            bufferedWriter.write(index + "\t" + definition + "\n");
            index++;
        }
        bufferedWriter.close();

        bufferedWriter = new BufferedWriter(new FileWriter("CODE.txt"));
        bufferedWriter.write(code);
        bufferedWriter.close();
    }
}
