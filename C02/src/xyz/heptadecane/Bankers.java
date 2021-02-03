package xyz.heptadecane;

public class Bankers {
    private final int n, m;
    private final int[][] max;
    private final int[][] allocated;
    private final int[] available;
    private final int[][] need;
    private final int[] executionSequence;

    public Bankers(int n, int m, int[][] max, int[][] allocated, int[] available) {
        this.n = n;
        this.m = m;
        this.max = max;
        this.allocated = allocated;
        this.available = available;
        this.need = new int[n][m];
        this.executionSequence = new int[n];

        for(int i=0; i<n; i++)
            for(int j=0; j<m; j++)
                need[i][j] = max[i][j] - allocated[i][j];
    }

    public int[] getExecutionSequence() {
        boolean[] completed = new boolean[n];
        for(int i=0; i<n; i++)
            completed[i] = false;

        for(int count=0; count<n; ){
            boolean deadLock = true;

            for(int i=0; i<n; i++){
                if(completed[i]) continue;
                boolean sufficientResource = true;
                for(int j=0; j<m; j++)
                    sufficientResource = sufficientResource && (available[j] >= need[i][j]);

                if(sufficientResource){
                    deadLock = false;
                    executionSequence[count] = i;
                    completed[i] = true;

                    for(int j=0; j<m; j++)
                        available[j] = available[j] + allocated[i][j];

                    count++;
                }
            }
            if(deadLock)
                return null;
        }
        return executionSequence;
    }
}
