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
        boolean tonerCanBeReplaced = currentTonerLevel < MINIMUM_TONER_LEVEL;
        if(tonerCanBeReplaced) {
            System.out.println("Replacing toner cartridge...");
            // Allow toner technician to refill cartridge
            currentTonerLevel += PAGES_PER_TONER_CARTRIDGE;
            System.out.println("Successfully replaced Toner Cartridge.");
        } else {
            System.out.printf("Cannot replace toner. Toner Level should be at a maximum of %d before it can be replaced." +
                    "Current Toner Level is %d.\n", MINIMUM_TONER_LEVEL, currentTonerLevel);
        }
    }

    @Override
    public synchronized void refillPaper() {
        boolean printerCanBeRefilled = (currentPaperLevel + SHEETS_PER_PACK) <= FULL_PAPER_TRAY;
        if(printerCanBeRefilled) {
            System.out.println("Refilling printer with paper...");
            // Allow paper technician to refill paper
            currentPaperLevel += SHEETS_PER_PACK;
            System.out.printf("Refilled tray with pack of paper. New Paper Level: %d\n", currentPaperLevel);
        } else {
            System.out.printf("Cannot refill printer with paper at this time. Current paper level is %d. " +
                    "Maximum paper level is %d.\n", currentPaperLevel, FULL_PAPER_TRAY);
        }
    }

    @Override
    public synchronized void printDocument(Document document) {
        int numberOfPages = document.getNumberOfPages();
        boolean sufficientPaperLevel = numberOfPages <= currentPaperLevel;
        boolean sufficientTonerLevel = numberOfPages <= currentTonerLevel;

        if(sufficientPaperLevel && sufficientTonerLevel) {
            System.out.printf("Printing document with page length: %d.\n", numberOfPages);
            currentPaperLevel -= numberOfPages;
            currentTonerLevel -= numberOfPages;
            numberOfDocumentsPrinted++;
            System.out.printf("Successfully printed the document. New Paper Level is %d and Toner Level is %d.\n",
                    currentPaperLevel, currentTonerLevel);
        } else {
            // User cannot print
            if(!sufficientPaperLevel && !sufficientTonerLevel) {
                System.out.printf("Out of paper and toner. Current Paper Level is %d and Toner Level is %d. The document you wish to print is %d pages.\n",
                        currentPaperLevel, currentTonerLevel, numberOfPages);
            }
            else if(!sufficientPaperLevel) {
                System.out.printf("Out of paper. Current Paper Level is %d. The document you wish to print is %d pages.\n",
                        currentPaperLevel, numberOfPages);
            }
            else {
                System.out.printf("Out of toner. Current Toner Level is %d. The document you wish to print is %d pages.\n",
                        currentTonerLevel, numberOfPages);
            }
        }
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
