import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/**
 * <h1>RR - Comp2240A1</h1>
 *
 * The round robin scheduler
 *
 * @author Cody Lewis (c3283349)
 * @version 1
 * @since 2019-08-17
 */

public class RR extends Scheduler {
    private String prevPid;
    private static final int SLICE_SIZE = 4;

    /**
     * A process wrapper class for Processes in this scheduler
     */
    private class RRProcess implements Comparable<RRProcess>,
            Comparator<RRProcess> {
        private Process process;
        private boolean used;
        private Integer time;

        /**
         * Input constructor
         *
         * @param process process that this will be composed of
         * @param time time this was last placed in the queue
         */
        public RRProcess(Process process, int time) {
            this.process = process;
            used = false;
            this.time = time;
        }

        /**
         * Get time this was last placed in the queue
         *
         * @return time of last placement in the queue
         */
        public Integer getTime() {
            return time;
        }

        /**
         * Update time of last placement in the queue
         *
         * @param time time last placed in queue
         */
        public void setTime(int time) {
            this.time = time;
        }

        /**
         * Get the process contained in
         *
         * @return process this is composed of
         */
        public Process getProcess() {
            return process;
        }

        /**
         * Compare the time of placement between this and other RRProcess
         *
         * @param other another RRProcess
         * @return Integer compareTo of the times
         */
        @Override
        public int compareTo(RRProcess other) {
            return time.compareTo(other.getTime());
        }

        /**
         * Compare 2 processes
         *
         * @param a A RRProcess
         * @param b another RRProcess
         * @return a.compareTo(b)
         */
        @Override
        public int compare(RRProcess a, RRProcess b) {
            return a.compareTo(b);
        }
    }

    private ArrayList<RRProcess> queue;
    private int slice;

    /**
     * Default constructor
     */
    public RR() {
        super();
        queue = new ArrayList<>();
        slice = 0;
        prevPid = "";
    }

    /**
     * Input Constructor
     *
     * @param switchProcessTime time to take to switch processes
     */
    public RR(int switchProcessTime) {
        super(switchProcessTime);
        queue = new ArrayList<>();
        slice = 0;
        prevPid = "";
    }

    /**
     * Add a process to the tail of the Queue
     *
     * @param process Process to add to the Queue
     */
    @Override
    public void add(Process process) {
        queue.add(new RRProcess(process, process.getArrivalTime()));
        Collections.sort(queue);
    }

    /**
     * Check whether the queue is empty
     *
     * @return True if the queue is empy else false
     */
    @Override
    public boolean empty() {
        return queue.isEmpty();
    }


    /**
     * Process the Process at the head of the queue
     *
     * @param time current time
     * @return time the processing operation took
     */
    @Override
    public int process(int time) {
        if (!queue.isEmpty()) {
            RRProcess head = queue.get(0);
            if (newProcess && !prevPid.equals(head.getProcess().getPid())) {
                return switchProcess(time);
            } else {
                int processingTime = head.getProcess()
                    .process(time, SLICE_SIZE);
                prevPid = head.getProcess().getPid();
                if (head.getProcess().finished()) {
                    processed.add(queue.remove(0).getProcess());
                    newProcess = true;
                } else {
                    queue.remove(0);
                    head.setTime(time + processingTime);
                    queue.add(head);
                    newProcess = true;
                }
                return processingTime;
            }
        }
        return 0;
    }

    /**
     * Switch to a new process
     *
     * @param time current time
     * @return time that the switching operation took
     */
    @Override
    protected int switchProcess(int time) {
        newProcess = false;
        slice = 0;
        startTimes += String.format(
            "T%d: %s\n",
            time + 1,
            queue.get(0).getProcess().getPid()
        );
        return switchProcessTime;
    }
}
