package uk.ac.westminster.ihanlelwala.printingsystem;

import java.util.Random;

/**
 * TODO determine whether i need to to maintain a private variable to store the thread group
 *  to which the Student thread belongs to (as per the spec)
 *  because anyways we will be constructing an instance of this class and be using that to create an instance of Thread
 *  then the Thread instance will anyways keep a track of the thread group.
 */
public class Student implements Runnable {

    public static final int NUMBER_OF_DOCUMENTS_PER_STUDENT = 5;
    private Printer printer;
    private String name;

    public Student(Printer printer, String name) {
        this.printer = printer;
        this.name = name;
    }

    @Override
    public void run() {
        Random random = new Random();

        for (int i = 1; i <= NUMBER_OF_DOCUMENTS_PER_STUDENT; i++) {

            int MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT = 1;
            int MAXIMUM_NUMBER_OF_PAGE_PER_DOCUMENT = 50;
            int numberOfPages = MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT +
                    random.nextInt(MAXIMUM_NUMBER_OF_PAGE_PER_DOCUMENT - MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT); // Adding 1 to ensure document is at least one page in length
            String documentName = "cwk" + i;

            System.out.printf("Printing document: %s of student: %s", name, documentName);
            Document document = new Document(this.name, documentName, numberOfPages);
            printer.printDocument(document);

            int MINIMUM_SLEEPING_TIME = 1000;
            int MAXIMUM_SLEEPING_TIME = 5000;
            int sleepingTime = MINIMUM_SLEEPING_TIME + random.nextInt(MAXIMUM_SLEEPING_TIME - MINIMUM_SLEEPING_TIME);
            try {
                Thread.sleep(sleepingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.out.printf("%s was interrupted during sleeping time after printing \'%s\' document.",
                        sleepingTime, documentName);
            }
        }

        System.out.printf("Student %s finished printing documents.", name);
    }
}
