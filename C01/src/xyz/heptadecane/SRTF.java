package xyz.heptadecane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SRTF extends Scheduler {
    private Map<Integer, Integer> timeMap;

    public SRTF(ArrayList<Process> processes) {
        super(processes);
        timeMap = new HashMap<>();
        for(Process process : processes)
            timeMap.put(process.getId(), process.getCpuTime());
    }

    @Override
    protected Process selectProcess() {
        Process selected = null;
        for(Process process : processes){
            if(!process.isCompleted() && currentTime>=process.getArrivalTime()){
                if(selected == null)
                    selected = process;
                else {
                    if(timeMap.get(process.getId()) < timeMap.get(selected.getId()))
                        selected = process;
                    else if(timeMap.get(process.getId()) == timeMap.get(selected.getId()))
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
    public String execute() {
        String chart = "";
        while (!isQueueEmpty()){
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
                int remainingTime = timeMap.get(process.getId()) - 1;
                timeMap.put(process.getId(), remainingTime);
                moveTimer(1);
                if(timeMap.get(process.getId()) == 0)
                    process.setCompletionTime(currentTime);
            }
        }
        chart += currentTime+"\n";
        return chart;
    }
}
