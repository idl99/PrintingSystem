package uk.ac.westminster.ihanlelwala.printingsystem;

/**
 * TODO add comments for the methods
 * https://www.geeksforgeeks.org/object-level-class-level-lock-java/
 * https://www.baeldung.com/cs/monitor
 */
public class LaserPrinter implements ServicePrinter {

    private String id;
    private int currentPaperLevel;
    private int currentTonerLevel;
    private int numberOfDocumentsPrinted;

    public LaserPrinter(String id, int currentPaperLevel, int currentTonerLevel) {
        this.id = id;
        this.currentPaperLevel = currentPaperLevel;
        this.currentTonerLevel = currentTonerLevel;
        this.numberOfDocumentsPrinted = 0;
    }

    @Override
    public synchronized void replaceTonerCartridge() {
        boolean tonerCannotBeReplaced = currentTonerLevel >= MINIMUM_TONER_LEVEL;
        while (tonerCannotBeReplaced) {
            System.out.printf("Checking toner... Toner need not be replaced at this time. Current Toner Level is %d\n", currentTonerLevel);
            try {
                wait(5000);
                tonerCannotBeReplaced = currentTonerLevel >= MINIMUM_TONER_LEVEL;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Allow toner technician to refill cartridge
        System.out.printf("Checking toner... Toner is low. Current Toner Level is %d. Replacing toner cartridge... ", currentTonerLevel);
        currentTonerLevel += PAGES_PER_TONER_CARTRIDGE;
        System.out.printf("Successfully replaced Toner Cartridge. New Toner Level is %d.\n", currentTonerLevel);

         notifyAll(); // If this method is not invoked, then the threads infinitely waiting (like Student threads)
                        // will not execute
    }

    @Override
    public synchronized void refillPaper() {
        boolean printerCannotBeRefilled = (currentPaperLevel + SHEETS_PER_PACK) > FULL_PAPER_TRAY;
        while (printerCannotBeRefilled) {
            System.out.printf("Checking paper... Paper Tray cannot be refilled at this time (exceeds maximum paper level). " +
                    "Current paper level is %d and Maximum paper level is %d.\n", currentPaperLevel, FULL_PAPER_TRAY);
            try {
                wait(5000);
                printerCannotBeRefilled = (currentPaperLevel + SHEETS_PER_PACK) > FULL_PAPER_TRAY;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Allow paper technician to refill paper
        System.out.print("Checking paper... Refilling printer with paper... ");
        currentPaperLevel += SHEETS_PER_PACK;
        System.out.printf("Refilled tray with pack of paper. New Paper Level: %d.\n", currentPaperLevel);

        notifyAll(); // If this method is not invoked, then the threads infinitely waiting (like Student threads)
                        // will not execute
    }

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
                System.out.printf("[%s][%s][%dpg] - Out of paper and toner. Current Paper Level is %d and Toner Level is %d.\n",
                        student, docName, numberOfPages,
                        currentPaperLevel, currentTonerLevel, numberOfPages);
            }
            else if(insufficientPaperLevel) {
                System.out.printf("[%s][%s][%dpg] - Out of paper. Current Paper Level is %d.\n",
                        student, docName, numberOfPages,
                        currentPaperLevel, numberOfPages);
            }
            else {
                System.out.printf("[%s][%s][%dpg] - Out of toner. Current Toner Level is %d.\n",
                        student, docName, numberOfPages,
                        currentTonerLevel, numberOfPages);
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
     * TODO determine whether this method should also be synchronized
     *  to ensure data correctness
     * @return
     */
    @Override
    public String toString() {
        return "LaserPrinter{" +
                "PrinterID: '" + id + '\'' + ", " +
                "Paper Level: " + currentPaperLevel + ", " +
                "Toner Level: " + currentTonerLevel + ", " +
                "Documents Printed: " + numberOfDocumentsPrinted +
                '}';
    }

}
