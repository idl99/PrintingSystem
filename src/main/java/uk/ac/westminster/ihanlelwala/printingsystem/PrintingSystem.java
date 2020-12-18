package uk.ac.westminster.ihanlelwala.printingsystem;

public class PrintingSystem {

    public static void main(String[] args) throws InterruptedException {

        ServicePrinter printer = new LaserPrinter("HP Printer", 50, 100);

        ThreadGroup students = new ThreadGroup("students");
        ThreadGroup technicians = new ThreadGroup("technicians");

        Thread student1 = new Thread(students, new Student(printer, "student1"));
        Thread student2 = new Thread(students, new Student(printer, "student2"));
        Thread student3 = new Thread(students, new Student(printer, "student3"));
        Thread student4 = new Thread(students, new Student(printer, "student4"));

        Thread paperTechnician = new Thread(technicians, new PaperTechnician(printer, "Mr. Paper Technician"));
        Thread tonerTechnician = new Thread(technicians, new TonerTechnician(printer, "Mr. Toner Technician"));

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
        paperTechnician.join();
        tonerTechnician.join();

        System.out.println("All tasks completed. Printing printer status...");
        System.out.println(printer.toString());

    }

}
