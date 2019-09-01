import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/**
 * <h1>NRR - Comp2240A1</h1>
 *
 * The narrow round robin scheduler
 *
 * @author Cody Lewis (c3283349)
 * @version 1
 * @since 2019-08-17
 */

public class NRR extends Scheduler {
    String prevPid;

    /**
     * Wrapper class that is inclusive of the scheduling of this
     */
    private class NarrowProcess implements Comparable<NarrowProcess>,
            Comparator<NarrowProcess> {
        private Process process;
        private int sliceSize;
        private boolean used;
        private Integer time;

        /**
         * Input constructor
         *
         * @param process Process that this will be composed of
         * @param time time that this has been placed in the queue
         */
        public NarrowProcess(Process process, int time) {
            this.process = process;
            sliceSize = 4;
            used = false;
            this.time = time;
        }

        /**
         * Get the current size of a time slice for this
         *
         * @return current slice size
         */
        public int getSliceSize() {
            return sliceSize;
        }

        /**
         * Get the time that this was placed in the queue
         *
         * @return time a queue placement
         */
        public Integer getTime() {
            return time;
        }

        /**
         * Update the time of queue placement
         *
         * @param time new time of placement
         */
        public void setTime(int time) {
            this.time = time;
        }

        /**
         * Make the slice size a unit smaller
         */
        public void decrementSliceSize() {
            if (used) {
                sliceSize--;
            } else {
                used = true;
            }
        }

        /**
         * Get the process contained in this
         *
         * @return process that this is composed of
         */
        public Process getProcess() {
            return process;
        }

        /**
         * Compare the time of queue placement of this to other
         *
         * @param other Another NarrowProcess
         * @return this.time.compareTo(other.time)
         */
        @Override
        public int compareTo(NarrowProcess other) {
            return time.compareTo(other.getTime());
        }

        /**
         * Compare two NarrowProcesses
         *
         * @param a A NarrowProcess
         * @param b another NarrowProcess
         * @return a.compareTo(b)
         */
        @Override
        public int compare(NarrowProcess a, NarrowProcess b) {
            return a.compareTo(b);
        }
    }

    private ArrayList<NarrowProcess> queue;
    private int slice;

    /**
     * Default constructor
     */
    public NRR() {
        super();
        queue = new ArrayList<>();
        slice = 0;
        prevPid = "";
    }

    /**
     * Input constructor
     *
     * @param switchProcessTime Time it takes to switch process
     */
    public NRR(int switchProcessTime) {
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
        queue.add(new NarrowProcess(process, process.getArrivalTime()));
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
            NarrowProcess head = queue.get(0);
            if (newProcess && !prevPid.equals(head.getProcess().getPid())) {
                return switchProcess(time);
            } else {
                int processingTime = head.getProcess().process(time, head.getSliceSize());
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
        queue.get(0).decrementSliceSize();
        startTimes += String.format(
            "T%d: %s\n",
            time + 1,
            queue.get(0).getProcess().getPid()
        );
        return switchProcessTime;
    }
}
