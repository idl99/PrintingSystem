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
            System.out.printf("Cannot replace toner. Toner Level should be at a maximum of %d before it can be replaced." + "Current Toner Level is %d.\n",
                    MINIMUM_TONER_LEVEL, currentTonerLevel);
            try {
                wait(5000);
                tonerCannotBeReplaced = currentTonerLevel >= MINIMUM_TONER_LEVEL;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Allow toner technician to refill cartridge
        System.out.println("Replacing toner cartridge...");
        currentTonerLevel += PAGES_PER_TONER_CARTRIDGE;
        System.out.println("Successfully replaced Toner Cartridge.");

         notifyAll(); // If this method is not invoked, then the threads infinitely waiting (like Student threads)
                        // will not execute
    }

    @Override
    public synchronized void refillPaper() {
        boolean printerCannotBeRefilled = (currentPaperLevel + SHEETS_PER_PACK) > FULL_PAPER_TRAY;
        while (printerCannotBeRefilled) {
            System.out.printf("Cannot refill printer with paper at this time. Current paper level is %d. " +
                    "Maximum paper level is %d.\n", currentPaperLevel, FULL_PAPER_TRAY);
            try {
                wait(5000);
                printerCannotBeRefilled = (currentPaperLevel + SHEETS_PER_PACK) > FULL_PAPER_TRAY;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Allow paper technician to refill paper
        System.out.println("Refilling printer with paper...");
        currentPaperLevel += SHEETS_PER_PACK;
        System.out.printf("Refilled tray with pack of paper. New Paper Level: %d\n", currentPaperLevel);

        notifyAll(); // If this method is not invoked, then the threads infinitely waiting (like Student threads)
                        // will not execute
    }

    @Override
    public synchronized void printDocument(Document document) {
        int numberOfPages = document.getNumberOfPages();
        boolean insufficientPaperLevel = numberOfPages > currentPaperLevel;
        boolean insufficientTonerLevel = numberOfPages > currentTonerLevel;

        while (insufficientPaperLevel || insufficientTonerLevel) {
            // User cannot print
            if(insufficientPaperLevel && insufficientTonerLevel) {
                System.out.printf("Out of paper and toner. Current Paper Level is %d and Toner Level is %d. The document you wish to print is %d pages.\n",
                        currentPaperLevel, currentTonerLevel, numberOfPages);
            }
            else if(insufficientPaperLevel) {
                System.out.printf("Out of paper. Current Paper Level is %d. The document you wish to print is %d pages.\n",
                        currentPaperLevel, numberOfPages);
            }
            else {
                System.out.printf("Out of toner. Current Toner Level is %d. The document you wish to print is %d pages.\n",
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

        System.out.printf("Printing document with page length: %d.\n", numberOfPages);
        currentPaperLevel -= numberOfPages;
        currentTonerLevel -= numberOfPages;
        numberOfDocumentsPrinted++;
        System.out.printf("Successfully printed the document. New Paper Level is %d and Toner Level is %d.\n",
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
