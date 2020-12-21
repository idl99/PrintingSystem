package uk.ac.westminster.ihanlelwala.printingsystem;

public class PrintingSystem {

    public static void main(String[] args) throws InterruptedException {

        // Set current Toner Level to a reasonably lesser value considering the number of documents and minimum number of pages per document
        // to increase the chances of at least one toner refill.
        // Otherwise, if Toner Level is set to a higher value, there's a chance a Toner refill will never be required
        // before the printing jobs are over.
        ServicePrinter printer = new LaserPrinter("HP Printer", 50, 50);

        String studentsThreadGroupName = "students";
        ThreadGroup students = new ThreadGroup(studentsThreadGroupName);

        Thread student1 = new Thread(students, new Student(studentsThreadGroupName, printer, "student1"));
        Thread student2 = new Thread(students, new Student(studentsThreadGroupName, printer, "student2"));
        Thread student3 = new Thread(students, new Student(studentsThreadGroupName, printer, "student3"));
        Thread student4 = new Thread(students, new Student(studentsThreadGroupName, printer, "student4"));

        String techniciansThreadGroupName = "technicians";
        ThreadGroup technicians = new ThreadGroup("technicians");

        Thread paperTechnician = new Thread(technicians,
                new PaperTechnician(techniciansThreadGroupName, printer, "Mr. Paper Technician"));
        Thread tonerTechnician = new Thread(technicians,
                new TonerTechnician(techniciansThreadGroupName, printer, "Mr. Toner Technician"));

        student1.start();
        student2.start();
        student3.start();
        student4.start();
        paperTechnician.start();
        tonerTechnician.start();

        student1.join();
        student2.join();
        student3.join();
        student4.join();
        // paperTechnician.join(); // No need to wait for paper technician to finish refilling all 3 paper packs since printing jobs are over.
                                    // Same applies for toner technician.
        // tonerTechnician.join();

        System.out.println(ConsoleColors.GREEN + "\nAll tasks completed. Printing printer status...\n" + ConsoleColors.RESET);

        System.out.print("==================================================\n" +
                          "                PRINTER SUMMARY                  \n" +
                          "=================================================\n");
        System.out.println(printer.toString());

        System.exit(0);
    }

}
