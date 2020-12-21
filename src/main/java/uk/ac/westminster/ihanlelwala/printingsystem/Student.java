package uk.ac.westminster.ihanlelwala.printingsystem;

import java.util.Random;

public class Student implements Runnable {

    private String threadGroup;
    private Printer printer;
    private String name;

    public Student(String threadGroup, Printer printer, String name) {
        this.threadGroup = threadGroup;
        this.printer = printer;
        this.name = name;
    }

    @Override
    public void run() {
        Random random = new Random();
        int numberOfDocumentsPerStudent = 5;

        for (int i = 1; i <= numberOfDocumentsPerStudent; i++) {

            int MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT = 1;

            // TODO verify if the following assumption is okay
            // If this is greater than the Toner Level, then there is a problem eventually where Toner won't refill
            // because Toner Level hasn't dropped below the specified threshold.
            // But a student may want to print a document with more pages than the current toner level.
            int MAXIMUM_NUMBER_OF_PAGE_PER_DOCUMENT = 10;

            int numberOfPages = MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT +
                    random.nextInt(MAXIMUM_NUMBER_OF_PAGE_PER_DOCUMENT - MINIMUM_NUMBER_OF_PAGE_PER_DOCUMENT); // Adding 1 to ensure document is at least one page in length
            String documentName = "cwk" + i;

            // System.out.printf("Printing document: %s of student: %s with page length: %s.\n", documentName, name, numberOfPages);
            Document document = new Document(this.name, documentName, numberOfPages);
            printer.printDocument(document);

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

        System.out.printf("Student %s finished printing documents.\n", name);
    }
}
