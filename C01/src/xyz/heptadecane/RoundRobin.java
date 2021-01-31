package xyz.heptadecane;

import java.util.*;

public class RoundRobin extends Scheduler{
    private Map<Integer, Integer> remainingTimeMap;
    private Process context;
    private Queue<Process> readyQueue;
    private final int timeQuantum;

    public RoundRobin(ArrayList<Process> processes, int timeQuantum) {
        super(processes);
        this.timeQuantum = timeQuantum;
        this.context = null;
        this.remainingTimeMap = new HashMap<>();
        this.readyQueue = new LinkedList<>();
        for(Process process : processes)
            remainingTimeMap.put(process.getId(), process.getCpuTime());
    }

    @Override
    protected Process selectProcess() {
        Process selected = null;
        ArrayList<Process> arrivedProcesses = new ArrayList<>();
        for(Process process : processes){
            if(!process.isCompleted() && !readyQueue.contains(process) && !process.equals(context))
                if(currentTime >= process.getArrivalTime())
                    arrivedProcesses.add(process);
        }
        arrivedProcesses.sort(new ProcessArrivalComparator());
        readyQueue.addAll(arrivedProcesses);
        if(!readyQueue.isEmpty())
            selected = readyQueue.remove();
        return selected;
    }

    @Override
    public void execute() {
        while (!isQueueEmpty()){
            Process process = selectProcess();
            if(context != null)
                readyQueue.add(context);

            if(process == null)
                moveTimer(1);
            else {
                if(!process.isResponded())
                    process.setResponseTime(currentTime);
                int remainingTime = remainingTimeMap.get(process.getId());
                int clicks = Math.min(remainingTime, timeQuantum);
                remainingTime = remainingTime - clicks;
                remainingTimeMap.put(process.getId(), remainingTime);
                moveTimer(clicks);

                if(remainingTimeMap.get(process.getId()) == 0) {
                    process.setCompletionTime(currentTime);
                    context = null;
                }
                else
                    context = process;
            }

        }

    }

     static class ProcessArrivalComparator implements Comparator<Process>{

        @Override
        public int compare(Process p1, Process p2) {
            if(p1.getArrivalTime() == p2.getArrivalTime())
                return p1.getId() - p2.getId();
            else
                return p1.getArrivalTime() - p2.getArrivalTime();
        }
    }
}
