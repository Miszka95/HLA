package pl.edu.wat.simulation;

import pl.edu.wat.simulation.client.ClientFederate;
import pl.edu.wat.simulation.kitchen.KitchenFederate;

import java.util.Random;

public class Randomizer {

    private static final int MAX_ITERATIONS_TO_MAKE_MEAL = 5;
    private static final int MAX_ITERATIONS_TO_EAT_MEAL = 4;

    private static final Random random = new Random();

    public static boolean test(double probability) {
        return random.nextFloat() < probability;
    }

    public static Double preparationTime() {
        return random.nextDouble() * KitchenFederate.TIME_STEP * MAX_ITERATIONS_TO_MAKE_MEAL;
    }

    public static Double eatingTime() {
        return random.nextDouble() * ClientFederate.TIME_STEP * MAX_ITERATIONS_TO_EAT_MEAL;
    }
}
