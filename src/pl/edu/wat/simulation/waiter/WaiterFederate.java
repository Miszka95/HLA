package pl.edu.wat.simulation.waiter;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;
import pl.edu.wat.simulation.InteractionType;

public class WaiterFederate extends Federate {

    private static final String NAME = "WaiterFederate";

    @Override
    protected void init() {
        ambassador = new WaiterAmbassador();
        ambassador.setFederate(this);
        timeStep = 10.0;
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void publishAndSubscribe() {
        publishInteraction(InteractionType.SERVE_ORDER);
        subscribeInteraction(InteractionType.COMPLETE_ORDER);
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
        new WaiterFederate().runFederate();
    }
}
