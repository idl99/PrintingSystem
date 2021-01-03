package uk.ac.westminster.ihanlelwala.printingsystem;

import java.util.Random;

/**
 * defines the behavior of a Student, who uses the printer to print documents
 *
 * initially this class was made to implement {@link Runnable} interface since the spec doesn't have a requirement for
 * custom Thread behavior. however, later the following note was mentioned in the spec,
 *
 * <b>"Note that this class is a thread"</b>
 *
 */
public class Student extends Thread {

    /**
     * Spec says to store the threadGroup and name in private variables in the {@link Student} class
     * However, the Thread class itself stores the threadGroup and name.
     * Hence, its a bad practice to store the same information in sub classes (redundancy).
     * Therefore, i have omitted them here and passed to the super class {@link Thread} through the call
     * to the super constructor in the {@link Student} (sub class) constructor defined below
     */

    private Printer printer;

    public Student(ThreadGroup threadGroup, Printer printer, String name) {
        super(threadGroup, name);
        this.printer = printer;
    }

    @Override
    public void run() {
        Random random = new Random();
        int numberOfDocumentsPerStudent = 5;

        for (int i = 1; i <= numberOfDocumentsPerStudent; i++) {

            int MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT = 1;

            // If this is greater than the Toner Level, then there is a problem eventually where Toner won't refill
            // because Toner Level hasn't dropped below the specified threshold.
            // But a student may want to print a document with more pages than the current toner level.
            int MAXIMUM_NUMBER_OF_PAGE_PER_DOCUMENT = 10;

            int numberOfPages = MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT +
                    random.nextInt(MAXIMUM_NUMBER_OF_PAGE_PER_DOCUMENT - MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT); // Adding 1 to ensure document is at least one page in length
            String documentName = "cwk" + i;

            // System.out.printf("Printing document: %s of student: %s with page length: %s.\n", documentName, name, numberOfPages);
            Document document = new Document(this.getName(), documentName, numberOfPages);
            printer.printDocument(document);

            // Excerpt from spec
            // Student's behaviour is to ... He/she should "sleep" for a random amount of time between each printing request.
            boolean lastDocument = i == numberOfDocumentsPerStudent;
            if (!lastDocument) {
                int MINIMUM_SLEEPING_TIME = 1000;
                int MAXIMUM_SLEEPING_TIME = 5000;
                int sleepingTime = MINIMUM_SLEEPING_TIME + random.nextInt(MAXIMUM_SLEEPING_TIME - MINIMUM_SLEEPING_TIME);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.printf("%s was interrupted during sleeping time after printing \'%s\' document.\n",
                            sleepingTime, documentName);
                }
            }
        }

        System.out.printf(ConsoleColors.GREEN + "Student %s finished printing documents.\n" + ConsoleColors.RESET, this.getName());
    }
}
