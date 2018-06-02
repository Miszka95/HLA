package pl.edu.wat.simulation.kitchen;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;
import pl.edu.wat.simulation.InteractionType;

public class KitchenFederate extends Federate {

    private static final String NAME = "KitchenFederate";

    @Override
    protected void init() {
        ambassador = new KitchenAmbassador();
        ambassador.setFederate(this);
        timeStep = 15.0;
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void publishAndSubscribe() {
        publishInteraction(InteractionType.COMPLETE_ORDER);
        subscribeInteraction(InteractionType.ORDER_FOOD);
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
