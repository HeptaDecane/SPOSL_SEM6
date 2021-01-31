package xyz.heptadecane;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(1,0,5));
        processes.add(new Process(2,1,4));
        processes.add(new Process(3,2,2));
        processes.add(new Process(4,4,1));

        Scheduler scheduler = new RoundRobin(processes,2);
        scheduler.execute();

        System.out.println("ID\tAT\tBT\tCT\tTT\tWT\tRT");
        for(Process process : processes) {
            System.out.print(process.getId() + "\t");
            System.out.print(process.getArrivalTime() + "\t");
            System.out.print(process.getCpuTime() + "\t");
            System.out.print(process.getCompletionTime() + "\t");
            System.out.print(process.getCompletionTime()-process.getArrivalTime() + "\t");
            System.out.print(process.getCompletionTime()-process.getArrivalTime() - process.getCpuTime() + "\t");
            System.out.print(process.getResponseTime()-process.getArrivalTime() + "\n");
        }
    }
}
