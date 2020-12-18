package uk.ac.westminster.ihanlelwala.printingsystem;

/**
 * TODO add comments for the methods
 * https://www.geeksforgeeks.org/object-level-class-level-lock-java/
 */
public class LaserPrinter implements ServicePrinter {

    private String id;
    private int currentPaperLevel;
    private int currentTonerLevel;
    private int numberOfDocumentsPrinted;

    // Opting for eager initialization for better thread safety during initialization
    // than lazy initialization (creating instance in the getInstance method)
    private static LaserPrinter instance = new LaserPrinter();

    // Although spec mentions to create a constructor, i have decided to make this a singleton class
    // since its a best practice suitable in this scenario, where this PrintingSystem program is only for a single printer
    private LaserPrinter() {
    }

    /**
     *
     * @return instance of Laser Printer
     */
    public static LaserPrinter getInstance() {
        return instance;
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
                    "Current Toner Level is %d.", MINIMUM_TONER_LEVEL, currentTonerLevel);
        }
    }

    @Override
    public synchronized void refillPaper() {
        boolean printerCanBeRefilled = (currentPaperLevel + SHEETS_PER_PACK) <= FULL_PAPER_TRAY;
        if(printerCanBeRefilled) {
            System.out.println("Refilling printer with paper...");
            // Allow paper technician to refill paper
            currentPaperLevel += SHEETS_PER_PACK;
            System.out.printf("Refilled tray with pack of paper. New Paper Level: %d", currentPaperLevel);
        } else {
            System.out.printf("Cannot refill printer with paper at this time. Current paper level is %d. " +
                    "Maximum paper level is %d", currentPaperLevel, FULL_PAPER_TRAY);
        }
    }

    @Override
    public synchronized void printDocument(Document document) {
        int numberOfPages = document.getNumberOfPages();
        boolean sufficientPaperLevel = numberOfPages <= currentPaperLevel;
        boolean sufficientTonerLevel = numberOfPages <= currentTonerLevel;

        if(sufficientPaperLevel && sufficientTonerLevel) {
            System.out.println("Printing document...");
            currentPaperLevel -= numberOfPages;
            currentTonerLevel -= numberOfPages;
            numberOfDocumentsPrinted++;
            System.out.printf("Successfully printed the document. New Paper Level is %d and Toner Level is %d.",
                    currentPaperLevel, currentTonerLevel);
        } else {
            // User cannot print
            if(!sufficientPaperLevel) {
                System.out.printf("Out of paper. Current Paper Level is %d. The document you wish to print is %d pages.",
                        currentPaperLevel, numberOfPages);
            }
            if(!sufficientTonerLevel) {
                System.out.printf("Out of toner. Current Toner Level is %d. The document you wish to print is %d pages.",
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
