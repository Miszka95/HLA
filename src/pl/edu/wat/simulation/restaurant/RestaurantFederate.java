package pl.edu.wat.simulation.restaurant;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;

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
