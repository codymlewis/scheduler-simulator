import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.io.File;

/**
 * <h1>Dispatcher - Comp2240A1</h1>
 *
 * A class for the Dispatcher, it manages the process execution simulation and
 * is composed of a scheduler.
 *
 * @author Cody Lewis
 * @version 1
 * @since 2019-08-17
 */

public class Dispatcher {
    private Scheduler scheduler;
    private int switchProcessTime;
    private Map<Integer, LinkedList<Process>> processes;

    /**
     * Default constructor
     */
    public Dispatcher() {
    }

    /**
     * Input constructor
     *
     * @param schedulerAlgorithm Name of the scheduler algorithm to use
     * @param filename Name of the data file
     */
    public Dispatcher(String schedulerAlgorithm, String filename) {
        this();
        processes = parseDataFile(filename);
        scheduler = schedulerFactory(schedulerAlgorithm);
    }

    /**
     * Read the data file and construct a map between the time of the process
     * and the list of processes.
     *
     * @param filename Name of the file to read
     * @return Map between time of process and the list of processes
     */
    private Map<Integer, LinkedList<Process>> parseDataFile(String filename) {
        Map<Integer, LinkedList<Process>> data = new HashMap<>();

        try (Scanner fstream = new Scanner(new File(filename))) {
            String curLine = fstream.nextLine();
            if (!curLine.contains("BEGIN")) {
                throw new InvalidDataException();
            }
            fstream.nextLine();
            curLine = fstream.nextLine();
            Scanner sstream = new Scanner(curLine);
            if (!sstream.next().contains("DISP:")) {
                throw new InvalidDataException();
            }
            switchProcessTime = sstream.nextInt();
            if (!fstream.nextLine().contains("END")) {
                throw new InvalidDataException();
            }
            String pid = "";
            int arrivalTime = 0;
            int serviceTime = 0;
            while (!(curLine = fstream.nextLine()).contains("EOF")) {
                if (curLine.length() > 1) {
                    sstream = new Scanner(curLine);
                    switch (sstream.next()) {
                        case "ID:":
                            pid = sstream.next();
                            break;
                        case "Arrive:":
                            arrivalTime = sstream.nextInt();
                            break;
                        case "ExecSize:":
                            serviceTime = sstream.nextInt();
                            break;
                        case "END":
                            if (data.get(arrivalTime) == null) {
                                data.put(
                                    arrivalTime,
                                    new LinkedList<Process>()
                                );
                            }
                            data.get(arrivalTime).add(
                                new Process(pid, arrivalTime, serviceTime)
                            );
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Factory method to choose the appropriate scheduler to compose this
     *
     * @param algorithm Name of the algorithm
     * @return A scheduler running the chosen algorithm
     */
    private Scheduler schedulerFactory(String algorithm) {
        switch (algorithm) {
            case "RR":
                return new RR(switchProcessTime);
            case "NRR":
                return new NRR(switchProcessTime);
            case "FB":
                return new FB(switchProcessTime);
            default:
                return new FCFS(switchProcessTime);
        }
    }

    /**
     * Run through the scheduled processes
     *
     * @return Results of the simulation
     */
    public String run() {
        int time = 0;
        int timeProcessingTo = 0;

        while (processes.size() != 0 || !scheduler.empty()) {
            if (processes.get(time) != null) {
                for (Process process : processes.get(time)) {
                    scheduler.add(process);
                }
                processes.remove(time);
            }
            scheduler.process(time);
            time++;
        }

        return scheduler.results();
    }

    /**
     * Give summary data of the simulation
     *
     * @return The average turnaroundTime and average waitingTime
     */
    public String summary() {
        return scheduler.summary();
    }
}
