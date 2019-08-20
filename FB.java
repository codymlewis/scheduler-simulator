import java.util.ArrayList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.Comparator;
import java.util.Collections;

/**
 * <h1>FB - Comp2240A1</h1>
 *
 * The round robin scheduler
 *
 * @author Cody Lewis
 * @version 1
 * @since 2019-08-17
 */

public class FB extends Scheduler {
    private static final int SLICE_SIZE = 4;
    private String prevPid;

    private class FBProcess implements Comparable<FBProcess>, Comparator<FBProcess> {
        private Process process;
        private Integer priority;

        public FBProcess() {
            priority = 0;
        }

        public FBProcess(Process process) {
            this.process = process;
            priority = 0;
        }

        public void incrementPriority() {
            if (priority < 5) {
                priority++;
            }
        }

        public Process getProcess() {
            return process;
        }

        public Integer getPriority() {
            return priority;
        }

        public int compareTo(FBProcess other) {
            return priority.compareTo(other.getPriority());
        }

        public int compare(FBProcess a, FBProcess b) {
            return a.compareTo(b);
        }

        public String toString() {
            return String.format("%s: %d", process.toString(), priority);
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
        // Collections.sort(queue, new FBProcess());
    }

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

    @Override
    public void process(int time) {
        FBProcess head = queue.get(0);
        if (head != null) {
            if (newProcess) {
                switching--;
                if (switching == 0) {
                    Collections.sort(queue, new FBProcess());
                    switchProcess(time);
                }
            } else if (head.getProcess().process(time)) {
                processed.add(queue.remove(0).getProcess());
                newProcess = true;
            } else {
                slice = (slice + 1) % SLICE_SIZE;
                if (slice == 0 && queue.size() > 1) {
                    head.incrementPriority();
                    add(queue.remove(0));
                    newProcess = true;
                }
            }
        }
    }

    @Override
    protected void switchProcess(int time) {
        newProcess = false;
        switching = switchProcessTime;
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
    }
}
