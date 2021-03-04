public class Main {
    public static void main(String[] args) {
        MacroProcessor macroProcessor = new MacroProcessor();
        macroProcessor.setFile("input.txt");
        try{
            macroProcessor.passOne();
        }catch (Exception e){
            System.out.println(e);
        }
    }
}
