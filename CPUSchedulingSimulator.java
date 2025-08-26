import java.util.Scanner;

public class CPUSchedulingSimulator {
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            // get scheduling algorithm choice
            int algorithmChoice = 0;
            boolean validAlgorithm = false;
            while (!validAlgorithm) {
                System.out.println("CPU Scheduling Simulator");
                System.out.println("Choose scheduling algorithm:");
                System.out.println("1. FCFS (First Come First Serve)");
                System.out.println("2. SJF (Shortest Job First)");
                System.out.print("Enter your choice (1 or 2): ");
                algorithmChoice = input.nextInt();

                // check if valid choice
                if (algorithmChoice != 1 && algorithmChoice != 2) {
                    System.out.println("Error: Please enter 1 for FCFS or 2 for SJF.");
                    validAlgorithm = false; // keep the flag false to continue loop
                } else {
                    validAlgorithm = true; // set flag to true to exit loop
                }
            }

            // get number of processes with validation loop using boolean flag
            int numProcesses = 0;
            boolean validProcessCount = false;
            while (!validProcessCount) {
                System.out.print("Enter the number of processes (3-10): ");
                numProcesses = input.nextInt();

                // check if valid number
                if (numProcesses < 3 || numProcesses > 10) {
                    System.out.println("Error: Number must be between 3 and 10. Please try again.");
                    validProcessCount = false; // keep the flag false to continue loop
                } else {
                    validProcessCount = true; // set flag to true to exit loop
                }
            }

            // arrays to store process data
            String[] processID = new String[numProcesses];
            int[] arrivalTime = new int[numProcesses];
            int[] burstTime = new int[numProcesses];
            int[] waitingTime = new int[numProcesses];
            int[] turnaroundTime = new int[numProcesses];
            int[] completionTime = new int[numProcesses];

            // get all process IDs with duplicate checking using boolean flag
            for (int i = 0; i < numProcesses; i++) {
                boolean validProcessID = false;
                while (!validProcessID) {
                    System.out.print("Enter process ID for Process " + (i + 1) + ": ");
                    processID[i] = input.next();

                    // check for duplicate process IDs (check against previous entries only)
                    boolean isDuplicate = false;
                    for (int j = 0; j < i; j++) {
                        if (processID[i].equals(processID[j])) {
                            System.out.println("Error: Duplicate Process ID found: " + processID[i] + ". Please enter a different ID.");
                            isDuplicate = true;
                            break; // exit the inner for loop
                        }
                    }
                    // set flag based on whether duplicate was found
                    validProcessID = !isDuplicate; // true if no duplicate, false otherwise
                }
            }

            // get all arrival times with validation loop using boolean flag
            for (int i = 0; i < numProcesses; i++) {
                boolean validArrivalTime = false;
                while (!validArrivalTime) {
                    System.out.print("Enter arrival time for " + processID[i] + ": ");
                    arrivalTime[i] = input.nextInt();

                    // check if arrival time is valid
                    if (arrivalTime[i] < 0) {
                        System.out.println("Error: Arrival time cannot be negative. Please try again.");
                        validArrivalTime = false; // keep flag false to continue loop
                    } else {
                        validArrivalTime = true; // set flag to true to exit loop
                    }
                }
            }

            // get all burst times with validation loop using boolean flag
            for (int i = 0; i < numProcesses; i++) {
                boolean validBurstTime = false;
                while (!validBurstTime) {
                    System.out.print("Enter burst time for " + processID[i] + ": ");
                    burstTime[i] = input.nextInt();

                    // check if burst time is valid
                    if (burstTime[i] <= 0) {
                        System.out.println("Error: Burst time must be greater than 0. Please try again.");
                        validBurstTime = false; // keep flag false to continue loop
                    } else {
                        validBurstTime = true; // set flag to true to exit loop
                    }
                }
            }

            // arrays to store execution order
            String[] executionOrder = new String[numProcesses];
            int[] executionAT = new int[numProcesses];
            int[] executionBT = new int[numProcesses];
            int[] executionWT = new int[numProcesses];
            int[] executionTAT = new int[numProcesses];

