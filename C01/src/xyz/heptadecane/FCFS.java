package xyz.heptadecane;

import java.util.ArrayList;

public class FCFS extends Scheduler {

    public FCFS(ArrayList<Process> processes) {
        super(processes);
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
    public String execute(){
        String chart = "";
        while(!isQueueEmpty()){
            chart += currentTime+" ";
            Process process = selectProcess();
            if(process == null) {
                chart += "[ ] ";
                moveTimer(1);
            }
            else {
                chart += "[P"+process.getId()+"] ";
                if(!process.isResponded())
                    process.setResponseTime(currentTime);
                moveTimer(process.getCpuTime());
                process.setCompletionTime(currentTime);
            }
        }
        chart += currentTime+"\n";
        return chart;
    }
}
