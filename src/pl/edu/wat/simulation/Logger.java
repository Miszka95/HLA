package pl.edu.wat.simulation;

public class Logger {

    public static void log(String pattern, Object... args) {
        System.out.println(String.format(pattern, args));
    }
}