            if (algorithmChoice == 1) {
                // FCFS: sort by arrival time (bubble sort)
                for (int i = 0; i < numProcesses - 1; i++) {
                    for (int j = 0; j < numProcesses - i - 1; j++) {
                        if (arrivalTime[j] > arrivalTime[j + 1]) {
                            // swap arrival times
                            int tempAT = arrivalTime[j];
                            arrivalTime[j] = arrivalTime[j + 1];
                            arrivalTime[j + 1] = tempAT;

                            // swap burst times
                            int tempBT = burstTime[j];
                            burstTime[j] = burstTime[j + 1];
                            burstTime[j + 1] = tempBT;

                            // swap process IDs
                            String tempID = processID[j];
                            processID[j] = processID[j + 1];
                            processID[j + 1] = tempID;
                        }
                    }
                }

                // FCFS calculation
                int currentTime = 0;
                for (int i = 0; i < numProcesses; i++) {
                    // if current time is less than arrival time, then set current time to arrival time
                    if (currentTime < arrivalTime[i]) {
                        currentTime = arrivalTime[i];
                    }

                    // calculate completion time
                    completionTime[i] = currentTime + burstTime[i];
                    currentTime = completionTime[i];

                    // calculate turnaround time
                    turnaroundTime[i] = completionTime[i] - arrivalTime[i];

                    // calculate waiting time
                    waitingTime[i] = turnaroundTime[i] - burstTime[i];

                    // store execution order
                    executionOrder[i] = processID[i];
                    executionAT[i] = arrivalTime[i];
                    executionBT[i] = burstTime[i];
                    executionWT[i] = waitingTime[i];
                    executionTAT[i] = turnaroundTime[i];
                }
            } else {
                // SJF calculation
                boolean[] completed = new boolean[numProcesses];
                int currentTime = 0;
                int completedCount = 0;
                int executionIndex = 0;

                while (completedCount < numProcesses) {
                    int shortestJob = -1;
                    int shortestBurst = Integer.MAX_VALUE;

                    // find the process with shortest burst time among available processes
                    for (int i = 0; i < numProcesses; i++) {
                        if (!completed[i] && arrivalTime[i] <= currentTime && burstTime[i] < shortestBurst) {
                            shortestBurst = burstTime[i];
                            shortestJob = i;
                        }
                    }

                    // if no process is available, move time forward to next arrival
                    if (shortestJob == -1) {
                        int nextArrival = Integer.MAX_VALUE;
                        for (int i = 0; i < numProcesses; i++) {
                            if (!completed[i] && arrivalTime[i] > currentTime && arrivalTime[i] < nextArrival) {
                                nextArrival = arrivalTime[i];
                            }
                        }
                        currentTime = nextArrival;
                    } else {
                        // execute the shortest job
                        completionTime[shortestJob] = currentTime + burstTime[shortestJob];
                        currentTime = completionTime[shortestJob];

                        // calculate turnaround time
                        turnaroundTime[shortestJob] = completionTime[shortestJob] - arrivalTime[shortestJob];

                        // calculate waiting time
                        waitingTime[shortestJob] = turnaroundTime[shortestJob] - burstTime[shortestJob];

                        // store execution order
                        executionOrder[executionIndex] = processID[shortestJob];
                        executionAT[executionIndex] = arrivalTime[shortestJob];
                        executionBT[executionIndex] = burstTime[shortestJob];
                        executionWT[executionIndex] = waitingTime[shortestJob];
                        executionTAT[executionIndex] = turnaroundTime[shortestJob];

                        completed[shortestJob] = true;
                        completedCount++;
                        executionIndex++;
                    }
                }
            }

            // calculate totals for averages
            int totalWT = 0;
            int totalTAT = 0;
            for (int i = 0; i < numProcesses; i++) {
                totalWT = totalWT + executionWT[i];
                totalTAT = totalTAT + executionTAT[i];
            }

            // calculate averages
            double avgWT = (double) totalWT / numProcesses;
            double avgTAT = (double) totalTAT / numProcesses;

            // display results

            
            if (algorithmChoice == 1) {
                System.out.println("\n" + "-".repeat(54));
                System.out.println("CPU SCHEDULING RESULTS - FCFS (First Come First Serve)");
                System.out.println("-".repeat(54) + "\n");
            } else {
               // System.out.println("\n" + "`".repeat(49));
                System.out.println("\n\n### CPU SCHEDULING RESULTS - SJF (Shortest Job First) ###\n\n");
               // System.out.println("`".repeat(49) + "\n");
            }

            // print table header
            System.out.println("-".repeat(40));
            System.out.println("PID\tAT\tBT\tWT\tTAT");
            System.out.println("-".repeat(40));

            // print each process in execution order
            for (int i = 0; i < numProcesses; i++) {
                System.out.println(executionOrder[i] + "\t" + executionAT[i] + "\t" + executionBT[i] + "\t" + executionWT[i] + "\t" + executionTAT[i]);
            }

            System.out.println("-".repeat(40));
            System.out.println("Average Waiting Time = " + String.format("%.1f", avgWT));
            System.out.println("Average Turnaround Time = " + String.format("%.1f", avgTAT));
        }
    }
}