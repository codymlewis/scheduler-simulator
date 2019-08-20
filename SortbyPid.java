import java.util.Comparator;

public class SortbyPid implements Comparator<Process> {
    public int compare(Process a, Process b) {
        return a.getPid().compareTo(b.getPid());
    }
}
