package xyz.heptadecane;

import java.util.ArrayList;

public class FCFS extends Scheduler {
    private ArrayList<Process> processes;
    private int currentTime;

    public FCFS(ArrayList<Process> processes) {
        this.processes = processes;
        currentTime = 0;
    }

    @Override
    protected Process selectProcess(){
        Process selected = null;
        for(Process process:processes){
            if(!process.isCompleted() && currentTime>=process.getArrivalTime()){
                if(selected == null)
                    selected = process;
                else {
                    if (process.getArrivalTime() < selected.getArrivalTime())
                        selected = process;
                    else if (process.getArrivalTime() == selected.getArrivalTime())
                        if (process.getId() < selected.getId())
                            selected = process;
                }
            }
        }
        return selected;
    }

    @Override
    public void execute(){
        while(!isQueueEmpty()){
            Process process = selectProcess();
            if(process == null){
                moveTimer(1);
            } else {
                if(!process.isResponded())
                    process.setResponseTime(currentTime);
                moveTimer(process.getCpuTime());
                process.setCompletionTime(currentTime);
            }
        }
    }
}
