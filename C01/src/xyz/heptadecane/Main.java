package xyz.heptadecane;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ArrayList<Process> processes = new ArrayList<>();
        processes.add(new Process(0,0,2));
        processes.add(new Process(1,1,2));
        processes.add(new Process(2,5,3));
        processes.add(new Process(3,6,4));

        Scheduler scheduler = new FCFS(processes);
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
