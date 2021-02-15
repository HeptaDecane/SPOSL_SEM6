package xyz.heptadecane;

public class Main {
    public static void main(String[] args) {

        Assembler assembler = new Assembler("input.asm");
        try {
            assembler.passOne();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
