package pl.edu.wat.simulation.kitchen;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;

import java.util.Collections;

import static pl.edu.wat.simulation.InteractionType.COMPLETE_ORDER;
import static pl.edu.wat.simulation.InteractionType.ORDER_FOOD;

public class KitchenFederate extends Federate {

    private static final String NAME = "KitchenFederate";

    @Override
    protected void init() {
        ambassador = new KitchenAmbassador();
        ambassador.setFederate(this);
        timeStep = 15.0;
        PUBLISHED_INTERACTIONS = Collections.singletonList(COMPLETE_ORDER);
        SUBSCRIBED_INTERACTIONS = Collections.singletonList(ORDER_FOOD);
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
        new KitchenFederate().runFederate();
    }
}
