/**
 * <h1>A1 - Comp2240</h1>
 *
 * The main interface class for the First Assignment of COMP2240 Operating
 * Systems. This simulates and compares a few scheduling algortihms.
 *
 *
 * @author Cody Lewis (c3283349)
 * @version 1
 * @since 2019-08-16
 */

public class A1 {
    public static void main(String args[]) {
        if (args.length < 1) {
            System.out.println("A1: missing an input file");
            System.out.println("Usage: java A1 INPUT_FILE");

            System.exit(1);
        }

        String[] dispatcherNames = {"FCFS", "RR", "FB", "NRR"};
        Dispatcher[] dispatchers = new Dispatcher[dispatcherNames.length];

        for (int i = 0; i < dispatcherNames.length; ++i) {
            System.out.println(String.format("%s:", dispatcherNames[i]));
            dispatchers[i] = new Dispatcher(dispatcherNames[i], args[0]);
            System.out.println(dispatchers[i].run());
        }

        System.out.println("Summary");
        System.out.println("Algorithm\tAverage Turnaround Time\tAverage Waiting Time");
        for (int i = 0; i < dispatcherNames.length; ++i) {
            System.out.format("%s\t\t%s\n", dispatcherNames[i], dispatchers[i].summary());
        }

        System.exit(0);
    }

}
