package pl.edu.wat.simulation.kitchen;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;
import pl.edu.wat.simulation.Interaction;

public class KitchenFederate extends Federate {

    private static final String NAME = "KitchenFederate";

    @Override
    protected void init() {
        ambassador = new KitchenAmbassador();
        timeStep = 15.0;
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void publishAndSubscribe() {
        publishInteraction(Interaction.COMPLETE_ORDER);
        subscribeInteraction(Interaction.ORDER_FOOD);
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
        new KitchenFederate().runFederate();
    }
}
