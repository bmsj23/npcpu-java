import java.util.Scanner;

public class FCFSScheduling {
    
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
        
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
        
            // sort processes by arrival time (bubble sort since fcfs, so ascending order is fine)
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
            
            // calculate completion times
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
            }
            
            // calculate totals for averages
            int totalWT = 0;
            int totalTAT = 0;
            
            for (int i = 0; i < numProcesses; i++) {
                totalWT = totalWT + waitingTime[i];
                totalTAT = totalTAT + turnaroundTime[i];
            }
            
            // calculate averages
            double avgWT = (double) totalWT / numProcesses;
            double avgTAT = (double) totalTAT / numProcesses;
            
            // display results
            System.out.println("\n" + "=".repeat(60));
            System.out.println("CPU SCHEDULING RESULTS");
            System.out.println("=".repeat(60));
            
            // print table header
            System.out.println("PID\tAT\tBT\tWT\tTAT");
            System.out.println("-".repeat(40));
            
            // print each process
            for (int i = 0; i < numProcesses; i++) {
                System.out.println(processID[i] + "\t" + arrivalTime[i] + "\t" + burstTime[i] + "\t" + waitingTime[i] + "\t" + turnaroundTime[i]);
            }
            
            System.out.println("-".repeat(40));
            System.out.println("Average Waiting Time = " + String.format("%.1f", avgWT));
            System.out.println("Average Turnaround Time = " + String.format("%.1f", avgTAT));
            System.out.println("=".repeat(60));
        
        }
    }
}