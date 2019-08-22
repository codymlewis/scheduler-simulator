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

        System.out.println("FCFS:");
        Dispatcher fcfs = new Dispatcher("FCFS", args[0]);
        System.out.println(fcfs.run());

        System.out.println("RR:");
        Dispatcher rr = new Dispatcher("RR", args[0]);
        System.out.println(rr.run());

        System.out.println("FB (constant):");
        Dispatcher fb = new Dispatcher("FB", args[0]);
        System.out.println(fb.run());

        System.out.println("NRR:");
        Dispatcher nrr = new Dispatcher("NRR", args[0]);
        System.out.println(nrr.run());

        System.out.println("Summary");
        System.out.println("Algorithm\tAverage Turnaround Time\tAverage Waiting Time");
        System.out.format("FCFS\t\t%s\n", fcfs.summary());
        System.out.format("RR\t\t%s\n", rr.summary());
        System.out.format("FB (constant)\t%s\n", fb.summary());
        System.out.format("NRR\t\t%s\n", nrr.summary());

        System.exit(0);
    }

}
