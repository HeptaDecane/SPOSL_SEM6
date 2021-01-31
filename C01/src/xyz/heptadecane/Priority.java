package xyz.heptadecane;

import java.util.ArrayList;

public class Priority extends Scheduler{

    public Priority(ArrayList<Process> processes) {
        super(processes);
    }

    @Override
    protected Process selectProcess() {
        Process selected = null;
        for(Process process : processes){
            if(!process.isCompleted() && currentTime>=process.getArrivalTime()){
                if(selected == null)
                    selected = process;
                else {
                    if(process.getPriority() < selected.getPriority())
                        selected = process;
                    else if(process.getPriority() == selected.getPriority())
                        if(process.getArrivalTime() < selected.getArrivalTime())
                            selected = process;
                        else if(process.getArrivalTime() == selected.getArrivalTime())
                            if(process.getId() < selected.getId())
                                selected = process;
                }
            }
        }
        return selected;
    }

    @Override
    public void execute() {
        while (!isQueueEmpty()){
            Process process = selectProcess();
            if(process == null)
                moveTimer(1);
            else {
                if(!process.isResponded())
                    process.setResponseTime(currentTime);
                moveTimer(process.getCpuTime());
                process.setCompletionTime(currentTime);
            }
        }
    }
}
