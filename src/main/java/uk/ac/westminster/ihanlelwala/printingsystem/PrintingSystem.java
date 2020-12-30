package uk.ac.westminster.ihanlelwala.printingsystem;

public class PrintingSystem {

    public static void main(String[] args) throws InterruptedException {

        // The two groups of users involved in this system
        String studentsThreadGroupName = "students";
        ThreadGroup students = new ThreadGroup(studentsThreadGroupName);

        String techniciansThreadGroupName = "technicians";
        ThreadGroup technicians = new ThreadGroup("technicians");

        /*
        Set initial Toner Level to a reasonably lesser value considering the number of pages per document
        to increase the chances of at least one toner refill.
        Otherwise, if initial Toner Level is set to a higher value, there's a chance a Toner refill will never be required
        before the printing jobs are over.

        Same as above applies for initial Paper level

        Suggested:
        set paper value to 10 for higher chance at one paper refill in the least. If a student acquires the printer
        first (before the paper technician) then printer will most probably run out of paper for the next student

        set toner value to 50 for higher chance at one toner replacement in the least.
        */
        ServicePrinter printer = new LaserPrinter("HP Printer", 10, 50, students);

        Thread student1 = new Thread(students, new Student(studentsThreadGroupName, printer, "student1"));
        Thread student2 = new Thread(students, new Student(studentsThreadGroupName, printer, "student2"));
        Thread student3 = new Thread(students, new Student(studentsThreadGroupName, printer, "student3"));
        Thread student4 = new Thread(students, new Student(studentsThreadGroupName, printer, "student4"));

        Thread paperTechnician = new Thread(technicians,
                new PaperTechnician(techniciansThreadGroupName, printer, "paperTechnician"));
        Thread tonerTechnician = new Thread(technicians,
                new TonerTechnician(techniciansThreadGroupName, printer, "tonerTechnician"));

        // start the threads so that they enter the runnable state and await their turn of execution
        // however, we cannot assure which thread will actually execute first or the order in which they'll execute
        student1.start();
        student2.start();
        student3.start();
        student4.start();
        paperTechnician.start();
        tonerTechnician.start();

        // wait for all threads to terminate upon completion of their execution
        // before proceeding to print the summary and exit the program
        student1.join();
        student2.join();
        student3.join();
        student4.join();
        paperTechnician.join();
        tonerTechnician.join();

        System.out.println(ConsoleColors.GREEN + "\nAll tasks completed. Printing printer status...\n" + ConsoleColors.RESET);

        System.out.print("==================================================\n" +
                          "                PRINTER SUMMARY                  \n" +
                          "=================================================\n");
        System.out.println(printer.toString());
    }

}
