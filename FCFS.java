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
    public void process(int time) {
        Process head = queue.peek();
        if (head != null) {
            if (newProcess) {
                switching--;
                if (switching == 0) {
                    switchProcess(time);
                }
            } else if (head.process(time)) {
                processed.add(queue.poll());
                newProcess = true;
            }
        }
    }

    @Override
    protected void switchProcess(int time) {
        newProcess = false;
        switching = switchProcessTime;
        startTimes += String.format(
            "T%d: %s\n",
            time + 1,
            queue.peek().getPid()
        );
    }
}
