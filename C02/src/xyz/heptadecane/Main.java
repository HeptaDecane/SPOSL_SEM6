package xyz.heptadecane;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int n, m;
        System.out.print("No. of Processes: ");
        n = scanner.nextInt();
        System.out.print("No. of Resources: ");
        m = scanner.nextInt();

        int[][] max = new int[n][m];
        int[][] allocated = new int[n][m];
        int[] available = new int[m];

        System.out.println("Allocation Matrix:");
        for(int i=0; i<n; i++)
            for(int j=0; j<m; j++)
                allocated[i][j] = scanner.nextInt();

        System.out.println("Max Matrix:");
        for(int i=0; i<n; i++)
            for(int j=0; j<m; j++)
                max[i][j] = scanner.nextInt();

        System.out.println("Available Matrix:");
        for(int i=0; i<m; i++)
            available[i] = scanner.nextInt();

        Bankers bankers = new Bankers(n, m, max, allocated, available);
        int[] executionSequence = bankers.getExecutionSequence();
        int[][] need = bankers.getNeed();
        ArrayList<int[]> work = bankers.getWork();

        System.out.println("\nNeed Matrix: ");
        for(int i=0; i<n; i++){
            for(int j=0; j<m; j++)
                System.out.print(need[i][j]+" ");
            System.out.println();
        }

        System.out.println("\nWork Matrix: ");
        for(int[] row : work){
            for(int i=0; i<m; i++)
                System.out.print(row[i]+" ");
            System.out.println();
        }

        int index = 0;
        System.out.println("\nExecution Sequence:");
        while (index < n){
            if(executionSequence[index] < 0) break;
            System.out.print("P" + (executionSequence[index] + 1) + "  ");
            index++;
        }
        if(n != index)
            System.out.println("\nDeadLock Detected.");
        else
            System.out.println("\nSafe Sequence.");

    }


}

/*
Test Cases:

No. of Processes: 4
No. of Resources: 3
Allocation Matrix:
1 0 1
2 1 2
3 0 0
1 0 1
Max Matrix:
2 1 1
5 4 4
3 1 1
1 1 1
Available Matrix:
9 2 5


No. of Processes: 5
No. of Resources: 4
Allocation Matrix:
0 1 1 0
1 2 3 1
1 3 6 5
0 6 3 2
0 0 1 4
Max Matrix:
0 2 1 0
1 6 5 2
2 3 6 6
0 6 5 2
0 6 5 6
Available Matrix:
3 17 16 12
 */
