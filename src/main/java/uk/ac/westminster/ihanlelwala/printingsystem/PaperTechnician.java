package uk.ac.westminster.ihanlelwala.printingsystem;

import java.util.Random;

/**
 * defines the behavior of a Paper Technician, who refills the paper tray of a printer with paper
 *
 * initially this class was made to implement {@link Runnable} interface since the spec doesn't have a requirement for
 * custom Thread behavior. however, later the following note was mentioned in the spec,
 *
 * <b>"Note that this class is a thread"</b>
 *
 */
public class PaperTechnician extends Thread {

    /**
     * Spec says to store the threadGroup and name in private variables in the {@link PaperTechnician} class
     * However, the Thread class itself stores the threadGroup and name.
     * Hence, its a bad practice to store the same information in sub classes (redundancy).
     * Therefore, i have omitted them here and passed to the super class {@link Thread} through the call
     * to the super constructor in the {@link PaperTechnician} (sub class) constructor defined below
     */

    private ServicePrinter printer;

    public PaperTechnician(ThreadGroup threadGroup, ServicePrinter printer, String name) {
        super(threadGroup, name);
        this.printer = printer;
    }

    @Override
    public void run() {
        Random random = new Random();
        int numberOfRefills = 3;

        for (int i = 1; i <= numberOfRefills; i++) {
            // System.out.printf("Paper Technician %s is attempting to refill paper pack no. %d\n", name, i);
            printer.refillPaper();

            // Excerpt from spec
            // Paper Technician's behaviour is to ... He/she should "sleep" for a random amount of time between each attempt to refill the paper.
            int MINIMUM_SLEEPING_TIME = 1000;
            int MAXIMUM_SLEEPING_TIME = 5000;
            int sleepingTime = MINIMUM_SLEEPING_TIME + random.nextInt(MAXIMUM_SLEEPING_TIME - MINIMUM_SLEEPING_TIME);
            // int sleepingTime = 5000;
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.printf("Paper Technician %s was interrupted during sleeping time " +
                                "after refilling paper pack no. %d.\n",
                        sleepingTime, i);
            }
        }

        System.out.printf("Paper Technician %s finished attempts to refill printer with paper packs.\n", getName());
    }
}
