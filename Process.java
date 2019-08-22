/**
 * <h1>Process - Comp2240A1</h1>
 *
 * A simple glorified struct style class for a process.
 *
 * @author Cody Lewis (c3283349)
 * @version 1
 * @since 2019-08-16
 */

public class Process {
    private String pid; // ID
    private Integer arrivalTime; // Arrive
    private Integer serviceTime; // ExecSize
    private Integer turnaroundTime;
    private Integer waitingTime;
    private Integer lastProcessedTime;
    private Integer startTime;

    /**
     * The default constructor
     */
    public Process() {
        pid = "";
        arrivalTime = 0;
        serviceTime = 0;
        turnaroundTime = 0;
        waitingTime = 0;
        lastProcessedTime = 0;
        startTime = 0;
    }

    /**
     * Member setting constructor
     *
     * @param pid process id
     * @param arrivalTime time the the process arrived at the processor
     * @param serviceTime time the process takes to execute
     */
    public Process(String pid, int arrivalTime, int serviceTime) {
        this();
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
        this.lastProcessedTime = arrivalTime;
    }

    /**
     * Get the process id
     *
     * @return process id
     */
    public String getPid() {
        return pid;
    }

    /**
     * Get the time that the process arrived at the processor
     *
     * @return time that the process arrived at the processor
     */
    public Integer getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Get the time that the process takes to execute
     *
     * @return time that the process takes to execute
     */
    public Integer getServiceTime() {
        return serviceTime;
    }

    /**
     * Set the process id
     *
     * @param pid process id
     */
    public void setPid(String pid) {
        this.pid = pid;
    }

    /**
     * Set the time that the process arrived at the processor
     *
     * @param arrivalTime time that the process arrived at the processor
     */
    public void setArrivalTime(Integer arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Set the time that the process takes to execute
     *
     * @param serviceTime time that the process takes to execute
     */
    public void setServiceTime(Integer serviceTime) {
        this.serviceTime = serviceTime;
    }

    /**
     * Process for the slice of time
     *
     * @param time current time
     * @param slice Time slice allocate to this, 0 means no slicing
     * @return amount of time taken
     */
    public int process(int time, int slice) {
        if (lastProcessedTime == arrivalTime) {
            startTime = time;
        }
        waitingTime += (time - lastProcessedTime);
        int timeTaken = 0;
        if (slice == 0) {
            timeTaken = serviceTime;
        } else {
            timeTaken = Integer.min(slice, serviceTime);
        }
        turnaroundTime += (time - lastProcessedTime + timeTaken);
        serviceTime -= timeTaken;
        lastProcessedTime = time + timeTaken;
        return timeTaken;
    }

    /**
     * Get the turnaround time
     *
     * @return Time taken waiting and excuting
     */
    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    /**
     * Get the waiting time
     *
     * @return Time taken waiting
     */
    public int getWaitingTime() {
        return waitingTime;
    }

    /**
     * Get the start time
     *
     * @return time when execution started
     */
    public int getStartTime() {
        return startTime;
    }

    /**
     * Query whether this has finished processing
     *
     * @return true if finished else false
     */
    public boolean finished() {
        return serviceTime == 0;
    }
}

