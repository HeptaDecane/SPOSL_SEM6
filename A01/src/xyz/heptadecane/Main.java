package xyz.heptadecane;

public class Main {
    public static void main(String[] args) {

        PassOne passOne = new PassOne("input.asm");
        try {
            passOne.parseFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
