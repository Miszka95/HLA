package pl.edu.wat.simulation.restaurant;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;
import pl.edu.wat.simulation.Interaction;

public class RestaurantFederate extends Federate {

    private static final String NAME = "RestaurantFederate";

    @Override
    protected void init() {
        ambassador = new RestaurantAmbassador();
        timeStep = 5.0;
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void publishAndSubscribe() {
        publishInteraction(Interaction.JOIN_QUEUE);
        publishInteraction(Interaction.ALLOW_TO_ENTER);
        subscribeInteraction(Interaction.ARRIVE);
        subscribeInteraction(Interaction.PAY_AND_LEAVE);
    }

    @Override
    protected void run() {
        while (ambassador.isRunning()) {
            Federation.advanceTime(timeStep, ambassador);
            System.out.println(NAME + ": " + ambassador.getFederateTime());
            Federation.tick();
        }
    }

    public static void main(String[] args) {
        new RestaurantFederate().runFederate();
    }
}
