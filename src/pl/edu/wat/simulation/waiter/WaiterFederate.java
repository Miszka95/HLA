package pl.edu.wat.simulation.waiter;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;

import java.util.Collections;

import static pl.edu.wat.simulation.InteractionType.COMPLETE_ORDER;
import static pl.edu.wat.simulation.InteractionType.SERVE_ORDER;

public class WaiterFederate extends Federate {

    private static final String NAME = "WaiterFederate";

    @Override
    protected void init() {
        ambassador = new WaiterAmbassador();
        ambassador.setFederate(this);
        timeStep = 10.0;
        PUBLISHED_INTERACTIONS = Collections.singletonList(SERVE_ORDER);
        SUBSCRIBED_INTERACTIONS = Collections.singletonList(COMPLETE_ORDER);
        Federation.join(NAME, ambassador);
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
