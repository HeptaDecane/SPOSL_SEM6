package xyz.heptadecane;

public class Process {
    private int id;
    private int arrivalTime;
    private int cpuTime;
    private int responseTime;
    private int completionTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getCpuTime() {
        return cpuTime;
    }

    public void setCpuTime(int cpuTime) {
        this.cpuTime = cpuTime;
    }

    public int getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(int responseTime) {
        this.responseTime = responseTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public Process(int id, int arrivalTime, int cpuTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.cpuTime = cpuTime;
        this.responseTime = -1;
        this.completionTime = -1;
    }

    public boolean isCompleted(){
        return this.completionTime != -1;
    }

    public boolean isResponded(){
        return this.responseTime != -1;
    }
}
