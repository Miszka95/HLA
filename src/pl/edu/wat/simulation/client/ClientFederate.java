package pl.edu.wat.simulation.client;

import pl.edu.wat.simulation.Federate;
import pl.edu.wat.simulation.Federation;

import java.util.Arrays;
import java.util.Collections;

import static pl.edu.wat.simulation.InteractionType.*;

public class ClientFederate extends Federate {

    private static final String NAME = "ClientFederate";

    @Override
    protected void init() {
        ambassador = new ClientAmbassador();
        ambassador.setFederate(this);
        timeStep = 2.0;
        PUBLISHED_INTERACTIONS = Arrays.asList(ARRIVE, LEAVE_QUEUE, ENTER, ORDER_FOOD, PAY_AND_LEAVE);
        SUBSCRIBED_INTERACTIONS = Arrays.asList(JOIN_QUEUE, ALLOW_TO_ENTER, SERVE_ORDER);
        Federation.join(NAME, ambassador);
    }

    @Override
    protected void run() {
        while (ambassador.isRunning()) {
            Federation.advanceTime(timeStep, ambassador);
            Client client = Client.create();
            sendInteraction(ARRIVE, Collections.singletonList(client.getId()));
            System.out.println(NAME + ": " + ambassador.getFederateTime());
            Federation.tick();
        }
    }

    public static void main(String[] args) {
        new ClientFederate().runFederate();
    }
}
