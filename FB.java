import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/**
 * <h1>FB - Comp2240A1</h1>
 *
 * The Feedback scheduler
 *
 * @author Cody Lewis
 * @version 1
 * @since 2019-08-17
 */

public class FB extends Scheduler {
    private static final int SLICE_SIZE = 4;
    private String prevPid;

    /**
     * Process wrapper class that includes priorities for feedback
     */
    private class FBProcess implements Comparable<FBProcess>,
            Comparator<FBProcess> {
        private Process process;
        private Integer priority;

        /**
         * Default constuctor
         */
        public FBProcess() {
            priority = 0;
        }

        /**
         * Input constuctor
         *
         * @param process Process to compose this of
         */
        public FBProcess(Process process) {
            this();
            this.process = process;
        }

        /**
         * Lower the priority of this
         */
        public void incrementPriority() {
            if (priority < 5) {
                priority++;
            }
        }

        /**
         * Get the process that this is composed of
         *
         * @return process contained in this
         */
        public Process getProcess() {
            return process;
        }

        /**
         * Get the priority of this
         *
         * @return Integer showing the priority of this
         */
        public Integer getPriority() {
            return priority;
        }

        /**
         * Compare the priority value of the to the other
         *
         * @param other other FBProcess to compare this to
         * @return this.compareTo(other) where the compared values are Integer
         */
        @Override
        public int compareTo(FBProcess other) {
            return priority.compareTo(other.getPriority());
        }

        /**
         * Compare two FBProcesses
         *
         * @param a a FBProcess
         * @param b another FBProcess
         * @return a.compareTo(b)
         */
        @Override
        public int compare(FBProcess a, FBProcess b) {
            return a.compareTo(b);
        }
    }

    private ArrayList<FBProcess> queue;
    private int slice;

    /**
     * Default constructor
     */
    public FB() {
        super();
        queue = new ArrayList<>();
        slice = 0;
        prevPid = "";
    }

    /**
     * Input constructor
     *
     * @param switchProcessTime time it takes to switch processes
     */
    public FB(int switchProcessTime) {
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
        queue.add(new FBProcess(process));
    }

    /**
     * Add a wrapped process to the queue and sort by priority
     *
     * @param process a wrapped process
     */
    private void add(FBProcess process) {
        queue.add(process);
        Collections.sort(queue, new FBProcess());
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
            FBProcess head = queue.get(0);
            if (newProcess) {
                Collections.sort(queue, new FBProcess());
                return switchProcess(time);
            } else {
                int processingTime = head.getProcess()
                    .process(time, SLICE_SIZE);
                if (head.getProcess().finished()) {
                    processed.add(queue.remove(0).getProcess());
                    newProcess = true;
                } else {
                    head.incrementPriority();
                    add(queue.remove(0));
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
        String curPid = queue.get(0).getProcess().getPid();
        if (!prevPid.equals(curPid)) {
            startTimes += String.format(
                "T%d: %s\n",
                time + 1,
                curPid
            );
        }
        prevPid = curPid;
        return switchProcessTime;
    }
}
