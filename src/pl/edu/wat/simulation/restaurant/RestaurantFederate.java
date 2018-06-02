package pl.edu.wat.simulation.restaurant;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;

import java.util.Arrays;

import static pl.edu.wat.simulation.InteractionType.*;

public class RestaurantFederate extends Federate {

    private static final String NAME = "RestaurantFederate";

    @Override
    protected void init() {
        ambassador = new RestaurantAmbassador();
        ambassador.setFederate(this);
        timeStep = 5.0;
        PUBLISHED_INTERACTIONS = Arrays.asList(JOIN_QUEUE, ALLOW_TO_ENTER);
        SUBSCRIBED_INTERACTIONS = Arrays.asList(ARRIVE, PAY_AND_LEAVE);
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void run() {
        while (ambassador.isRunning()) {
            Federation.advanceTime(timeStep, ambassador);
            System.out.println(NAME + ": " + ambassador.getFederateTime());
            ambassador.getReceivedInteractions().forEach(System.out::println);
            Federation.tick();
        }
    }

    public static void main(String[] args) {
        new RestaurantFederate().runFederate();
    }
}
