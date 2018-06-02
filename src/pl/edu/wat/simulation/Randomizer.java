package pl.edu.wat.simulation;

import java.util.Random;

public class Randomizer {

    private static final Random random = new Random();

    public static boolean test(double probability) {
        return random.nextFloat() < probability;
    }

    public static Double preparationTime() {
        return random.nextDouble() * 15 * 5;//TODO: poprawić później
    }

    public static Double eatingTime() {
        return random.nextDouble() * 2 * 10;
    }//TODO: to też
}
