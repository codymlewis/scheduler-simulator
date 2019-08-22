import java.util.LinkedList;
import java.util.Queue;

/**
 * <h1>FCFS - Comp2240A1</h1>
 *
 * The first come fist served scheduler
 *
 * @author Cody Lewis
 * @version 1
 * @since 2019-08-17
 */

public class FCFS extends Scheduler {
    private Queue<Process> queue;

    /**
     * Default constructor
     */
    public FCFS() {
        super();
        queue = new LinkedList<>();
    }

    public FCFS(int switchProcessTime) {
        super(switchProcessTime);
        queue = new LinkedList<>();
    }

    /**
     * Add a process to the tail of the Queue
     *
     * @param process Process to add to the Queue
     */
    @Override
    public void add(Process process) {
        queue.add(process);
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
    public int process(int time) {
        Process head = queue.peek();
        if (head != null) {
            if (newProcess) {
                return switchProcess(time);
            } else {
                int processingTime = head.process(time, 0);
                if (head.finished()) {
                    processed.add(queue.poll());
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
        startTimes += String.format(
            "T%d: %s\n",
            time + 1,
            queue.peek().getPid()
        );
        return switchProcessTime;
    }
}
