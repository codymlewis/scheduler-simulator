import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/**
 * <h1>RR - Comp2240A1</h1>
 *
 * The round robin scheduler
 *
 * @author Cody Lewis
 * @version 1
 * @since 2019-08-17
 */

public class NRR extends Scheduler {
    String prevPid;

    private class NarrowProcess implements Comparable<NarrowProcess>,
            Comparator<NarrowProcess> {
        private Process process;
        private int sliceSize;
        private boolean used;
        private Integer time;

        public NarrowProcess(Process process, int time) {
            this.process = process;
            sliceSize = 4;
            used = false;
            this.time = time;
        }

        public int getSliceSize() {
            return sliceSize;
        }


        public Integer getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }

        public void decrementSliceSize() {
            if (used) {
                sliceSize--;
            } else {
                used = true;
            }
        }

        public Process getProcess() {
            return process;
        }

        @Override
        public int compareTo(NarrowProcess other) {
            return time.compareTo(other.getTime());
        }

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
