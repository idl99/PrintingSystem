package uk.ac.westminster.ihanlelwala.printingsystem;

public interface ServicePrinter extends Printer {

    public final int FULL_PAPER_TRAY  = 250 ;
    public final int SHEETS_PER_PACK    = 50  ;

    public final int FULL_TONER_LEVEL        = 500 ;
    public final int MINIMUM_TONER_LEVEL     = 10  ;
    public final int PAGES_PER_TONER_CARTRIDGE  = 500 ;

    // Technician methods
    public void replaceTonerCartridge() ;
    public void refillPaper() ;

}
