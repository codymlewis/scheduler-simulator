import java.util.Collections;
import java.util.ArrayList;

/**
 * <h1>Scheduler - Comp2240A1</h1>
 *
 * Interface for the scheduler classes
 *
 * @author Cody Lewis
 * @version 1
 * @since 2019-08-17
 */

public abstract class Scheduler {
    protected ArrayList<Process> processed;
    protected Integer switchProcessTime;
    protected boolean newProcess;
    protected Integer switching;
    protected String startTimes;

    protected Scheduler() {
        processed = new ArrayList<>();
        switchProcessTime = 0;
        newProcess = true;
        switching = switchProcessTime;
        startTimes = "";
    }

    protected Scheduler(int switchProcessTime) {
        this();
        this.switchProcessTime = switchProcessTime;
        switching = switchProcessTime;
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

    public abstract void process(int time);

    /**
     * Give the results of the simulation, such as the times the process was
     * execute
     *
     * @return Results of the simulation
     */
    public String results() {
        String stats = "";
        Collections.sort(processed, new SortbyPid());
        stats += startTimes;
        stats += "\nProcess Turnaround Time Waiting Time\n";
        for (Process process : processed) {
            stats += String.format(
                "%s      %d              %d\n",
                process.getPid(),
                process.getTurnaroundTime(),
                process.getWaitingTime()
            );
        }

        return stats;
    }

    /**
     * Give summary data of the simulation
     *
     * @return The average turnaroundTime and average waitingTime
     */
    public String summary() {
        double avgTurnaround = 0;
        double avgWaiting = 0;

        for (Process process : processed) {
            avgTurnaround += process.getTurnaroundTime();
            avgWaiting += process.getWaitingTime();
        }
        avgTurnaround /= processed.size();
        avgWaiting /= processed.size();

        return String.format("%.2f                     %.2f", avgTurnaround, avgWaiting);
    }

    protected abstract void switchProcess(int time);
}
