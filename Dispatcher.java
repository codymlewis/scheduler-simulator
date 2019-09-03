import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * <h1>Dispatcher - Comp2240A1</h1>
 *
 * A class for the Dispatcher, it manages the process execution simulation and
 * is composed of a scheduler.
 *
 * @author Cody Lewis (c3283349)
 * @version 1
 * @since 2019-08-17
 */

public class Dispatcher {
    private Scheduler scheduler;
    private int switchProcessTime;
    private Map<Integer, LinkedList<Process>> processes;

    /**
     * Input constructor
     *
     * @param schedulerAlgorithm Name of the scheduler algorithm to use
     * @param filename Name of the data file
     */
    public Dispatcher(String schedulerAlgorithm, String filename) {
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
        Map<Integer, LinkedList<Process>> data = null;

        try (Scanner fstream = new Scanner(new File(filename))) {
            String curLine = fstream.nextLine();
            // Check that the file is formated correctly
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
            data = fstreamToMap(fstream);
        } catch (FileNotFoundException fnfe) {
            System.err.format(
                "Error: File %s was not found. Continuing run with no processes.",
                filename
            );
        } catch (InvalidDataException ide) {
            System.err.format(
                "Error: %s Continuing run with no processes.",
                ide.getMessage()
            );
        }

        return data == null ?
            new HashMap<Integer, LinkedList<Process>>() :
            data;
    }

    /**
     * Put the file contents from the stream into Map
     *
     * @param fstream A scanner stream iterated to the process content
     * @return Map from the time of arrival to the list of processes
     */
    private Map<Integer, LinkedList<Process>> fstreamToMap(Scanner fstream) {
        String pid = "", curLine = "";
        Scanner sstream;
        int arrivalTime = 0;
        int serviceTime = 0;
        Map<Integer, LinkedList<Process>> data = new HashMap<>();

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
        PriorityQueue<Integer> eventTimes = new PriorityQueue<>();
        processes.keySet().stream().forEach(e -> eventTimes.add(e));

        while (!eventTimes.isEmpty()) {
            time = eventTimes.poll();
            if (processes.get(time) != null) {
                processes.get(time).stream().forEach(p -> scheduler.add(p));
                processes.remove(time);
            }
            if (time >= timeProcessingTo) {
                int processingTime = scheduler.process(time);
                if (processingTime != 0) {
                    eventTimes.add(time + processingTime);
                    timeProcessingTo = time + processingTime;
                }
            }
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
