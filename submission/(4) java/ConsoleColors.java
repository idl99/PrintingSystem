package uk.ac.westminster.ihanlelwala.printingsystem;

// Reference https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println
public enum ConsoleColors {

    RESET("\033[0m"), // Reset color of text
    RED("\033[0;31m"), GREEN("\033[0;32m"), PURPLE("\033[0;35m"), YELLOW("\033[0;33m");

    String consoleColor;

    ConsoleColors(String consoleColor) {
        this.consoleColor = consoleColor;
    }

    @Override
    public String toString() {
        return consoleColor;
    }
}
