import java.util.Comparator;

/**
 * <h1>SortbyPid - Comp2240A1</h1>
 * Sort the processes by their id
 *
 * @author Cody Lewis (c3283349)
 * @version 1
 * @since 2019-08-17
 */

public class SortbyPid implements Comparator<Process> {
    public int compare(Process a, Process b) {
        return a.getPid().compareTo(b.getPid());
    }
}
