package xyz.heptadecane;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int mode = 0;
        System.out.println("Scheduling Modes: ");
        System.out.println("\t1. First Come First Serve");
        System.out.println("\t2. Highest Priority First");
        System.out.println("\t3. Shortest Remaining Time First");
        System.out.println("\t4. Round Robin");
        while(true){
            System.out.print("Mode: ");
            mode = Integer.parseInt(scanner.nextLine());
            if(mode==1 || mode==2 || mode==3 ||mode==4) break;
            else
                System.out.println("invalid index: "+mode);
        }

        int timeQuantum = 1;
        if(mode == 4){
            System.out.print("Time Quantum: ");
            timeQuantum = Integer.parseInt(scanner.nextLine());
        }

        int n;
        ArrayList<Process> processes = new ArrayList<>();
        System.out.print("No. of Process: ");
        n = Integer.parseInt(scanner.nextLine());

        int arrivalTime, cpuTime, priority = 0;
        for(int i=1; i<=n; i++){
            System.out.println("\nP"+i);
            System.out.print("\tArrival Time: ");
            arrivalTime = Integer.parseInt(scanner.nextLine());
            System.out.print("\tCPU Time: ");
            cpuTime = Integer.parseInt(scanner.nextLine());
            if(mode == 2){
                System.out.print("\tPriority: ");
                priority = Integer.parseInt(scanner.nextLine());
            }
            processes.add(new Process(i, arrivalTime, cpuTime, priority));
        }

        Scheduler scheduler = null;
        switch (mode){
            case 1:
                scheduler = new FCFS(processes);
                break;
            case 2:
                scheduler = new Priority(processes);
                break;
            case 3:
                scheduler = new SRTF(processes);
                break;
            case 4:
                scheduler = new RoundRobin(processes, timeQuantum);
                break;
        }

        String chart = scheduler.execute();
        System.out.println("\nChart:\n"+chart);

        double avgTurnAboutTime = 0, avgWaitingTime = 0, avgResponseTime = 0;
        System.out.println("ID\tAT\tBT\tCT\tTT\tWT\tRT");
        for(Process process : processes) {
            System.out.print(process.getId() + "\t");
            System.out.print(process.getArrivalTime() + "\t");
            System.out.print(process.getCpuTime() + "\t");
            System.out.print(process.getCompletionTime() + "\t");
            System.out.print(process.getCompletionTime()-process.getArrivalTime() + "\t");
            System.out.print(process.getCompletionTime()-process.getArrivalTime() - process.getCpuTime() + "\t");
            System.out.print(process.getResponseTime()-process.getArrivalTime() + "\n");

            avgTurnAboutTime += process.getCompletionTime()-process.getArrivalTime();
            avgWaitingTime += process.getCompletionTime()-process.getArrivalTime() - process.getCpuTime();
            avgResponseTime += process.getResponseTime()-process.getArrivalTime();
        }
        avgTurnAboutTime = avgTurnAboutTime/processes.size();
        avgWaitingTime = avgWaitingTime/processes.size();
        avgResponseTime = avgResponseTime/processes.size();
        System.out.println("Average Turn About Time: "+avgTurnAboutTime);
        System.out.println("Average Waiting Time: "+avgWaitingTime);
        System.out.println("Average Response Time: "+avgResponseTime);
    }
}