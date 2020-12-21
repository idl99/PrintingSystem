# PrintingSystem

This program models a Printing System as a multi-threaded concurrent Java program, using appropriate Java concurrency features such as threads, thread groups, monitors. 
The Printing System's Java classes implements the behaviour of the following entities:

- a shared printer
- four students who share it and
- two technicians who service it.

The students each use the printer to print several documents. Each technician only does one job, i.e. one only refills the paper tray & the other only replaces the toner cartridge.
