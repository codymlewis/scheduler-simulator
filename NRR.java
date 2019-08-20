import java.util.LinkedList;
import java.util.Queue;

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
    private class NarrowProcess {
        private Process process;
        private int sliceSize;
        private boolean used;

        public NarrowProcess(Process process) {
            this.process = process;
            sliceSize = 4;
            used = false;
        }

        public int getSliceSize() {
            return sliceSize;
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
    }

    private Queue<NarrowProcess> queue;
    private int slice;

    /**
     * Default constructor
     */
    public NRR() {
        super();
        queue = new LinkedList<>();
        slice = 0;
    }

    public NRR(int switchProcessTime) {
        super(switchProcessTime);
        queue = new LinkedList<>();
        slice = 0;
    }

    /**
     * Add a process to the tail of the Queue
     *
     * @param process Process to add to the Queue
     */
    @Override
    public void add(Process process) {
        queue.add(new NarrowProcess(process));
    }

    /**
     * Check whether the queue is empty
     *
     * @return True if the queue is empy else false
     */
    @Override
    public boolean empty() {
        return queue.peek() == null;
    }

    @Override
    public void process(int time) {
        NarrowProcess head = queue.peek();
        if (head != null) {
            if (newProcess) {
                switching--;
                if (switching == 0) {
                    switchProcess(time);
                }
            } else if (head.getProcess().process(time)) {
                processed.add(queue.poll().getProcess());
                newProcess = true;
            } else {
                slice = (slice + 1) % queue.peek().getSliceSize();
                if (slice == 0 && queue.size() > 1) {
                    queue.add(queue.poll());
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
        queue.peek().decrementSliceSize();
        startTimes += String.format(
            "T%d: %s\n",
            time + 1,
            queue.peek().getProcess().getPid()
        );
    }
}
