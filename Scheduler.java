import java.util.Collections;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * <h1>Scheduler - Comp2240A1</h1>
 *
 * Interface for the scheduler classes
 *
 * @author Cody Lewis (c3283349)
 * @version 1
 * @since 2019-08-17
 */

public abstract class Scheduler {
    protected ArrayList<Process> processed;
    protected Integer switchProcessTime;
    protected boolean newProcess;
    protected String startTimes;

    /**
     * Default constructor
     */
    protected Scheduler() {
        processed = new ArrayList<>();
        switchProcessTime = 0;
        newProcess = true;
        startTimes = "";
    }

    /**
     * Input constructor
     *
     * @param switchProcessTime Time to take to switch processes
     */
    protected Scheduler(int switchProcessTime) {
        this();
        this.switchProcessTime = switchProcessTime;
    }

    /**
     * Add a process to the scheduler
     *
     * @param process Process to add to the schedule
     */
    public abstract void add(Process process);

    /**
     * Check whether the schedule is empty
     *
     * @return True if schedule is empty else false
     */
    public abstract boolean empty();

    /**
     * Process the Process at the head of the queue
     *
     * @param time current time
     * @return time the processing operation took
     */
    public abstract int process(int time);

    /**
     * Switch to a new process
     *
     * @param time current time
     * @return time that the switching operation took
     */
    protected abstract int switchProcess(int time);

    /**
     * Give the results of the simulation, such as the times the process was
     * execute
     *
     * @return Results of the simulation
     */
    public String results() {
        return startTimes + "\n" +
            "Process Turnaround Time Waiting Time\n" +
            processed.stream()
                .sorted(new SortbyPid())
                .map(p -> String.format(
                            "%s\t%d\t\t%d",
                            p.getPid(),
                            p.getTurnaroundTime(),
                            p.getWaitingTime()))
                .collect(Collectors.joining("\n")) + "\n";
    }

    /**
     * Give summary data of the simulation
     *
     * @return The average turnaroundTime and average waitingTime
     */
    public String summary() {
        return String.format(
                "%.2f\t\t\t%.2f",
                processed.stream()
                    .mapToDouble(p -> p.getTurnaroundTime())
                    .average()
                    .orElse(0),
                processed.stream()
                    .mapToDouble(p -> p.getWaitingTime())
                    .average()
                    .orElse(0)
        );
    }
}
