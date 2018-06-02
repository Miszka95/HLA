package pl.edu.wat.simulation.restaurant;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;
import pl.edu.wat.simulation.InteractionType;

public class RestaurantFederate extends Federate {

    private static final String NAME = "RestaurantFederate";

    @Override
    protected void init() {
        ambassador = new RestaurantAmbassador();
        ambassador.setFederate(this);
        timeStep = 5.0;
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void publishAndSubscribe() {
        publishInteraction(InteractionType.JOIN_QUEUE);
        publishInteraction(InteractionType.ALLOW_TO_ENTER);
        subscribeInteraction(InteractionType.ARRIVE);
        subscribeInteraction(InteractionType.PAY_AND_LEAVE);
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
