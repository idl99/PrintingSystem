package uk.ac.westminster.ihanlelwala.printingsystem;

/**
 * defines the attributes and methods for the functions of a Laser Printer
 *
 * Additional References
 * https://www.geeksforgeeks.org/object-level-class-level-lock-java/
 * https://www.baeldung.com/cs/monitor
 */
public class LaserPrinter implements ServicePrinter {

    private String id;
    private int currentPaperLevel;
    private int currentTonerLevel;
    private int numberOfDocumentsPrinted;
    private ThreadGroup students;

    public LaserPrinter(String id, int initialPaperLevel, int initialTonerLevel, ThreadGroup students) {
        this.id = id;
        this.currentPaperLevel = initialPaperLevel;
        this.currentTonerLevel = initialTonerLevel;
        this.students = students;
        this.numberOfDocumentsPrinted = 0;
    }

    /**
     * method to replace Toner cartridge of printer
     *
     * toner can only be replaced when current toner level goes below the minimum toner level specified in {@link ServicePrinter#MINIMUM_TONER_LEVEL}
     * when toner cartridge is replaced, toner level goes upto full toner level specified in {@link ServicePrinter#FULL_TONER_LEVEL}
     */
    @Override
    public synchronized void replaceTonerCartridge() {
        boolean tonerCannotBeReplaced = currentTonerLevel >= MINIMUM_TONER_LEVEL;
        while (tonerCannotBeReplaced) {
            System.out.printf(ConsoleColors.PURPLE + "Checking toner... " + ConsoleColors.RESET +
                    "Toner need not be replaced at this time. Current Toner Level is %d\n", currentTonerLevel);
            try {
                wait(5000);
                if(allStudentsHaveFinishedPrinting()) {
                    return;
                }
                tonerCannotBeReplaced = currentTonerLevel >= MINIMUM_TONER_LEVEL;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Allow toner technician to refill cartridge
        System.out.printf(ConsoleColors.PURPLE + "Checking toner... " +
                "Toner is low. Current Toner Level is %d. Replacing toner cartridge... ", currentTonerLevel);
        currentTonerLevel += PAGES_PER_TONER_CARTRIDGE;
        System.out.printf("Successfully replaced Toner Cartridge. New Toner Level is %d.\n" + ConsoleColors.RESET, currentTonerLevel);

         notifyAll(); // If this method is not invoked, then the threads infinitely waiting (like Student threads)
                        // will not execute
    }

    /**
     * method which tell us whether all students have finishing printing their documents
     *
     * @return true if all students have finishing printing their documents, false if otherwise.
     */
    private boolean allStudentsHaveFinishedPrinting() {
        return students.activeCount() < 1;
    }

    /**
     * method to refill paper tray of printer with paper
     *
     * paper tray can only be refilled if the new paper level does not exceed the full (maximum) paper level specified in {@link ServicePrinter#FULL_PAPER_TRAY}
     * each paper refill increases the paper level by the number of papers in pack specified in {@link ServicePrinter#SHEETS_PER_PACK}
     */
    @Override
    public synchronized void refillPaper() {
        boolean printerCannotBeRefilled = (currentPaperLevel + SHEETS_PER_PACK) > FULL_PAPER_TRAY;
        while (printerCannotBeRefilled) {
            System.out.printf(ConsoleColors.YELLOW + "Checking paper... " + ConsoleColors.RESET +
                    "Paper Tray cannot be refilled at this time (exceeds maximum paper level). " +
                    "Current paper level is %d and Maximum paper level is %d.\n", currentPaperLevel, FULL_PAPER_TRAY);
            try {
                wait(5000);
                if(allStudentsHaveFinishedPrinting()) {
                    return;
                }
                printerCannotBeRefilled = (currentPaperLevel + SHEETS_PER_PACK) > FULL_PAPER_TRAY;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Allow paper technician to refill paper
        System.out.print(ConsoleColors.YELLOW + "Checking paper... Refilling printer with paper... ");
        currentPaperLevel += SHEETS_PER_PACK;
        System.out.printf("Refilled tray with pack of paper. New Paper Level: %d.\n" + ConsoleColors.RESET, currentPaperLevel);

        notifyAll(); // If this method is not invoked, then the threads infinitely waiting (like Student threads)
                        // will not execute
    }

    /**
     * method to print a document
     *
     * a student can print a document only if there is sufficient paper and toner to print the given document. Else
     * student has to wait until notified by a technician (after refilling paper or replacing toner cartridge)
     * to attempt to print the document again
     *
     * printing a document reduces both the paper level and toner level by the number of pages in the document
     *
     * @param document the {@link Document} to be printed by the student.
     */
    @Override
    public synchronized void printDocument(Document document) {

        String student = document.getUserID();
        String docName = document.getDocumentName();
        int numberOfPages = document.getNumberOfPages();

        boolean insufficientPaperLevel = numberOfPages > currentPaperLevel;
        boolean insufficientTonerLevel = numberOfPages > currentTonerLevel;

        while (insufficientPaperLevel || insufficientTonerLevel) {
            // User cannot print
            if(insufficientPaperLevel && insufficientTonerLevel) {
                System.out.printf(ConsoleColors.RED + "[%s][%s][%dpg] - Out of paper and toner. Current Paper Level is %d and Toner Level is %d.\n" + ConsoleColors.RESET,
                        student, docName, numberOfPages, currentPaperLevel, currentTonerLevel);
            }
            else if(insufficientPaperLevel) {
                System.out.printf(ConsoleColors.RED + "[%s][%s][%dpg] - Out of paper. Current Paper Level is %d.\n" + ConsoleColors.RESET,
                        student, docName, numberOfPages, currentPaperLevel);
            }
            else {
                System.out.printf(ConsoleColors.RED + "[%s][%s][%dpg] - Out of toner. Current Toner Level is %d.\n" + ConsoleColors.RESET,
                        student, docName, numberOfPages, currentTonerLevel);
            }

            try {
                wait();
                insufficientPaperLevel = numberOfPages > currentPaperLevel;
                insufficientTonerLevel = numberOfPages > currentTonerLevel;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("[%s][%s][%dpg] - Printing document with page length: %d.\n",
                student, docName, numberOfPages, numberOfPages);
        currentPaperLevel -= numberOfPages;
        currentTonerLevel -= numberOfPages;
        numberOfDocumentsPrinted++;
        System.out.printf("[%s][%s][%dpg] - Successfully printed the document. New Paper Level is %d and Toner Level is %d.\n",
                student, docName, numberOfPages,
                currentPaperLevel, currentTonerLevel);
    }

    /**
     * this method returns a string representation of the printer's current staate
     *
     * @return string containing {@link #id}, LaserPrinter{@link #currentPaperLevel},
     * {@link #currentTonerLevel}, LaserPrinter{@link #numberOfDocumentsPrinted}
     */
    @Override
    public synchronized String toString() {
        return "LaserPrinter{" +
                "PrinterID: '" + id + '\'' + ", " +
                "Paper Level: " + currentPaperLevel + ", " +
                "Toner Level: " + currentTonerLevel + ", " +
                "Documents Printed: " + numberOfDocumentsPrinted +
                '}';
    }

}
