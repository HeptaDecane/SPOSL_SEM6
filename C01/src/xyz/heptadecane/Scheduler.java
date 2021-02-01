package xyz.heptadecane;

import java.util.ArrayList;

public abstract class Scheduler {
    protected ArrayList<Process> processes;
    protected int currentTime;

    public Scheduler(ArrayList<Process> processes) {
        this.processes = processes;
        this.currentTime = 0;
    }

    protected void moveTimer(int clicks){
        currentTime = currentTime + clicks;
    }

    protected boolean isQueueEmpty(){
        boolean result = true;
        for(Process process : processes)
            result = result && process.isCompleted();
        return result;
    }

    protected abstract Process selectProcess();
    public abstract String execute();
}
