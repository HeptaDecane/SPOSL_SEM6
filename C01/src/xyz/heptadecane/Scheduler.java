package xyz.heptadecane;

import java.util.ArrayList;

public abstract class Scheduler {
    private ArrayList<Process> processes;
    private int currentTime;

    protected void moveTimer(int time){
        currentTime = currentTime + time;
    }

    protected boolean isQueueEmpty(){
        boolean result = true;
        for(Process process : processes)
            result = result && process.isCompleted();
        return result;
    }

    protected abstract Process selectProcess();
    public abstract void execute();
}
